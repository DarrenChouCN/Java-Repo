package au.edu.adelaide.paxos.app;

import au.edu.adelaide.paxos.core.Proposer;
import au.edu.adelaide.paxos.util.Logging;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

public class CliCommands implements Runnable {

    private final int selfId;
    private final Proposer proposer;
    private final AtomicBoolean running;

    public CliCommands(int selfId, Proposer proposer, AtomicBoolean running) {
        this.selfId = selfId;
        this.proposer = proposer;
        this.running = running;
    }

    @Override
    public void run() {
        Logging.info("CLI ready: type `propose <VALUE>` or `quit`");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while (running.get() && (line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String low = line.toLowerCase(Locale.ROOT);
                if (low.equals("quit") || low.equals("exit")) {
                    Logging.info("CLI received quit");
                    running.set(false);
                    break;
                }
                // propose M5 / PROPOSE   myValue  / propose  my-value
                if (low.startsWith("propose")) {
                    String[] tokens = line.split("\\s+", 2);
                    if (tokens.length < 2 || tokens[1].isBlank()) {
                        Logging.warn("usage: propose <VALUE>");
                        continue;
                    }
                    String value = tokens[1].trim();
                    Logging.info("CLI propose value=" + value + " from=M" + selfId);
                    proposer.onClientPropose(value);
                    continue;
                }
                Logging.warn("unknown command: " + line);
            }
        } catch (Exception e) {
            Logging.error("CLI loop error", e);
        }
    }
}
