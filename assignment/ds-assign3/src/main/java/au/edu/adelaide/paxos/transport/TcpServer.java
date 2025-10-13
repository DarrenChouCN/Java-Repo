package au.edu.adelaide.paxos.transport;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TcpServer {

    private final int listenPort;
    private final BlockingQueue<PaxosMessage> inbound;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ExecutorService connReaders = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r, "tcp-conn-reader");
        t.setDaemon(true);
        return t;
    });
    private ServerSocket serverSocket;

    public TcpServer(int listenPort, BlockingQueue<PaxosMessage> inbound) {
        this.listenPort = listenPort;
        this.inbound = Objects.requireNonNull(inbound, "inbound");
    }

    public void start() throws Exception {
        if (!running.compareAndSet(false, true)) return;
        serverSocket = new ServerSocket(listenPort);
        Thread acceptLoop = new Thread(this::acceptLoop, "tcp-accept-" + listenPort);
        acceptLoop.setDaemon(true);
        acceptLoop.start();
        System.out.println("listening on port " + listenPort);
    }

    private void acceptLoop() {
        try {
            while (running.get()) {
                Socket s = serverSocket.accept();
                connReaders.submit(() -> readLoop(s));
            }
        } catch (Exception e) {
            if (running.get()) System.err.println("accept error: " + e.getMessage());
        }
    }

    private void readLoop(Socket s) {
        String remote = s.getRemoteSocketAddress().toString();
        try (s;
             var br = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                PaxosMessage m = PaxosMessageParser.fromJson(line);
                if (m != null) {
                    inbound.put(m);
                    System.out.println("enqueued from " + remote + " : " + m.type() + " from=" + m.from() + " to=" + m.to() + " n=" + m.n());
                }
            }
        } catch (Exception e) {
            System.err.println("connection " + remote + " closed/error: " + e.getMessage());
        }
    }

    public void stop() {
        if (!running.compareAndSet(true, false)) return;
        try {
            serverSocket.close();
        } catch (Exception ignore) {
        }
        connReaders.shutdownNow();
        System.out.println("stopped port " + listenPort);
    }
}
