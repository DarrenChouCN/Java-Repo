import au.edu.adelaide.paxos.core.*;
import au.edu.adelaide.paxos.transport.Dispatcher;
import au.edu.adelaide.paxos.transport.InMemoryTransport;
import au.edu.adelaide.paxos.transport.PaxosMessage;
import au.edu.adelaide.paxos.transport.PaxosMessageParser;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.PREPARE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * End-to-end tests for the messaging layer:
 * PaxosMessage, PaxosMessageParser, InMemoryTransport, Dispatcher
 * wired with existing Proposer / PaxosState / Learner (no custom subclasses).
 */
public class MessagingLayerTest {

    /**
     * Phase1 end-to-end over the messaging layer:
     * Proposer broadcasts PREPARE -> two PaxosState acceptors reply PROMISE (unicast) ->
     * Proposer collects majority and marks phase-1 done.
     */
    @Test
    void phase1_prepare_promise_majority() {
        Quorum quorum = new Quorum(3);
        InMemoryTransport bus = new InMemoryTransport();

        // node1: proposer (+ learner可有可无)
        AtomicReference<Dispatcher> d1ref = new AtomicReference<>();
        Proposer proposer1 = new Proposer(
                1, quorum,
                n -> d1ref.get().broadcastPrepare(n, "c-prep"),
                (n, v) -> d1ref.get().broadcastAcceptRequest(n, v, "c-acc")
        );
        Learner learner1 = new Learner(quorum); // 这里不参与断言，只是占位

        Dispatcher d1 = new Dispatcher(1, bus, proposer1, null, learner1);
        d1ref.set(d1);

        // node2 & node3: acceptors (PaxosState 有无参构造)
        PaxosState acc2 = new PaxosState();
        PaxosState acc3 = new PaxosState();
        Dispatcher d2 = new Dispatcher(2, bus, null, acc2, null);
        Dispatcher d3 = new Dispatcher(3, bus, null, acc3, null);

        // register all
        bus.register(1, d1);
        bus.register(2, d2);
        bus.register(3, d3);

        // trigger phase1
        ProposalNumber n = proposer1.onClientPropose("X"); // 会通过 broadcaster 调用 d1.broadcastPrepare

        assertTrue(proposer1.promisedCount() >= quorum.majorityCount(),
                "proposer should receive a majority of PROMISEs");
        assertTrue(proposer1.phase1Done(), "phase-1 must be done after majority");
        assertEquals(n, proposer1.currentN());
    }

    /**
     * Full E2E: PREPARE/PROMISE -> ACCEPT_REQUEST/ACCEPTED -> Learner decides value.
     * Uses existing PaxosState.onAcceptRequest and Learner APIs.
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
        Learner learner1 = new Learner(quorum); // Learner(Quorum) 构造  :contentReference[oaicite:3]{index=3}
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

        // Learner 应该在收到多数 ACCEPTED 后置位
        assertTrue(learner1.isDecided(), "learner should have decided after majority ACCEPTED");
        assertEquals(clientValue, learner1.decidedValue(), "decided value should match client value");
    }

    /**
     * Sanity: PaxosMessage <-> JSON via PaxosMessageParser keeps core fields.
     * （与上层装配无关，确保 Parser 自身正常）
     */
    @Test
    void paxos_message_json_roundtrip() {
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
