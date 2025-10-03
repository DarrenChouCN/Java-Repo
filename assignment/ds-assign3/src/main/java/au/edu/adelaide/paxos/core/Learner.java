package au.edu.adelaide.paxos.core;

import java.util.*;
import java.util.function.Consumer;

public final class Learner {
    private final Quorum quorum;

    private final Map<ProposalNumber, Map<String, Set<Integer>>> votes = new HashMap<>();

    private boolean decided = false;
    private String decidedValue = null;

    private final List<Consumer<String>> listeners = new ArrayList<>();

    public Learner(Quorum quorum) {
        this.quorum = Objects.requireNonNull(quorum, "quorum");
    }

    public synchronized void addListener(Consumer<String> onConsensus) {
        if (onConsensus != null) listeners.add(onConsensus);
    }

    public synchronized void onAccepted(int fromMemberId, ProposalNumber n, String v) {
        if (decided) return;

        Objects.requireNonNull(n, "n");
        Objects.requireNonNull(v, "v");
        if (fromMemberId <= 0) throw new IllegalArgumentException("memberId must be > 0");

        Map<String, Set<Integer>> byValue = votes.computeIfAbsent(n, _ -> new HashMap<>());
        Set<Integer> senders = byValue.computeIfAbsent(v, _ -> new HashSet<>());
        boolean newVote = senders.add(fromMemberId);

        if (!newVote) return;

        if (senders.size() >= quorum.majorityCount()) {
            decided = true;
            decidedValue = v;

            for (Consumer<String> l : listeners) {
                try {
                    l.accept(v);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    public synchronized boolean isDecided() {
        return decided;
    }

    public synchronized String decidedValue() {
        return decidedValue;
    }
}
