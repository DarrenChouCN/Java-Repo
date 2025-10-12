package au.edu.adelaide.paxos.transport;

import java.util.Objects;

public final class PaxosMessage {

    public enum Type {
        // proposer -> acceptors
        PREPARE,

        // acceptor -> proposer
        PROMISE,

        // proposer -> acceptors
        ACCEPT_REQUEST,

        // acceptor -> learner
        ACCEPTED,

        // client(local) -> proposer
        CLIENT_PROPOSE
    }

    private final Type type;
    private final int from;          // sender memberId
    private final Integer to;        // null => broadcast
    private final String n;          // ProposalNumber formatted as "round.memberId"
    private final String v;          // candidate value
    private final String acceptedN;  // acceptor's last accepted N
    private final String acceptedV;  // acceptor's last accepted V
    private final Boolean promised;  // for PROMISE: true/false
    private final String corr;       // optional correlation id

    private PaxosMessage(Builder b) {
        this.type = Objects.requireNonNull(b.type, "type");
        this.from = b.from;
        this.to = b.to;
        this.n = b.n;
        this.v = b.v;
        this.acceptedN = b.acceptedN;
        this.acceptedV = b.acceptedV;
        this.promised = b.promised;
        this.corr = b.corr;
    }

    public Type type() {
        return type;
    }

    public int from() {
        return from;
    }

    public Integer to() {
        return to;
    }

    public String n() {
        return n;
    }

    public String v() {
        return v;
    }

    public String acceptedN() {
        return acceptedN;
    }

    public String acceptedV() {
        return acceptedV;
    }

    public Boolean promised() {
        return promised;
    }

    public String corr() {
        return corr;
    }

    public static Builder newMessage(Type type) {
        return new Builder().type(type);
    }

    public static final class Builder {
        private Type type;
        private int from;
        private Integer to;
        private String n;
        private String v;
        private String acceptedN;
        private String acceptedV;
        private Boolean promised;
        private String corr;

        public Builder type(Type t) {
            this.type = t;
            return this;
        }

        public Builder from(int f) {
            this.from = f;
            return this;
        }

        public Builder to(Integer t) {
            this.to = t;
            return this;
        }

        public Builder n(String s) {
            this.n = s;
            return this;
        }

        public Builder v(String s) {
            this.v = s;
            return this;
        }

        public Builder acceptedN(String s) {
            this.acceptedN = s;
            return this;
        }

        public Builder acceptedV(String s) {
            this.acceptedV = s;
            return this;
        }

        public Builder promised(Boolean b) {
            this.promised = b;
            return this;
        }

        public Builder corr(String s) {
            this.corr = s;
            return this;
        }

        public PaxosMessage build() {
            return new PaxosMessage(this);
        }
    }

    @Override
    public String toString() {
        return "PaxosMessage{" + type + ", from=" + from + ", to=" + to + ", n=" + n +
                ", v=" + v + ", promised=" + promised + ", acceptedN=" + acceptedN +
                ", acceptedV=" + acceptedV + ", corr=" + corr + "}";
    }
}
