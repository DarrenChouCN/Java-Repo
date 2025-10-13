package au.edu.adelaide.paxos.fault;

import au.edu.adelaide.paxos.transport.PaxosMessage;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class FaultProfile {

    public enum Kind {
        RELIABLE,
        STANDARD,
        LATENT,
        FAILURE
    }

    public static final class Sparse {
        private final Map<PaxosMessage.Type, Integer> gates = new EnumMap<>(PaxosMessage.Type.class);

        public Sparse gate(PaxosMessage.Type t, int everyN) {
            gates.put(Objects.requireNonNull(t), Math.max(everyN, 1));
            return this;
        }

        int gateOf(PaxosMessage.Type t) {
            return gates.getOrDefault(t, 1);
        }
    }


    public final Kind kind;
    public final long delayMinMs;
    public final long delayMaxMs;
    public final double dropRate;      // 0.0 ~ 1.0
    public final Sparse sparse;        // allow 1 of every N (per type)
    public final boolean crashOnce;    // one-shot crash
    public final PaxosMessage.Type crashAtType; // crash trigger on first seen type (optional)

    private FaultProfile(Builder b) {
        this.kind = b.kind;
        this.delayMinMs = b.delayMinMs;
        this.delayMaxMs = b.delayMaxMs;
        this.dropRate = b.dropRate;
        this.sparse = b.sparse;
        this.crashOnce = b.crashOnce;
        this.crashAtType = b.crashAtType;
    }

    public static final class Builder {
        private Kind kind = Kind.RELIABLE;
        private long delayMinMs = 0, delayMaxMs = 0;
        private double dropRate = 0.0;
        private Sparse sparse = new Sparse();
        private boolean crashOnce = false;
        private PaxosMessage.Type crashAtType = null;

        public Builder kind(Kind k) {
            this.kind = k;
            return this;
        }

        public Builder delay(long minMs, long maxMs) {
            this.delayMinMs = minMs;
            this.delayMaxMs = maxMs;
            return this;
        }

        public Builder drop(double rate) {
            this.dropRate = rate;
            return this;
        }

        public Builder sparse(Sparse s) {
            this.sparse = s;
            return this;
        }

        public Builder crashOnce(PaxosMessage.Type trigger) {
            this.crashOnce = true;
            this.crashAtType = trigger;
            return this;
        }

        public FaultProfile build() {
            return new FaultProfile(this);
        }
    }

    // some ready-made profiles
    public static FaultProfile reliable() {
        return new Builder().kind(Kind.RELIABLE).build();
    }

    public static FaultProfile standard() {
        return new Builder().kind(Kind.STANDARD).delay(0, 15).drop(0.02).build();
    }

    public static FaultProfile latent() {
        return new Builder().kind(Kind.LATENT).delay(50, 300).drop(0.05)
                .sparse(new Sparse().gate(PaxosMessage.Type.ACCEPTED, 2))
                .build();
    }

    public static FaultProfile failure() {
        return new Builder().kind(Kind.FAILURE).delay(20, 120).drop(0.10).crashOnce(PaxosMessage.Type.PREPARE).build();
    }
}
