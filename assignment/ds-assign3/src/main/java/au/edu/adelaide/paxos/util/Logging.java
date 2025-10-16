package au.edu.adelaide.paxos.util;

import au.edu.adelaide.paxos.transport.PaxosMessage;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public final class Logging {

    private Logging() {
    }

    private static String ts() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    public static void tx(PaxosMessage m, Integer toId, String extra) {
        // Single-line, stable field order for grep
        System.out.printf(
                "TX ts=%s type=%s from=M%d to=%s n=%s v=%s corr=%s %s%n",
                ts(), m.type(), m.from(), (toId == null ? "ALL" : ("M" + toId)),
                nv(m.n()), nv(m.v()), nv(m.corr()),
                (extra == null ? "" : extra));
    }

    public static void rx(PaxosMessage m, int selfId, String extra) {
        System.out.printf(
                "RX ts=%s self=M%d type=%s from=M%d to=%s n=%s v=%s promised=%s acceptedN=%s acceptedV=%s corr=%s %s%n",
                ts(), selfId, m.type(), m.from(),
                (m.to() == null ? "ALL" : "M" + m.to()), nv(m.n()), nv(m.v()),
                String.valueOf(m.promised()),
                nv(m.acceptedN()), nv(m.acceptedV()),
                nv(m.corr()),
                (extra == null ? "" : extra));
    }

    public static void consensus(int selfId, String value, String n) {
        System.out.printf(
                "CONSENSUS ts=%s self=M%d value=%s n=%s%n",
                ts(), selfId, nv(value), nv(n));
    }

    public static void info(String msg) {
        System.out.println("INFO ts=" + ts() + " " + msg);
    }

    public static void warn(String msg) {
        System.out.println("WARN ts=" + ts() + " " + msg);
    }

    public static void error(String msg, Throwable t) {
        System.out.println("ERROR ts=" + ts() + " " + msg + " ex=" + (t == null ? "" : t.getMessage()));
    }

    private static String nv(String s) {
        return s == null ? "-" : s;
    }
}
