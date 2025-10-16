package au.edu.adelaide.paxos.transport;

import au.edu.adelaide.paxos.util.PaxosMessageParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTransport {

    private final Map<Integer, Dispatcher> peers = Collections.synchronizedMap(new HashMap<>());

    public void register(int memberId, Dispatcher dispatcher) {
        peers.put(memberId, dispatcher);
    }

    public void send(int to, PaxosMessage msg) {
        Dispatcher d = peers.get(to);
        if (d == null) return;
        String json = PaxosMessageParser.toJson(msg);
        PaxosMessage copy = PaxosMessageParser.fromJson(json);
        if (copy != null) {
            d.onMessage(copy);
        }
    }

    public void broadcast(PaxosMessage msg) {
        String json = PaxosMessageParser.toJson(msg);
        for (Dispatcher d : snapshot()) {
            PaxosMessage copy = PaxosMessageParser.fromJson(json);
            if (copy != null) d.onMessage(copy);
        }
    }

    private Dispatcher[] snapshot() {
        synchronized (peers) {
            return peers.values().toArray(new Dispatcher[0]);
        }
    }

}
