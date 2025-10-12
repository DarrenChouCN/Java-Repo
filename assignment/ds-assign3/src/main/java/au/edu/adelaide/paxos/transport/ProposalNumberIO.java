package au.edu.adelaide.paxos.transport;

import au.edu.adelaide.paxos.core.ProposalNumber;

public class ProposalNumberIO {

    private ProposalNumberIO() {
    }

    public static String format(ProposalNumber n) {
        if (n == null) return null;
        return n.getRound() + "." + n.getMemberId();
    }

    public static ProposalNumber parse(String s) {
        if (s == null) return null;
        int dot = s.indexOf('.');
        if (dot <= 0 || dot >= s.length() - 1) {
            throw new IllegalArgumentException("Invalid ProposalNumber: " + s);
        }
        long round = Long.parseLong(s.substring(0, dot));
        int member = Integer.parseInt(s.substring(dot + 1));
        return new ProposalNumber(round, member);
    }
}
