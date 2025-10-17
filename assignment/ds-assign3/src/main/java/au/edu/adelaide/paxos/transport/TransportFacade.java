package au.edu.adelaide.paxos.transport;

import au.edu.adelaide.paxos.fault.FaultInjector;
import au.edu.adelaide.paxos.util.NetworkConfigLoader;

import java.io.InputStream;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * Facade for network transport: TCP server + client, message queue, dispatcher threads.
 */
public class TransportFacade implements AutoCloseable {

    private final int selfId;
    private final NetworkConfigLoader cfg;
    private final Dispatcher dispatcher;

    private final BlockingQueue<PaxosMessage> inbound = new LinkedBlockingQueue<>(8192);
    private final ExecutorService businessPool;
    private final TcpClient client = new TcpClient();

    private TcpServer server;

    public TransportFacade(int selfId, InputStream networkConfig,
                           Dispatcher dispatcher, int businessThreads) throws Exception {
        this.selfId = selfId;
        this.cfg = NetworkConfigLoader.load(networkConfig);
        this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher");
        this.businessPool = Executors.newFixedThreadPool(Math.max(1, businessThreads), r -> {
            Thread t = new Thread(r, "paxos-dispatcher");
            t.setDaemon(true);
            return t;
        });

        NetworkConfigLoader.Peer peer = cfg.getPeer(selfId);
        if (peer != null) {
            this.server = new TcpServer(peer.port, inbound);
        }
    }

    public void start() throws Exception {
        server.start();
        for (int i = 0; i < ((ThreadPoolExecutor) businessPool).getCorePoolSize(); i++) {
            businessPool.submit(this::drainLoop);
        }
    }

    private void drainLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                PaxosMessage m = inbound.take();
                try {
                    dispatcher.onMessage(m);
                } catch (Throwable biz) {
                    System.err.println("dispatcher error on " + m.type() + ": " + biz.getMessage());
                }
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void send(int to, PaxosMessage msg) {
        var peer = cfg.getPeer(to);
        if (peer == null) {
            System.err.println("unknown peer " + to);
            return;
        }
        // Let fault injector decide whether to send or drop
        if (!injector.beforeSendOrDispatch(msg, "send", to))
            return;
        // actually send
        client.sendPaxosMessage(peer.address(), msg);
    }

    public void broadcast(PaxosMessage msg) {
        Collection<NetworkConfigLoader.Peer> allPeersExcept = cfg.getAllPeersExcept(selfId);
        for (NetworkConfigLoader.Peer peer : allPeersExcept) {
            // Let fault injector decide whether to send or drop
            if (!injector.beforeSendOrDispatch(msg, "broadcast", peer.id))
                continue;
            // actually send
            client.sendPaxosMessage(peer.address(), msg);
        }
    }

    @Override
    public void close() {
        server.stop();
        businessPool.shutdownNow();
    }

    // Fault injector for testing purposes
    private volatile FaultInjector injector = FaultInjector.NOOP;

    public void setFaultInjector(FaultInjector injector) {
        this.injector = (injector == null ? FaultInjector.NOOP : injector);
    }

    protected String currentProfileTag() {
        return String.valueOf(injector);
    }
}
