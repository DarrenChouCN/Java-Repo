import au.edu.adelaide.paxos.core.*;
import au.edu.adelaide.paxos.transport.Dispatcher;
import au.edu.adelaide.paxos.transport.InMemoryTransport;
import au.edu.adelaide.paxos.transport.PaxosMessage;
import au.edu.adelaide.paxos.util.PaxosMessageParser;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.PREPARE;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests for the messaging layer (Dispatcher + Transport).
 * These are more like integration tests, as they test multiple components working together.
 */
public class MessagingLayerTest {


    /**
     * Test that a proposer can successfully complete phase 1 (prepare/promise)
     * by receiving a majority of PROMISE responses from acceptors.
     */
    @Test
    void phase1_prepare_promise_majority() {
        Quorum quorum = new Quorum(3);
        InMemoryTransport bus = new InMemoryTransport();

        AtomicReference<Dispatcher> d1ref = new AtomicReference<>();
        Proposer proposer1 = new Proposer(
                1, quorum,
                n -> d1ref.get().broadcastPrepare(n, "c-prep"),
                (n, v) -> d1ref.get().broadcastAcceptRequest(n, v, "c-acc")
        );
        Learner learner1 = new Learner(quorum);

        Dispatcher d1 = new Dispatcher(1, bus, proposer1, null, learner1);
        d1ref.set(d1);

        PaxosState acc2 = new PaxosState();
        PaxosState acc3 = new PaxosState();
        Dispatcher d2 = new Dispatcher(2, bus, null, acc2, null);
        Dispatcher d3 = new Dispatcher(3, bus, null, acc3, null);

        // register all
        bus.register(1, d1);
        bus.register(2, d2);
        bus.register(3, d3);

        // trigger phase1
        ProposalNumber n = proposer1.onClientPropose("X");

        assertTrue(proposer1.promisedCount() >= quorum.majorityCount(),
                "proposer should receive a majority of PROMISES");
        assertTrue(proposer1.phase1Done(), "phase-1 must be done after majority");
        assertEquals(n, proposer1.currentN());
    }


    /**
     * Full flow test: proposer + 2 acceptors + learner.
     * Proposer should be able to complete phase 1 and phase 2, and learner should reach a decision.
     */
    @Test
    void full_flow_reaches_learner_decision() {
        Quorum quorum = new Quorum(3);
        InMemoryTransport bus = new InMemoryTransport();

        // node1: proposer + learner
        AtomicReference<Dispatcher> d1ref = new AtomicReference<>();
        Proposer proposer1 = new Proposer(
                1, quorum,
                n -> d1ref.get().broadcastPrepare(n, "prep-2"),
                (n, v) -> d1ref.get().broadcastAcceptRequest(n, v, "acc-2")
        );
        Learner learner1 = new Learner(quorum);
        Dispatcher d1 = new Dispatcher(1, bus, proposer1, null, learner1);
        d1ref.set(d1);

        // node2 & node3: acceptors
        Dispatcher d2 = new Dispatcher(2, bus, null, new PaxosState(), null);
        Dispatcher d3 = new Dispatcher(3, bus, null, new PaxosState(), null);

        bus.register(1, d1);
        bus.register(2, d2);
        bus.register(3, d3);

        // start proposal; proposer will broadcast PREPARE, gather PROMISE majority, then broadcast ACCEPT_REQUEST
        String clientValue = "VALUE-X";
        proposer1.onClientPropose(clientValue);

        assertTrue(learner1.isDecided(), "learner should have decided after majority ACCEPTED");
        assertEquals(clientValue, learner1.decidedValue(), "decided value should match client value");
    }


    /**
     * Test JSON serialization and deserialization of PaxosMessage.
     */
    @Test
    void paxos_message_json_round_trip() {
        PaxosMessage m = PaxosMessage.newMessage(PREPARE)
                .from(10).n("7.3").corr("c-xyz").build();

        String json = PaxosMessageParser.toJson(m);
        PaxosMessage copy = PaxosMessageParser.fromJson(json);

        assertNotNull(copy);
        assertEquals(PREPARE, copy.type());
        assertEquals(10, copy.from());
        assertEquals("7.3", copy.n());
        assertEquals("c-xyz", copy.corr());
    }
}
