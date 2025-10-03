package au.edu.adelaide.paxos.core;

/**
 * Quorum is a utility class to calculate majority counts for a given number of nodes.
 */
public class Quorum {

    private final int n;

    public Quorum(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be > 0");
        this.n = n;
    }

    /**
     * Returns the majority count for the given number of nodes.
     *
     * @return the majority count
     */
    public int majorityCount() {
        return (n / 2) + 1;
    }

    /**
     * Checks if the given count is a majority.
     *
     * @param count the count to check
     * @return true if the count is a majority, false otherwise
     */
    public boolean majority(int count) {
        return count >= majorityCount();
    }
}
