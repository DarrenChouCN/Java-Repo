import au.edu.adelaide.paxos.transport.*;
import au.edu.adelaide.paxos.util.NetworkConfigLoader;
import au.edu.adelaide.paxos.util.PaxosMessageParser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the TCP messaging stack:
 * - NetworkConfigLoader parsing
 * - TcpServer (line-delimited JSON -> enqueue)
 * - TcpClient (send one JSON line)
 * - TransportFacade (send/broadcast -> Dispatcher.onMessage)
 */
public class TcpStackTest {

    // --------- tiny helper dispatcher that only records received messages ---------
    // NOTE: If your Dispatcher class is 'final', remove 'final' or turn this into
    //       a standalone class in test sources (same package) extending Dispatcher.
    static class RecordingDispatcher extends Dispatcher {
        final BlockingQueue<PaxosMessage> inbox = new LinkedBlockingQueue<>();

        public RecordingDispatcher(int memberId) {
            // transport/proposer/acceptor/learner are not used by this recorder
            super(memberId, null, null, null, null);
        }

        @Override
        public void onMessage(PaxosMessage m) {
            if (m != null) inbox.offer(m);
        }
    }

    // Close resources if a test leaves something running
    @AfterEach
    void afterEach() throws Exception {
        // no-op: TransportFacade is AutoCloseable and closed in tests
    }

    // -------------------- Test 1: NetworkConfigLoader parses config --------------------

    @Test
    void network_config_parsing_with_comments_and_blanks() throws Exception {
        String cfg = """
                # memberId host port
                1 127.0.0.1 7011
                2 127.0.0.1 7012
                
                // comment
                3 127.0.0.1 7013
                """;
        try (InputStream in = new ByteArrayInputStream(cfg.getBytes(StandardCharsets.UTF_8))) {
            NetworkConfigLoader loader = NetworkConfigLoader.load(in);
            assertNotNull(loader.getPeer(1));
            assertEquals(7012, loader.getPeer(2).port);
            // 'except self' should filter out given id
            assertEquals(2, loader.getAllPeersExcept(1).size());
        }
    }

    // -------------------- Test 2: TcpServer + TcpClient line-delimited JSON --------------------

    @Test
    void tcp_server_enqueues_json_line_from_client() throws Exception {
        int freePort = pickFreePort();
        BlockingQueue<PaxosMessage> queue = new LinkedBlockingQueue<>();
        TcpServer server = new TcpServer(freePort, queue);
        server.start();

        try {
            // build a PREPARE message and send one JSON line
            PaxosMessage msg = PaxosMessage.newMessage(PREPARE)
                    .from(10).n("5.1").corr("c-prep").build();
            String json = PaxosMessageParser.toJson(msg) + "\n";

            TcpClient client = new TcpClient();
            client.send(new InetSocketAddress("127.0.0.1", freePort),
                    PaxosMessage.newMessage(PREPARE).from(10).n("5.1").corr("c-prep").build());

            // expect the server reader to enqueue exactly that message
            PaxosMessage got = queue.poll(Duration.ofSeconds(2).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
            assertNotNull(got, "server should enqueue the received message");
            assertEquals(PREPARE, got.type());
            assertEquals(10, got.from());
            assertEquals("5.1", got.n());
        } finally {
            server.stop();
        }
    }

    // -------------------- Test 3: TransportFacade send() unicast -> target dispatcher only --------------------

    @Test
    void transport_facade_unicast_to_specific_peer() throws Exception {
        // dynamic free ports to avoid conflicts
        int p1 = pickFreePort();
        int p2 = pickFreePort();

        String cfg = "1 127.0.0.1 " + p1 + "\n" +
                "2 127.0.0.1 " + p2 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes()), d1, 1);
             TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes()), d2, 1)) {

            net1.start();
            net2.start();

            // send PROMISE to #2
            PaxosMessage promise = PaxosMessage.newMessage(PROMISE)
                    .from(1).to(2).n("9.1").promised(true).acceptedN("7.2").acceptedV("H").build();
            net1.send(2, promise);

            PaxosMessage got1 = d1.inbox.poll(300, java.util.concurrent.TimeUnit.MILLISECONDS);
            PaxosMessage got2 = d2.inbox.poll(2000, java.util.concurrent.TimeUnit.MILLISECONDS);

            // #1 should not receive its own unicast
            assertNull(got1, "sender should not receive its own unicast");
            // #2 should receive the message intact
            assertNotNull(got2);
            assertEquals(PROMISE, got2.type());
            assertEquals("9.1", got2.n());
            assertEquals(Boolean.TRUE, got2.promised());
            assertEquals("7.2", got2.acceptedN());
            assertEquals("H", got2.acceptedV());
        }
    }

    // -------------------- Test 4: TransportFacade broadcast() -> all except self --------------------

    @Test
    void transport_facade_broadcast_to_all_except_self() throws Exception {
        int p1 = pickFreePort(), p2 = pickFreePort(), p3 = pickFreePort();
        String cfg = "1 127.0.0.1 " + p1 + "\n" +
                "2 127.0.0.1 " + p2 + "\n" +
                "3 127.0.0.1 " + p3 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);
        RecordingDispatcher d3 = new RecordingDispatcher(3);

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes()), d1, 2);
             TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes()), d2, 2);
             TransportFacade net3 = new TransportFacade(3, new ByteArrayInputStream(cfg.getBytes()), d3, 2)) {

            net1.start();
            net2.start();
            net3.start();

            // broadcast ACCEPTED from #1 (self should not receive it)
            PaxosMessage accepted = PaxosMessage.newMessage(ACCEPTED)
                    .from(1).n("11.1").v("V").build();
            net1.broadcast(accepted);

            PaxosMessage g1 = d1.inbox.poll(300, java.util.concurrent.TimeUnit.MILLISECONDS);
            PaxosMessage g2 = d2.inbox.poll(2000, java.util.concurrent.TimeUnit.MILLISECONDS);
            PaxosMessage g3 = d3.inbox.poll(2000, java.util.concurrent.TimeUnit.MILLISECONDS);

            assertNull(g1, "self should not receive its own broadcast");
            assertNotNull(g2, "peer #2 should receive broadcast");
            assertNotNull(g3, "peer #3 should receive broadcast");
            assertEquals(ACCEPTED, g2.type());
            assertEquals("11.1", g2.n());
            assertEquals("V", g2.v());
            assertEquals(ACCEPTED, g3.type());
            assertEquals("11.1", g3.n());
            assertEquals("V", g3.v());
        }
    }

    // -------------------- helpers --------------------

    // Allocate an ephemeral free port for testing.
    private static int pickFreePort() throws Exception {
        try (ServerSocket ss = new ServerSocket(0)) {
            ss.setReuseAddress(true);
            return ss.getLocalPort();
        }
    }
}
