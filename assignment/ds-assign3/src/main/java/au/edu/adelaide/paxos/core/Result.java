package au.edu.adelaide.paxos.core;

public class Result {

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
