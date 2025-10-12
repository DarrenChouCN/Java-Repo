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

    // --- helper: capture prepare broadcasts (like CapturingAccept for phase-2) ---
    static final class CapturingPrepare implements Proposer.PrepareBroadcaster {
        int calls = 0;
        ProposalNumber lastN;

        @Override
        public void broadcastPrepare(ProposalNumber n) {
            calls++;
            lastN = n;
        }
    }

    // --- helper: deterministic backoff for assertions (delay = retryIndex) ---
    static final class DeterministicBackoff implements Proposer.BackoffPolicy {
        @Override
        public long delayMillis(long retryIndex) {
            return retryIndex; // 0 on first check, 1 after first retryOnce(), etc.
        }
    }

    /**
     * When a rejection is observed, proposer should expose shouldRetry()=true,
     * and retryOnce() must bump proposal number, reset P1 state, and re-send PREPARE.
     */
    @Test
    void rejection_triggers_retry_bumpsN_resetsP1_and_resendsPrepare() {
        Quorum quorum = new Quorum(5);
        CapturingPrepare prep = new CapturingPrepare();
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(21, quorum, prep, acc);
        p.setBackoffPolicy(new DeterministicBackoff());

        // start a proposal -> one prepare broadcast
        ProposalNumber n1 = p.onClientPropose("C1");
        assertEquals(1, prep.calls, "onClientPropose must broadcast PREPARE once");

        // observe a rejection => do not count as promise, but mark for retry
        p.onPromise(4, n1, false, null, null);
        assertEquals(0, p.promisedCount(), "rejections do not increment promised count");
        assertFalse(p.phase1Done(), "phase-1 not done after rejection");
        assertTrue(p.shouldRetry(), "shouldRetry must be true after a rejection");

        // before retry: retryIndex==0 -> currentBackoffMillis==0 (deterministic)
        assertEquals(0L, p.currentBackoffMillis(), "first backoff check uses retryIndex=0");

        // perform a single retry -> new n, P1 state cleared, prepare broadcast again
        ProposalNumber n2 = p.retryOnce();
        assertNotEquals(n1, n2, "retry must generate a strictly larger proposal number");
        assertEquals(2, prep.calls, "retryOnce must broadcast PREPARE again");
        assertEquals(n2, prep.lastN, "last prepare must use the new proposal number");
        assertEquals(0, p.promisedCount(), "promised set must be cleared for new round");
        assertNull(p.chosenValueForAccept(), "no chosen value before reaching majority");
        assertFalse(p.phase1Done(), "phase-1 still not done after just retrying");

        // after first retry: retryIndex==1 -> deterministic backoff reflects that
        assertEquals(1L, p.currentBackoffMillis(), "backoff reflects retryIndex after retryOnce()");
    }

    /**
     * After a retry, if a majority of promises is gathered that includes a highest
     * lastAccepted value, the proposer must inherit that value and send ACCEPT once.
     */
    @Test
    void after_retry_majority_inherits_highestAccepted_and_sendsAccept_once() {
        Quorum quorum = new Quorum(5);                 // majority = 3
        CapturingPrepare prep = new CapturingPrepare();
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(22, quorum, prep, acc);

        ProposalNumber n1 = p.onClientPropose("CLIENT");
        // trigger a rejection to enable retry
        p.onPromise(5, n1, false, null, null);
        assertTrue(p.shouldRetry());

        // retry -> new proposal number
        ProposalNumber n2 = p.retryOnce();
        assertNotEquals(n1, n2);

        // now 3 promises, one of them carries highest lastAccepted ("H")
        p.onPromise(1, n2, true, new ProposalNumber(7, 9), "L");
        p.onPromise(2, n2, true, new ProposalNumber(9, 1), "H");  // highest
        p.onPromise(3, n2, true, null, null);

        // assertions: inherited value and single ACCEPT broadcast
        assertTrue(p.phase1Done(), "phase-1 must complete after majority promises");
        assertEquals("H", p.chosenValueForAccept(), "must inherit highest lastAccepted value");
        assertTrue(p.phase2Sent(), "must send phase-2 after majority");
        assertEquals(1, acc.calls, "must broadcast accept exactly once");
        assertEquals(n2, acc.lastN, "accept uses the retried proposal number");
        assertEquals("H", acc.lastV, "accept uses the inherited value");
    }

    /**
     * Once phase-1 is done (majority reached), retryOnce() becomes a no-op:
     * it must not re-broadcast PREPARE nor change the proposal number.
     */
    @Test
    void retry_is_noop_after_phase1_done_no_prepare_resent() {
        Quorum quorum = new Quorum(5);
        CapturingPrepare prep = new CapturingPrepare();
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(23, quorum, prep, acc);

        ProposalNumber n = p.onClientPropose("V");
        assertEquals(1, prep.calls);

        // reach majority immediately (no rejection path this time)
        p.onPromise(1, n, true, null, null);
        p.onPromise(2, n, true, null, null);
        p.onPromise(3, n, true, null, null);
        assertTrue(p.phase1Done(), "phase-1 completed");
        assertTrue(p.phase2Sent(), "accept already sent");

        // calling retryOnce() now must not change anything
        ProposalNumber unchanged = p.retryOnce();
        assertEquals(n, unchanged, "retryOnce returns current number when phase-1 done");
        assertEquals(1, prep.calls, "no new PREPARE should be sent after phase-1 done");
    }

    /**
     * After a rejection-led retry, duplicates and mismatched rounds are still ignored,
     * and ACCEPT is sent only once upon reaching majority for the new round.
     */
    @Test
    void after_retry_ignore_duplicates_mismatched_and_send_single_accept() {
        Quorum quorum = new Quorum(5);
        CapturingPrepare prep = new CapturingPrepare();
        CapturingAccept acc = new CapturingAccept();
        Proposer p = new Proposer(24, quorum, prep, acc);

        ProposalNumber n1 = p.onClientPropose("ONCE");
        // cause a rejection then retry
        p.onPromise(9, n1, false, null, null);
        ProposalNumber n2 = p.retryOnce();

        // deliver a valid promise and a duplicate for the same member
        p.onPromise(1, n2, true, null, null);
        p.onPromise(1, n2, true, null, null); // duplicate -> ignored

        // mismatched round => ignored
        p.onPromise(2, new ProposalNumber(999, 888), true, null, null);

        // finish the majority for the retried round
        p.onPromise(2, n2, true, null, null);
        p.onPromise(3, n2, true, null, null);

        assertTrue(p.phase1Done());
        assertTrue(p.phase2Sent(), "accept must be sent once after majority");
        assertEquals(1, acc.calls, "accept broadcast only once");
        assertEquals(n2, acc.lastN);
        assertEquals("ONCE", acc.lastV, "client value used since no prior accepted seen in majority");
    }

}
