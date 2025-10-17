package au.edu.adelaide.paxos.transport;

import au.edu.adelaide.paxos.util.PaxosMessageParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {

    /**
     * Sends a PaxosMessage to the specified address using a TCP socket.
     *
     * @param address the destination address
     * @param msg     the PaxosMessage to send
     */
    public void sendPaxosMessage(InetSocketAddress address, PaxosMessage msg) {
        String line = PaxosMessageParser.toJson(msg) + "\n";
        try (Socket socket = new Socket()) {
            socket.connect(address);
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            writer.write(line);
            writer.flush();
            System.out.println("sent to " + address + " : " + msg.type() +
                    " from=" + msg.from() + " to=" + msg.to() + " n=" + msg.n());
        } catch (IOException e) {
            System.err.println("send failed to " + address + " : " + e.getMessage());
        }
    }
}
