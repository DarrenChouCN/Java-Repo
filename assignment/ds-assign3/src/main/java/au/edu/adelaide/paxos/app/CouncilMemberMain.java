package au.edu.adelaide.paxos.app;

import au.edu.adelaide.paxos.core.Learner;
import au.edu.adelaide.paxos.core.PaxosState;
import au.edu.adelaide.paxos.core.Proposer;
import au.edu.adelaide.paxos.core.Quorum;
import au.edu.adelaide.paxos.fault.FaultInjector;
import au.edu.adelaide.paxos.fault.FaultProfile;
import au.edu.adelaide.paxos.transport.Dispatcher;
import au.edu.adelaide.paxos.transport.PaxosMessage;
import au.edu.adelaide.paxos.transport.ProposalNumberIO;
import au.edu.adelaide.paxos.transport.TransportFacade;
import au.edu.adelaide.paxos.util.Logging;
import au.edu.adelaide.paxos.util.NetworkConfigLoader;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class CouncilMemberMain {

    public static void main(String[] args) throws Exception {
        Args a = parseArgs(args);
        if (a == null) {
            System.out.println("No args or --help");
            return;
        }
        final int selfId = a.id();
        final String profileName = a.profile();
        final Path cfgPath = Path.of(a.config());
        if (!Files.isRegularFile(cfgPath)) {
            System.err.println("config not found: " + cfgPath);
            return;
        }

        // --- Quorum & Roles
        var loader = NetworkConfigLoader.load(new FileInputStream(cfgPath.toFile()));
        int clusterSize = loader.getAllPeersExcept(selfId).size() + 1;
        Quorum quorum = new Quorum(clusterSize);

        // Learner
        Learner learner = new Learner(quorum);
        learner.addListener(val -> {
            Logging.consensus(selfId, val, "-");
        });

        // Acceptor
        PaxosState acceptor = new PaxosState();

        Dispatcher dispatcher = new Dispatcher(selfId, null, null, acceptor, learner);

        // TransportFacade: TCP server + inbound queue + business pool (calls dispatcher.onMessage)
        TransportFacade net = new TransportFacade(selfId, new FileInputStream(cfgPath.toFile()), dispatcher, /*businessThreads*/ 2) {
            @Override
            public void send(int to, PaxosMessage msg) {
                // TX log before send
                Logging.tx(msg, to, "profile=" + profileName);
                super.send(to, msg);
            }

            @Override
            public void broadcast(PaxosMessage msg) {
                Logging.tx(msg, null, "profile=" + profileName);
                super.broadcast(msg);
            }
        };
        Dispatcher loggingDispatcher = new Dispatcher(selfId, null, null, acceptor, learner) {
            @Override
            public void onMessage(PaxosMessage m) {
                Logging.rx(m, selfId, "profile=" + profileName);
                super.onMessage(m);
            }
        };
        net.close();
        TransportFacade net2 = new TransportFacade(selfId, new FileInputStream(cfgPath.toFile()), loggingDispatcher, 2);

        // Proposer
        Proposer proposer = new Proposer(selfId, quorum,
                n -> {
                    // broadcast prepare
                    PaxosMessage m = PaxosMessage.newMessage(PaxosMessage.Type.PREPARE)
                            .from(selfId).n(ProposalNumberIO.format(n)).corr(corr()).build();
                    net2.broadcast(m);
                },
                (n, v) -> {
                    // broadcast accept request
                    PaxosMessage m = PaxosMessage.newMessage(PaxosMessage.Type.ACCEPT_REQUEST)
                            .from(selfId).n(ProposalNumberIO.format(n)).v(v).corr(corr()).build();
                    net2.broadcast(m);
                }
        );
        Dispatcher fullDispatcher = new Dispatcher(selfId, null, proposer, acceptor, learner) {
            @Override
            public void onMessage(PaxosMessage m) {
                Logging.rx(m, selfId, "profile=" + profileName);
                super.onMessage(m);
            }
        };
        net2.close();
        TransportFacade transport = new TransportFacade(selfId, new FileInputStream(cfgPath.toFile()), fullDispatcher, 2);

        // --- Fault profile & injector
        FaultProfile profile = chooseProfile(profileName);
        FaultInjector injector = new FaultInjector(profile, reason -> {
            Logging.warn("CRASH profile hit: " + reason + " self=M" + selfId);
            transport.close();
        });
        fullDispatcher.setFaultInjector(injector);
        transport.setFaultInjector(injector);

        // --- start network
        transport.start();
        Logging.info("Member started: self=M" + selfId + " cluster=" + clusterSize + " profile=" + profileName + " config=" + cfgPath);

        // --- CLI
        AtomicBoolean running = new AtomicBoolean(true);
        ExecutorService cliPool = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "cli");
            t.setDaemon(true);
            return t;
        });
        cliPool.submit(new CliCommands(selfId, proposer, running));

        // --- shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                running.set(false);
                transport.close();
                cliPool.shutdownNow();
            } catch (Exception ignore) {
            }
            Logging.info("Member shutdown self=M" + selfId);
        }));

        // main thread blocks until quit
        while (running.get()) Thread.sleep(200);
    }


    private record Args(int id, String config, String profile) {
    }

    private static Args parseArgs(String[] args) {
        Integer id = null;
        String cfg = null;
        String profile = "standard";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--id" -> id = Integer.parseInt(args[++i]);
                case "--config" -> cfg = args[++i];
                case "--profile" -> profile = args[++i].toLowerCase(Locale.ROOT);
                case "--help", "-h" -> {
                    return null;
                }
                default -> {
                }
            }
        }
        if (id == null || cfg == null) return null;
        return new Args(id, cfg, profile);
    }

    private static FaultProfile chooseProfile(String name) {
        return switch (name) {
            case "reliable" -> FaultProfile.reliable();
            case "latent" -> FaultProfile.latent();
            case "failure" -> FaultProfile.failure();
            default -> FaultProfile.standard();
        };
    }

    private static String corr() {
        return UUID.randomUUID().toString();
    }

}
