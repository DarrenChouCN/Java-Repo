package au.edu.adelaide.paxos.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Load network configuration from a text file.
 * Each line: <memberId> <host> <port>
 */
public class NetworkConfigLoader {

    private final Map<Integer, Peer> peers = new LinkedHashMap<>();

    public static NetworkConfigLoader load(InputStream in) throws IOException {
        NetworkConfigLoader cfg = new NetworkConfigLoader();
        cfg.parse(in);
        return cfg;
    }

    private void parse(InputStream in) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            int lineno = 0;
            Set<Integer> ports = new HashSet<>();
            while ((line = br.readLine()) != null) {
                lineno++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#") || line.startsWith("//")) continue;
                String[] toks = line.split("\\s+");
                if (toks.length != 3) {
                    System.err.println("skip invalid line " + lineno + ": " + line);
                    continue;
                }
                int id = Integer.parseInt(toks[0]);
                String host = toks[1];
                int port = Integer.parseInt(toks[2]);
                if (!ports.add(port)) {
                    throw new IOException("Duplicate port " + port + " at line " + lineno);
                }
                if (peers.put(id, new Peer(id, host, port)) != null) {
                    throw new IOException("Duplicate memberId " + id + " at line " + lineno);
                }
            }
        }
        if (peers.isEmpty()) throw new IOException("No peers configured");
    }

    public Peer getPeer(int id) {
        return peers.get(id);
    }

    public Collection<Peer> getAllPeersExcept(int selfId) {
        List<Peer> out = new ArrayList<>();
        for (Peer p : peers.values()) if (p.id != selfId) out.add(p);
        return out;
    }

    /**
     * Example configuration line:
     * 1 localhost 8080
     * 2 localhost 8081
     * 3 localhost 8082
     */
    public static final class Peer {
        public final int id;
        public final String host;
        public final int port;

        public Peer(int id, String host, int port) {
            this.id = id;
            this.host = host;
            this.port = port;
        }

        public InetSocketAddress address() {
            return new InetSocketAddress(host, port);
        }

        @Override
        public String toString() {
            return "Peer{" +
                    "id=" + id +
                    ", host='" + host + '\'' +
                    ", port=" + port +
                    '}';
        }
    }
}
