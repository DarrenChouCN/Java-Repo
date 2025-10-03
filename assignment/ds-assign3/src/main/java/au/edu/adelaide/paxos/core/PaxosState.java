package au.edu.adelaide.paxos.core;

import java.util.Objects;

/**
 * PaxosState is used for maintaining the state of a Paxos acceptor. It keeps track of maxPromisedN,
 * acceptedN, and acceptedV, which are used for managing the Paxos consensus process.
 * This class is thread-safe.
 */
public class PaxosState {

    // The highest proposal number promised to any proposer
    private ProposalNumber maxPromisedN;

    // The proposal number and value of the last accepted proposal
    private ProposalNumber acceptedN;

    // The value of the last accepted proposal
    private String acceptedV;


    // Constructor initializes the PaxosState with no promises or accepted proposals
    public synchronized PromiseResult onPrepare(ProposalNumber n) {
        Objects.requireNonNull(n);
        if (maxPromisedN == null || n.compareTo(maxPromisedN) >= 0) {
            maxPromisedN = n;
            return new PromiseResult(true, acceptedN, acceptedV);
        } else {
            return new PromiseResult(false, acceptedN, acceptedV);
        }
    }

    // Handles an accept request from a proposer
    public synchronized AcceptResult onAcceptRequest(ProposalNumber n, String v) {
        Objects.requireNonNull(n, "n");
        Objects.requireNonNull(v, "v");
        if (maxPromisedN == null || n.compareTo(maxPromisedN) >= 0) {
            maxPromisedN = n;
            acceptedN = n;
            acceptedV = v;
            return new AcceptResult(true, acceptedN, acceptedV);
        } else {
            return new AcceptResult(false, acceptedN, acceptedV);
        }
    }

    public synchronized ProposalNumber maxPromisedN() {
        return maxPromisedN;
    }

    public synchronized ProposalNumber acceptedN() {
        return acceptedN;
    }

    public synchronized String acceptedV() {
        return acceptedV;
    }

    // Result of a prepare request
    public static final class PromiseResult {
        public final boolean promised;
        public final ProposalNumber lastAcceptedN;
        public final String lastAcceptedV;

        public PromiseResult(boolean promised, ProposalNumber n, String v) {
            this.promised = promised;
            this.lastAcceptedN = n;
            this.lastAcceptedV = v;
        }
    }

    // Result of an accept request
    public static final class AcceptResult {
        public final boolean accepted;
        public final ProposalNumber acceptedN;
        public final String acceptedV;

        public AcceptResult(boolean accepted, ProposalNumber n, String v) {
            this.accepted = accepted;
            this.acceptedN = n;
            this.acceptedV = v;
        }
    }

}
