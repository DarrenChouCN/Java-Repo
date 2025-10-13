package au.edu.adelaide.paxos.fault;

import au.edu.adelaide.paxos.transport.PaxosMessage;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class FaultInjector {
    public interface CrashHandler {
        /**
         * Handle a one-shot crash event (e.g., stop server/consumer threads).
         */
        void crash(String reason) throws RuntimeException;
    }

    private final FaultProfile profile;
    private final CrashHandler crashHandler;
    private final AtomicBoolean crashed = new AtomicBoolean(false);
    private final AtomicLong counters = new AtomicLong(); // simple rolling counter for sparse gating

    public static final FaultInjector NOOP = new FaultInjector(FaultProfile.reliable(), r -> {
    });

    public FaultInjector(FaultProfile profile, CrashHandler crashHandler) {
        this.profile = Objects.requireNonNull(profile);
        this.crashHandler = Objects.requireNonNull(crashHandler);
    }

    /**
     * returns false => drop the message; true => continue
     */
    public boolean beforeSendOrDispatch(PaxosMessage m, String side, Integer toIdOrNull) {
        // crash-once
        if (profile.crashOnce && !crashed.get() && profile.crashAtType != null && m.type() == profile.crashAtType) {
            if (crashed.compareAndSet(false, true)) {
                log("crash profile hit: profile=%s type=%s side=%s", profile.kind, m.type(), side);
                crashHandler.crash("fault-profile crash@" + m.type());
                return false;
            }
        }
        // drop
        if (prob(profile.dropRate)) {
            log("drop: profile=%s type=%s from=%s to=%s", profile.kind, m.type(), m.from(), toIdOrNull);
            return false;
        }
        // sparse gating per type
        int gate = profile.sparse.gateOf(m.type());
        if (gate > 1) {
            long n = counters.incrementAndGet();
            if ((n % gate) != 0) {
                log("sparse-drop: profile=%s type=%s passEvery=%d from=%s to=%s",
                        profile.kind, m.type(), gate, m.from(), toIdOrNull);
                return false;
            }
        }
        // delay
        long d = delay(profile.delayMinMs, profile.delayMaxMs);
        if (d > 0) {
            log("delay %dms: profile=%s type=%s", d, profile.kind, m.type());
            sleepQuiet(d);
        }
        return true;
    }

    private static boolean prob(double p) {
        return p > 0 && ThreadLocalRandom.current().nextDouble() < p;
    }

    private static long delay(long min, long max) {
        if (max <= 0 || max < min) return 0;
        if (min == max) return min;
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    private static void sleepQuiet(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private static void log(String fmt, Object... args) {
        System.out.println(String.format(fmt, args));
    }
}
