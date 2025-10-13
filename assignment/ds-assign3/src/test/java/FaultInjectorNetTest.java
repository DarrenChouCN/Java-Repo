import au.edu.adelaide.paxos.fault.FaultInjector;
import au.edu.adelaide.paxos.fault.FaultProfile;
import au.edu.adelaide.paxos.transport.Dispatcher;
import au.edu.adelaide.paxos.transport.PaxosMessage;
import au.edu.adelaide.paxos.transport.TransportFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Fault injection tests for the TCP transport + dispatcher boundary.
 * We do NOT involve Paxos roles; the dispatcher only records inbound messages.
 */
public class FaultInjectorNetTest {

    /**
     * Minimal dispatcher that only records messages (no business logic).
     */
    static class RecordingDispatcher extends Dispatcher {
        final BlockingQueue<PaxosMessage> inbox = new LinkedBlockingQueue<>();

        public RecordingDispatcher(int memberId) {
            super(memberId, null, null, null, null);
        }

        @Override
        public void onMessage(PaxosMessage m) {
            // Let fault injector (if set) run inside super.onMessage(...)
            // but we still want to record receipt even if this instance has no roles.
            // So we record first, then call super (which will ignore unknown types/roles).
            super.onMessage(m);
            if (m != null) inbox.offer(m);
        }
    }

    @AfterEach
    void tearDown() {
        // nothing to clean globally; each test closes its own TransportFacade
    }

    // ---------------------- DROP: send-side dropRate=1.0 => peer never receives ----------------------

    @Test
    void drop_on_send_discards_message() throws Exception {
        int p1 = freePort(), p2 = freePort();
        String cfg = "1 127.0.0.1 " + p1 + "\n" + "2 127.0.0.1 " + p2 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d1, 1); TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d2, 1)) {

            net1.start();
            net2.start();

            // send-side injector: always drop
            FaultProfile dropAll = new FaultProfile.Builder().drop(1.0).build();
            net1.setFaultInjector(new FaultInjector(dropAll, reason -> {
            }));

            PaxosMessage m = PaxosMessage.newMessage(PREPARE).from(1).to(2).n("1.1").build();
            net1.send(2, m);

            // peer should never receive it
            PaxosMessage got = d2.inbox.poll(300, TimeUnit.MILLISECONDS);
            assertNull(got, "peer must not receive when send-side dropRate=1.0");
        }
    }

    // ---------------------- DELAY: send-side delay >= min ----------------------

    @Test
    void delay_on_send_adds_latency() throws Exception {
        int p1 = freePort(), p2 = freePort();
        String cfg = "1 127.0.0.1 " + p1 + "\n" + "2 127.0.0.1 " + p2 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d1, 1); TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d2, 1)) {

            net1.start();
            net2.start();

            // send-side delay window (keep it modest to reduce flakiness)
            FaultProfile slow = new FaultProfile.Builder().delay(60, 80).build();
            net1.setFaultInjector(new FaultInjector(slow, reason -> {
            }));

            PaxosMessage m = PaxosMessage.newMessage(PROMISE).from(1).to(2).n("2.1").promised(true).build();

            long t0 = System.nanoTime();
            net1.send(2, m);
            PaxosMessage got = d2.inbox.poll(2, TimeUnit.SECONDS);
            long dtMs = Duration.ofNanos(System.nanoTime() - t0).toMillis();

            assertNotNull(got, "peer should still receive after delay");
            // assert lower bound to avoid platform variance; allow some margin
            assertTrue(dtMs >= 50, "elapsed " + dtMs + "ms should be >= 50ms (delay injected)");
        }
    }

    @Test
    void sparse_on_send_allows_every_second_unicast() throws Exception {
        int p1 = freePort(), p2 = freePort();
        String cfg = "1 127.0.0.1 " + p1 + "\n" +
                "2 127.0.0.1 " + p2 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d1, 1);
             TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d2, 1)) {

            net1.start();
            net2.start();

            // send-side sparse: ACCEPTED passes 1 of every 2
            FaultProfile.Sparse gate = new FaultProfile.Sparse().gate(ACCEPTED, 2);
            FaultProfile sparseTx = new FaultProfile.Builder().sparse(gate).build();
            net1.setFaultInjector(new FaultInjector(sparseTx, reason -> {
            }));

            // unicast 10 ACCEPTED to peer #2
            for (int i = 0; i < 10; i++) {
                PaxosMessage acc = PaxosMessage.newMessage(PaxosMessage.Type.ACCEPTED)
                        .from(1).to(2).n("3.1").v("V").build();
                net1.send(2, acc);
            }

            // expect roughly half to arrive (allow small variance)
            int received = drainCount(d2.inbox, 2000);
            assertTrue(received >= 4 && received <= 6, "received=" + received + " (expected ~5 of 10)");
        }
    }

    // ---------------------- CRASH-ONCE: trigger on first PREPARE, subsequent messages pass ----------------------

    @Test
    void crash_once_triggers_once_then_allows_following_messages() throws Exception {
        int p1 = freePort(), p2 = freePort();
        String cfg = "1 127.0.0.1 " + p1 + "\n" + "2 127.0.0.1 " + p2 + "\n";

        RecordingDispatcher d1 = new RecordingDispatcher(1);
        RecordingDispatcher d2 = new RecordingDispatcher(2);

        AtomicBoolean crashed = new AtomicBoolean(false);
        FaultProfile crashProfile = new FaultProfile.Builder().crashOnce(PREPARE)                 // crash on first PREPARE seen at the chosen side
                .build();

        try (TransportFacade net1 = new TransportFacade(1, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d1, 1); TransportFacade net2 = new TransportFacade(2, new ByteArrayInputStream(cfg.getBytes(UTF_8)), d2, 1)) {

            net1.start();
            net2.start();

            // choose receive-side crash: dispatcher will call CrashHandler once, and drop that message
            FaultInjector rxCrash = new FaultInjector(crashProfile, reason -> crashed.set(true));
            net1.setFaultInjector(rxCrash);

            // First PREPARE should trigger crash handler and be dropped
            PaxosMessage prep = PaxosMessage.newMessage(PREPARE).from(1).to(2).n("7.1").build();
            net1.send(2, prep);

            PaxosMessage got1 = d2.inbox.poll(300, TimeUnit.MILLISECONDS);
            assertNull(got1, "first PREPARE should be dropped (crash-once)");
            assertTrue(crashed.get(), "crash handler must be invoked exactly once");

            // A following PROMISE should pass through (crash is one-shot)
            PaxosMessage promise = PaxosMessage.newMessage(PROMISE).from(1).to(2).n("7.1").promised(true).build();
            net1.send(2, promise);
            PaxosMessage got2 = d2.inbox.poll(2000, TimeUnit.MILLISECONDS);
            assertNotNull(got2, "subsequent message should pass after one-shot crash");
            assertEquals(PROMISE, got2.type());
        }
    }

    // ---------------------- helpers ----------------------

    private static int drainCount(BlockingQueue<PaxosMessage> q, long millis) throws InterruptedException {
        long deadline = System.currentTimeMillis() + millis;
        int c = 0;
        PaxosMessage m;
        while ((m = q.poll(50, TimeUnit.MILLISECONDS)) != null || System.currentTimeMillis() < deadline) {
            if (m != null) c++;
        }
        return c;
    }

    /**
     * Allocate an ephemeral free TCP port for test.
     */
    private static int freePort() throws Exception {
        try (ServerSocket ss = new ServerSocket(0)) {
            ss.setReuseAddress(true);
            return ss.getLocalPort();
        }
    }
}
