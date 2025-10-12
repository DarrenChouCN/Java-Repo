package au.edu.adelaide.paxos.transport;

import au.edu.adelaide.paxos.core.*;

import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.ACCEPT_REQUEST;
import static au.edu.adelaide.paxos.transport.PaxosMessage.Type.PREPARE;
import static au.edu.adelaide.paxos.transport.ProposalNumberIO.format;

public class Dispatcher {

    private final int localMemberId;
    private final InMemoryTransport transport;
    private final Proposer proposer;
    private final PaxosState acceptor;
    private final Learner learner;


    public Dispatcher(int localMemberId, InMemoryTransport transport, Proposer proposer, PaxosState acceptor, Learner learner) {
        this.localMemberId = localMemberId;
        this.transport = transport;
        this.proposer = proposer;
        this.acceptor = acceptor;
        this.learner = learner;
    }

    public void onMessage(PaxosMessage m) {
        if (m == null) return;

        switch (m.type()) {
            case CLIENT_PROPOSE -> handleClientPropose(m);
            case PREPARE -> handlePrepare(m);
            case ACCEPT_REQUEST -> handleAcceptRequest(m);
            case PROMISE -> handlePromise(m);
            case ACCEPTED -> handleAccepted(m);
        }
    }

    private void handleClientPropose(PaxosMessage m) {
        if (proposer == null) return;
        proposer.onClientPropose(m.v());
    }

    private void handlePrepare(PaxosMessage m) {
        if (acceptor == null) return;
        ProposalNumber pn = ProposalNumberIO.parse(m.n());
        Result.PromiseResult r = acceptor.onPrepare(pn); // uses your PaxosState API
        // reply PROMISE back to sender
        PaxosMessage resp = PaxosMessage.newMessage(PaxosMessage.Type.PROMISE)
                .from(localMemberId).to(m.from())
                .n(format(pn))
                .promised(r.promised)
                .acceptedN(format(r.lastAcceptedN))
                .acceptedV(r.lastAcceptedV)
                .corr(m.corr())
                .build();
        transport.send(m.from(), resp);
    }

    private void handleAcceptRequest(PaxosMessage m) {
        if (acceptor == null) return;
        ProposalNumber pn = ProposalNumberIO.parse(m.n());
        Result.AcceptResult ar = acceptor.onAcceptRequest(pn, m.v());
        if (ar.accepted) {
            // Broadcast ACCEPTED to all learners (and proposers if they listen)
            PaxosMessage accepted = PaxosMessage.newMessage(PaxosMessage.Type.ACCEPTED)
                    .from(localMemberId).to(null)
                    .n(format(ar.acceptedN))   // reflect what acceptor actually persisted
                    .v(ar.acceptedV)
                    .corr(m.corr())
                    .build();
            transport.broadcast(accepted);
        }
        // If not accepted, classic Paxos does not require a negative message here.
    }

    private void handlePromise(PaxosMessage m) {
        if (proposer == null) return;
        ProposalNumber pn = ProposalNumberIO.parse(m.n());
        ProposalNumber an = ProposalNumberIO.parse(m.acceptedN());
        boolean promised = (m.promised() != null) && m.promised();
        proposer.onPromise(m.from(), pn, promised, an, m.acceptedV());
    }

    private void handleAccepted(PaxosMessage m) {
        if (learner == null) return;
        ProposalNumber pn = ProposalNumberIO.parse(m.n());
        learner.onAccepted(m.from(), pn, m.v());
    }

    // Convenience: outgoing sends from proposer side
    public void broadcastPrepare(ProposalNumber n, String corr) {
        PaxosMessage msg = PaxosMessage.newMessage(PREPARE)
                .from(localMemberId).to(null).n(format(n)).corr(corr).build();
        transport.broadcast(msg);
    }

    public void broadcastAcceptRequest(ProposalNumber n, String v, String corr) {
        PaxosMessage msg = PaxosMessage.newMessage(ACCEPT_REQUEST)
                .from(localMemberId).to(null).n(format(n)).v(v).corr(corr).build();
        transport.broadcast(msg);
    }

    public int localMemberId() {
        return localMemberId;
    }

}
