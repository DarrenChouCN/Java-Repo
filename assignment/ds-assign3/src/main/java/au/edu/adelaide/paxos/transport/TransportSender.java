package au.edu.adelaide.paxos.transport;

public interface TransportSender {

    void send(int to, PaxosMessage msg);

    void broadcast(PaxosMessage msg);
}
