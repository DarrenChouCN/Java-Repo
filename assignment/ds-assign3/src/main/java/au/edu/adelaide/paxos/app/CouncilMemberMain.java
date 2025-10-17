package au.edu.adelaide.paxos.app;

import au.edu.adelaide.paxos.core.Learner;
import au.edu.adelaide.paxos.core.PaxosState;
import au.edu.adelaide.paxos.core.Proposer;
import au.edu.adelaide.paxos.core.Quorum;
import au.edu.adelaide.paxos.transport.*;
import au.edu.adelaide.paxos.util.Logging;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class CouncilMemberMain {

    public static void main(String[] args) throws Exception {
        int selfId = -1;
        String cfgPath = "./network.config";
        String profileName = "reliable";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--id":
                    selfId = Integer.parseInt(args[++i]);
                    break;
                case "--config":
                    cfgPath = args[++i];
                    break;
                case "--profile":
                    profileName = args[++i];
                    break;
                case "--help":
                default:
                    if ("--help".equals(args[i])) {
                        System.out.println("usage: --id <int> --config <path> [--profile ...]");
                        return;
                    }
            }
        }
        if (selfId <= 0) {
            System.err.println("missing --id");
            return;
        }
        File configFile = new File(cfgPath);
        if (!configFile.isFile()) {
            System.err.println("config not found: " + configFile.getAbsolutePath());
            return;
        }

        Map<Integer, InetSocketAddress> peers = loadPeers(configFile);
        if (!peers.containsKey(selfId)) {
            System.err.println("selfId " + selfId + " not in config");
            return;
        }
        int clusterSize = peers.size();

        Quorum quorum = new Quorum(clusterSize);
        Learner learner = new Learner(quorum);
        PaxosState acceptor = new PaxosState();

        AtomicReference<TransportFacade> transportRef = new AtomicReference<>();

        int finalSelfId = selfId;
        Proposer proposer = new Proposer(selfId, quorum,
                /* PrepareBroadcaster */ n -> {
            PaxosMessage prepare = PaxosMessage.newMessage(PaxosMessage.Type.PREPARE).from(finalSelfId).n(ProposalNumberIO.format(n)).corr(java.util.UUID.randomUUID().toString()).build();
            TransportFacade t = transportRef.get();
            if (t != null) t.broadcast(prepare);
            else Logging.warn("prepare dropped: transport not ready");
        },
                /* AcceptBroadcaster */ (n, v) -> {
            PaxosMessage ar = PaxosMessage.newMessage(PaxosMessage.Type.ACCEPT_REQUEST).from(finalSelfId).n(ProposalNumberIO.format(n)).v(v).corr(java.util.UUID.randomUUID().toString()).build();
            TransportFacade t = transportRef.get();
            if (t != null) t.broadcast(ar);
            else Logging.warn("accept-request dropped: transport not ready");
        });

        Dispatcher dispatcher = new Dispatcher(selfId, proposer, acceptor, learner);

        learner.addListener(value -> System.out.println("CONSENSUS: value=" + value));

        TransportFacade transport = new TransportFacade(selfId, new FileInputStream(configFile), dispatcher, 2) {
            @Override
            public void send(int to, PaxosMessage msg) {
                Logging.tx(msg, to, "from=" + finalSelfId + ",type=" + msg.getClass().getSimpleName() + ",thr=" + Thread.currentThread().getName());
                super.send(to, msg);
            }

            @Override
            public void broadcast(PaxosMessage msg) {
                Logging.tx(msg, null, "from=" + finalSelfId + ",type=" + msg.getClass().getSimpleName() + ",thr=" + Thread.currentThread().getName());
                super.broadcast(msg);
            }
        };

        transportRef.set(transport);

        dispatcher.setSender(new TransportSender() {
            @Override
            public void send(int to, PaxosMessage msg) {
                transport.send(to, msg);
            }

            @Override
            public void broadcast(PaxosMessage msg) {
                transport.broadcast(msg);
            }
        });

        // FaultInjector injector = new FaultInjector(FaultProfile.of(profileName), r -> { Logging.warn("CRASH: " + r); transport.close(); });
        // dispatcher.setFaultInjector(injector);
        // transport.setFaultInjector(injector);
        transport.start();

        Logging.info("Member started: self=M" + selfId + " cluster=" + clusterSize + " profile=" + profileName + " config=" + configFile.getPath());

        AtomicBoolean running = new AtomicBoolean(true);
        ExecutorService cliPool = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "cli");
            t.setDaemon(true);
            return t;
        });
        cliPool.submit(new CliCommands(selfId, proposer, running));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                running.set(false);
            } catch (Throwable ignore) {
            }
            try {
                cliPool.shutdownNow();
            } catch (Throwable ignore) {
            }
            try {
                transport.close();
            } catch (Throwable ignore) {
            }
        }));

        while (running.get()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
                break;
            }
        }
        transport.close();
    }

    private static Map<Integer, InetSocketAddress> loadPeers(File f) throws IOException {
        Map<Integer, InetSocketAddress> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] t = line.split("\\s+");
                if (t.length < 3) continue;
                int id = Integer.parseInt(t[0]);
                String host = t[1];
                int port = Integer.parseInt(t[2]);
                map.put(id, new InetSocketAddress(host, port));
            }
        }
        return map;
    }
}
