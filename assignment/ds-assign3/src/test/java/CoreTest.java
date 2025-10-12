import au.edu.adelaide.paxos.core.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class CoreTest {


    // ------------------------ ProposalNumber tests ------------------------

    @Test
    void compareOrdersByRoundThenMember() {
        ProposalNumber a = new ProposalNumber(1, 1);
        ProposalNumber b = new ProposalNumber(2, 1);
        ProposalNumber c = new ProposalNumber(2, 2);

        assertTrue(a.compareTo(b) < 0, "1.x < 2.x");
        assertTrue(b.compareTo(c) < 0, "2.1 < 2.2");
        assertTrue(c.compareTo(b) > 0, "2.2 > 2.1");
    }

    @Test
    void nextRoundIsMonotonic() {
        ProposalNumber p = new ProposalNumber(3, 4);
        ProposalNumber next = p.nextRound();

        assertEquals(3, p.getRound());
        assertEquals(4, p.getMemberId());
        assertEquals(4, next.getRound());
        assertEquals(4, next.getMemberId());
        assertTrue(p.compareTo(next) < 0);
    }

    @Test
    void parseAndToStringAreSymmetric() {
        ProposalNumber p = new ProposalNumber(10, 9);
        ProposalNumber q = ProposalNumber.parse(p.toString());
        assertEquals(p, q);
    }


    // ------------------------ Quorum tests ------------------------

    @Test
    void majorityCountOdd() {
        Quorum q = new Quorum(9);
        assertEquals(5, q.majorityCount());  // 9 -> 5
        assertTrue(q.majority(5));
        assertFalse(q.majority(4));
    }

    @Test
    void majorityCountEven() {
        Quorum q = new Quorum(6);
        assertEquals(4, q.majorityCount());  // 6 -> 4
        assertTrue(q.majority(4));
        assertFalse(q.majority(3));
    }

    // ------------------------ PaxosState and Acceptor tests ------------------------

    @Test
    void prepare_firstPromiseSucceeds_andCarriesNullAccepted() {
        PaxosState s = new PaxosState();
        ProposalNumber n1 = new ProposalNumber(1, 1);

        Result.PromiseResult r1 = s.onPrepare(n1);
        assertTrue(r1.promised);
        assertNull(r1.lastAcceptedN);
        assertNull(r1.lastAcceptedV);
        assertEquals(n1, s.maxPromisedN());
    }

    @Test
    void prepare_smallerThanMaxPromised_isRejected() {
        PaxosState s = new PaxosState();
        ProposalNumber n2 = new ProposalNumber(2, 1);
        ProposalNumber n1 = new ProposalNumber(1, 9);

        // promise n2: round 2 > null
        assertTrue(s.onPrepare(n2).promised);
        Result.PromiseResult r = s.onPrepare(n1);

        // reject n1: round 1 < round 2
        assertFalse(r.promised);
        assertEquals(n2, s.maxPromisedN());
    }

    @Test
    void acceptRequest_afterPromiseWithSameOrHigherN_isAcceptedAndRecorded() {
        PaxosState s = new PaxosState();
        ProposalNumber n3 = new ProposalNumber(3, 5);

        // promise n3: round 3 > null
        assertTrue(s.onPrepare(n3).promised);
        Result.AcceptResult a = s.onAcceptRequest(n3, "M5");
        assertTrue(a.accepted);
        assertEquals(n3, a.acceptedN);
        assertEquals("M5", a.acceptedV);
        assertEquals(n3, s.acceptedN());
        assertEquals("M5", s.acceptedV());
        assertEquals(n3, s.maxPromisedN());         // max promised remains n3
    }

    @Test
    void acceptRequest_lowerThanMaxPromised_isRejected() {
        PaxosState s = new PaxosState();
        ProposalNumber n5 = new ProposalNumber(5, 2);
        ProposalNumber n4 = new ProposalNumber(4, 9);

        // promise n5: round 5 > null
        assertTrue(s.onPrepare(n5).promised);

        // reject n4: round 4 < round 5
        Result.AcceptResult a = s.onAcceptRequest(n4, "M1");
        assertFalse(a.accepted);
        assertNull(s.acceptedN());
        assertNull(s.acceptedV());

        // max promised remains n5
        assertEquals(n5, s.maxPromisedN());
    }

    @Test
    void prepare_afterAccepted_carriesLastAcceptedPair() {
        PaxosState s = new PaxosState();
        ProposalNumber n2 = new ProposalNumber(2, 1);
        ProposalNumber n3 = new ProposalNumber(3, 1);

        // promise n2 and accept "M8"
        assertTrue(s.onPrepare(n2).promised);
        assertTrue(s.onAcceptRequest(n2, "M8").accepted);

        // promise n3 and check it carries last accepted pair
        Result.PromiseResult p = s.onPrepare(n3);
        assertTrue(p.promised);
        assertEquals(n2, p.lastAcceptedN);
        assertEquals("M8", p.lastAcceptedV);
    }

    @Test
    void onPrepare_and_onAcceptRequest_basicHappyPath() {
        PaxosState state = new PaxosState();
        Acceptor acceptor = new Acceptor(state);

        ProposalNumber n = new ProposalNumber(7, 3);

        Result.PromiseResult pr = acceptor.onPrepare(n);
        assertTrue(pr.promised);

        Result.AcceptResult ar = acceptor.onAcceptRequest(n, "M4");
        assertTrue(ar.accepted);
        assertEquals(n, ar.acceptedN);
        assertEquals("M4", ar.acceptedV);

        assertEquals(n, state.acceptedN());
        assertEquals("M4", state.acceptedV());
    }

    @Test
    void onAcceptRequest_rejectsWhenLowerThanMaxPromised() {
        PaxosState state = new PaxosState();
        Acceptor acceptor = new Acceptor(state);

        ProposalNumber nHigh = new ProposalNumber(10, 1);
        ProposalNumber nLow = new ProposalNumber(9, 9);

        // promise nHigh
        assertTrue(acceptor.onPrepare(nHigh).promised);
        // reject nLow
        Result.AcceptResult ar = acceptor.onAcceptRequest(nLow, "X");

        assertFalse(ar.accepted);
        assertNull(state.acceptedN());
        assertNull(state.acceptedV());
    }

    // ------------------------ Learner tests ------------------------
    @Test
    void reachesConsensusWhenMajorityUniqueSenders() {
        Learner learner = new Learner(new Quorum(9));
        ProposalNumber n = new ProposalNumber(1, 1);

        learner.onAccepted(1, n, "M5");
        learner.onAccepted(2, n, "M5");
        learner.onAccepted(3, n, "M5");
        learner.onAccepted(4, n, "M5");
        assertFalse(learner.isDecided());

        learner.onAccepted(5, n, "M5");
        assertTrue(learner.isDecided());
        assertEquals("M5", learner.decidedValue());
    }

    @Test
    void duplicateAndOutOfOrderDoNotBreakCounting() {
        Learner learner = new Learner(new Quorum(9));
        ProposalNumber n = new ProposalNumber(2, 3);

        learner.onAccepted(2, n, "A");
        learner.onAccepted(1, n, "A");
        learner.onAccepted(2, n, "A");
        learner.onAccepted(4, n, "A");
        learner.onAccepted(3, n, "A");
        learner.onAccepted(7, n, "A");

        assertTrue(learner.isDecided());
        assertEquals("A", learner.decidedValue());
    }

    @Test
    void countsPerValueSeparatelyUnderSameProposalNumber() {
        Learner learner = new Learner(new Quorum(9));
        ProposalNumber n = new ProposalNumber(5, 2);

        learner.onAccepted(1, n, "A");
        learner.onAccepted(2, n, "A");
        learner.onAccepted(3, n, "A");

        learner.onAccepted(4, n, "B");
        learner.onAccepted(5, n, "B");
        learner.onAccepted(6, n, "B");

        assertFalse(learner.isDecided());
    }

    @Test
    void listenerIsCalledOnceWhenDecided() {
        Learner learner = new Learner(new Quorum(9));
        ProposalNumber n = new ProposalNumber(7, 9);

        AtomicReference<String> got = new AtomicReference<>();
        AtomicInteger times = new AtomicInteger(0);
        learner.addListener(v -> { got.set(v); times.incrementAndGet(); });

        learner.onAccepted(1, n, "X");
        learner.onAccepted(2, n, "X");
        learner.onAccepted(3, n, "X");
        learner.onAccepted(4, n, "X");
        learner.onAccepted(5, n, "X");

        assertTrue(learner.isDecided());
        assertEquals("X", got.get());
        assertEquals(1, times.get());

        learner.onAccepted(6, n, "X");
        learner.onAccepted(7, n, "X");
        assertEquals(1, times.get());
    }
}
