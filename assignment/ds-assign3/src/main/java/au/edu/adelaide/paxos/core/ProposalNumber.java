package au.edu.adelaide.paxos.core;

import java.util.Objects;

/**
 * A proposal number in the Paxos algorithm, consisting of a round number and a member ID.
 * Proposal numbers are compared first by round number, then by member ID.
 */
public class ProposalNumber implements Comparable<ProposalNumber> {

    private final long round;
    private final int memberId;

    public ProposalNumber(long round, int memberId) {
        if (round < 0) throw new IllegalArgumentException("round must be >= 0");
        if (memberId < 0) throw new IllegalArgumentException("memberId must be > 0");

        this.round = round;
        this.memberId = memberId;
    }

    @Override
    public int compareTo(ProposalNumber o) {
        if (this.round != o.round) {
            return Long.compare(this.round, o.round);
        }
        return Integer.compare(this.memberId, o.memberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProposalNumber that)) return false;
        return round == that.round && memberId == that.memberId;
    }

    public ProposalNumber nextRound() {
        return new ProposalNumber(this.round + 1, this.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(round, memberId);
    }

    @Override
    public String toString() {
        return round + "." + memberId;
    }

    public static ProposalNumber parse(String s) {
        String[] parts = s.split("\\.");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid format: " + s);
        return new ProposalNumber(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    }

    public long getRound() {
        return round;
    }

    public int getMemberId() {
        return memberId;
    }
}
