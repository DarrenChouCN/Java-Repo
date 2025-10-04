import au.edu.adelaide.paxos.core.ProposalNumber;
import au.edu.adelaide.paxos.core.Proposer;
import au.edu.adelaide.paxos.core.Quorum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProposerTest {

    /**
     * no-op prepare broadcaster
     */
    static final class NoopPrepare implements Proposer.PrepareBroadcaster {
        @Override
        public void broadcastPrepare(ProposalNumber n) { /* no-op */ }
    }

    /**
     * capture accept requests
     */
    static final class CapturingAccept implements Proposer.AcceptBroadcaster {
        int calls = 0;
        ProposalNumber lastN;
        String lastV;

        @Override
        public void broadcastAcceptRequest(ProposalNumber n, String v) {
            calls++;
            lastN = n;
            lastV = v;
        }
    }

    @Test
    void majority_noPrior_usesClientValue_and_sendsPhase2() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(1, quorum, new NoopPrepare(), acc);

        // start proposal
        ProposalNumber n = p.onClientPropose("X");     // returns the proposal number

        // 3 distinct promises, no prior accepted
        p.onPromise(1, n, true, null, null);
        p.onPromise(2, n, true, null, null);
        p.onPromise(3, n, true, null, null);

        // assertions
        assertTrue(p.phase1Done(), "phase-1 must finish");
        assertEquals("X", p.chosenValueForAccept(), "no prior -> keep client value");
        assertEquals(3, p.promisedCount(), "unique member count");
        assertTrue(p.phase2Sent(), "phase-2 must be sent");
        assertEquals(1, acc.calls, "accept broadcast once");
        assertEquals(n, acc.lastN, "accept uses same proposal number");
        assertEquals("X", acc.lastV, "accept uses chosen value");
    }

    @Test
    void inherit_highestLastAccepted_value_and_sendsPhase2() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(2, quorum, new NoopPrepare(), acc);

        ProposalNumber n = p.onClientPropose("CLIENT");

        // three promises; highest lastAcceptedN carries "B"
        p.onPromise(1, n, true, new ProposalNumber(10, 7), "A");
        p.onPromise(2, n, true, new ProposalNumber(12, 1), "B");  // highest
        p.onPromise(3, n, true, null, null);

        assertTrue(p.phase1Done(), "phase-1 must finish");
        assertEquals("B", p.chosenValueForAccept(), "must inherit highest lastAccepted");
        assertEquals(3, p.promisedCount());
        assertTrue(p.phase2Sent(), "phase-2 must be sent");
        assertEquals(1, acc.calls);
        assertEquals(n, acc.lastN);
        assertEquals("B", acc.lastV);
    }

    @Test
    void ignore_mismatchedRound_and_duplicate_then_notEnoughForMajority_noPhase2() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(3, quorum, new NoopPrepare(), acc);

        ProposalNumber n = p.onClientPropose("Y");

        // mismatched round -> ignored
        p.onPromise(9, new ProposalNumber(999, 999), true, null, null);

        // valid promise from member 1
        p.onPromise(1, n, true, null, null);

        // duplicate from the same member -> ignored
        p.onPromise(1, n, true, null, null);

        // another valid promise from member 2
        p.onPromise(2, n, true, null, null);

        // only 2 valid unique promises -> not majority
        assertEquals(2, p.promisedCount(), "dup + mismatched must be ignored");
        assertFalse(p.phase1Done(), "not enough for majority");
        assertNull(p.chosenValueForAccept(), "no chosen value without majority");
        assertFalse(p.phase2Sent(), "phase-2 should NOT be sent");
        assertEquals(0, acc.calls, "no accept broadcast");
    }

    @Test
    void doNotResendAccept_afterPhase2Sent_evenIfMorePromisesArrive() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(10, quorum, new NoopPrepare(), acc);

        ProposalNumber n = p.onClientPropose("Z");

        // reach majority -> send accept once
        p.onPromise(1, n, true, null, null);
        p.onPromise(2, n, true, null, null);
        p.onPromise(3, n, true, null, null);

        assertTrue(p.phase2Sent());
        assertEquals(1, acc.calls);

        // more valid promises + duplicates -> should NOT trigger another accept
        p.onPromise(3, n, true, null, null);                 // duplicate
        p.onPromise(4, n, true, null, null);
        p.onPromise(5, n, true, null, null);

        assertEquals(1, acc.calls, "must broadcast accept only once");
        assertEquals("Z", acc.lastV);
        assertEquals(n, acc.lastN);
    }

    @Test
    void startNewRound_onNewClientPropose_resetsState_andUsesNewN() {
        Quorum quorum = new Quorum(5);
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(11, quorum, new NoopPrepare(), acc);

        // round 1 -> finish and send accept
        ProposalNumber n1 = p.onClientPropose("R1");
        p.onPromise(1, n1, true, null, null);
        p.onPromise(2, n1, true, null, null);
        p.onPromise(3, n1, true, null, null);
        assertTrue(p.phase2Sent());
        assertEquals(1, acc.calls);

        // start a new round -> state should reset and proposal number should change
        ProposalNumber n2 = p.onClientPropose("R2");
        assertNotEquals(n1, n2, "new round must have a new proposal number");
        assertEquals(0, p.promisedCount(), "state must reset");
        assertFalse(p.phase1Done());
        assertFalse(p.phase2Sent());

        // reach majority again for the new round
        p.onPromise(1, n2, true, null, null);
        p.onPromise(2, n2, true, null, null);
        p.onPromise(3, n2, true, null, null);

        assertTrue(p.phase2Sent());
        assertEquals(2, acc.calls, "second round should send another accept");
        assertEquals(n2, acc.lastN);
        assertEquals("R2", acc.lastV);
    }

    @Test
    void rejectionsDoNotCount_untilMajorityPromisesThenSendAccept() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(12, quorum, new NoopPrepare(), acc);

        ProposalNumber n = p.onClientPropose("OK");

        // two rejections -> should not count
        p.onPromise(8, n, false, null, null);
        p.onPromise(9, n, false, null, null);
        assertEquals(0, p.promisedCount());
        assertFalse(p.phase1Done());
        assertFalse(p.phase2Sent());

        // three real promises -> reach majority
        p.onPromise(1, n, true, null, null);
        p.onPromise(2, n, true, null, null);
        p.onPromise(3, n, true, null, null);

        assertEquals(3, p.promisedCount(), "only true promises count");
        assertTrue(p.phase1Done());
        assertTrue(p.phase2Sent(), "accept must be sent after majority");
        assertEquals(1, acc.calls);
        assertEquals("OK", acc.lastV);
        assertEquals(n, acc.lastN);
    }

}
