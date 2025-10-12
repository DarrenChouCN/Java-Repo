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
    public synchronized Result.PromiseResult onPrepare(ProposalNumber n) {
        Objects.requireNonNull(n);
        if (maxPromisedN == null || n.compareTo(maxPromisedN) >= 0) {
            maxPromisedN = n;
            return new Result.PromiseResult(true, acceptedN, acceptedV);
        } else {
            return new Result.PromiseResult(false, acceptedN, acceptedV);
        }
    }

    // Handles an accept request from a proposer
    public synchronized Result.AcceptResult onAcceptRequest(ProposalNumber n, String v) {
        Objects.requireNonNull(n, "n");
        Objects.requireNonNull(v, "v");
        if (maxPromisedN == null || n.compareTo(maxPromisedN) >= 0) {
            maxPromisedN = n;
            acceptedN = n;
            acceptedV = v;
            return new Result.AcceptResult(true, acceptedN, acceptedV);
        } else {
            return new Result.AcceptResult(false, acceptedN, acceptedV);
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



}
