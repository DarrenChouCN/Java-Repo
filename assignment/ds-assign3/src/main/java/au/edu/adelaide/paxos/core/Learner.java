package au.edu.adelaide.paxos.core;

import java.util.*;
import java.util.function.Consumer;

/**
 * The learner is responsible for learning the chosen value once a proposal has been
 * accepted by a majority of acceptors.
 */
public final class Learner {

    private final Quorum quorum;

    /**
     * A proposal — identified by its round and proposer — maps to its candidate values and,
     * for each value, the set of acceptors that have accepted it.
     * <p>
     * Example: Seven round proposal from member 3,A1 A2 A3 accepted value "X"
     * votes = {(7,3) -> {"X" -> {1,2,3}}}
     */
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

    /**
     * If a proposal has been accepted by a majority of acceptors, the learner
     * considers the value chosen and notifies its listeners.
     *
     * @param fromMemberId The member ID of the acceptor sending the accepted message
     * @param n            The proposal number that was accepted
     * @param v            The value that was accepted
     */
    public synchronized void onAccepted(int fromMemberId, ProposalNumber n, String v) {
        if (decided) return;

        Objects.requireNonNull(n, "n");
        Objects.requireNonNull(v, "v");
        if (fromMemberId <= 0) throw new IllegalArgumentException("memberId must be > 0");

        // Record the vote
        Map<String, Set<Integer>> byValue = votes.computeIfAbsent(n, _ -> new HashMap<>());

        // For the given value, get or create the set of senders (acceptors) that voted for it
        Set<Integer> senders = byValue.computeIfAbsent(v, _ -> new HashSet<>());
        boolean newVote = senders.add(fromMemberId);

        if (!newVote) return;

        // Check if we have a quorum for this value
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
