package au.edu.adelaide.paxos.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The proposer is responsible for initiating proposals and driving the Paxos consensus process.
 * It handles phase 1 (prepare/promise) and phase 2 (accept/accepted) of the Paxos algorithm.
 */
public class Proposer {

    public interface PrepareBroadcaster {
        void broadcastPrepare(ProposalNumber n);
    }

    public interface AcceptBroadcaster {
        void broadcastAcceptRequest(ProposalNumber n, String v);
    }

    private final int memberId;
    private final Quorum quorum;
    private final PrepareBroadcaster broadcaster;
    private final AcceptBroadcaster acceptBroadcaster;

    // current proposal number
    private long roundCounter = 0;

    // state for phase 1
    private ProposalNumber currentN = null;
    private String clientValue = null;
    private boolean phase1Done = false;

    private final Set<Integer> promisedFrom = new HashSet<>();
    private ProposalNumber highestAcceptedN = null;
    private String highestAcceptedV = null;

    // state for phase 2
    private String chosenValueForAccept = null;
    private boolean phase2Sent = false;

    public Proposer(int memberId, Quorum quorum, PrepareBroadcaster broadcaster, AcceptBroadcaster acceptBroadcaster) {
        if (memberId <= 0) throw new IllegalArgumentException("memberId > 0 required");
        this.memberId = memberId;
        this.quorum = Objects.requireNonNull(quorum, "quorum");
        this.broadcaster = Objects.requireNonNull(broadcaster, "broadcaster");
        this.acceptBroadcaster = Objects.requireNonNull(acceptBroadcaster, "acceptBroadcaster");
    }

    /**
     * Start a new proposal with the given value.
     * This will initiate phase 1 of the Paxos algorithm by broadcasting a prepare message.
     * The proposer will wait for promises from a majority of acceptors before proceeding to phase 2.
     *
     * @param value the value to propose
     * @return the proposal number used for this proposal
     * @throws NullPointerException if value is null
     */
    public synchronized ProposalNumber onClientPropose(String value) {
        // initialize for new proposal
        Objects.requireNonNull(value, "value");
        phase1Done = false;
        phase2Sent = false;
        promisedFrom.clear();
        highestAcceptedN = null;
        highestAcceptedV = null;
        chosenValueForAccept = null;
        clientValue = value;

        // start phase 1
        roundCounter += 1;
        currentN = new ProposalNumber(roundCounter, memberId);

        broadcaster.broadcastPrepare(currentN);
        return currentN;
    }

    /**
     * Handle a promise response from an acceptor.
     * If a majority of promises are received for the current proposal number,
     * phase 1 is considered done and the proposer can proceed to phase 2.
     *
     * @param fromMemberId  the ID of the member sending the promise
     * @param n             the proposal number of the promise
     * @param promised      true if the acceptor promises to accept proposals with number >= n
     * @param lastAcceptedN the highest proposal number accepted by the acceptor, or null if none
     * @param lastAcceptedV the value associated with lastAcceptedN, or null if none
     */
    public synchronized void onPromise(
            int fromMemberId, ProposalNumber n, boolean promised, ProposalNumber lastAcceptedN, String lastAcceptedV) {
        // ignore if not for current proposal or phase 1 already done
        if (currentN == null) return;
        if (!currentN.equals(n)) return;
        if (!promised) return;
        if (phase1Done) return;

        if (!promisedFrom.add(fromMemberId)) {
            return;
        }

        // update highest accepted value if necessary
        if (lastAcceptedN != null) {
            if (highestAcceptedN == null || lastAcceptedN.compareTo(highestAcceptedN) > 0) {
                highestAcceptedN = lastAcceptedN;
                highestAcceptedV = lastAcceptedV;
            }
        }

        // if received promises from majority, phase 1 is done
        if (promisedFrom.size() >= quorum.majorityCount()) {
            phase1Done = true;
            chosenValueForAccept = (highestAcceptedV != null) ? highestAcceptedV : clientValue;
            // Phase 2: idempotent broadcast
            if (!phase2Sent) {
                phase2Sent = true;
                acceptBroadcaster.broadcastAcceptRequest(currentN, chosenValueForAccept);
            }
        }
    }

    public synchronized ProposalNumber currentN() {
        return currentN;
    }

    public synchronized boolean phase1Done() {
        return phase1Done;
    }

    public synchronized String chosenValueForAccept() {
        return chosenValueForAccept;
    }

    public synchronized int promisedCount() {
        return promisedFrom.size();
    }

    public synchronized boolean phase2Sent() {
        return phase2Sent;
    }

}
