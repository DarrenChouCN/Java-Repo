package au.edu.adelaide.paxos.core;

/**
 * Acceptor class that handles prepare and accept requests in the Paxos consensus algorithm.
 * It delegates state management to a PaxosState instance.
 */
public class Acceptor {

    private final PaxosState state;

    public Acceptor(PaxosState state) {
        this.state = state;
    }

    // Handles a prepare request from a proposer
    public Result.PromiseResult onPrepare(ProposalNumber n) {
        return state.onPrepare(n);
    }

    // Handles an accept request from a proposer
    public Result.AcceptResult onAcceptRequest(ProposalNumber n, String v) {
        return state.onAcceptRequest(n, v);
    }
}
