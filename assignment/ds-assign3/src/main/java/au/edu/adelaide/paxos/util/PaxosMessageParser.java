package au.edu.adelaide.paxos.util;

import au.edu.adelaide.paxos.transport.PaxosMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class PaxosMessageParser {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(PaxosMessage.class, new PaxosMessageAdapter()).disableHtmlEscaping().create();

    private PaxosMessageParser() {
    }

    public static String toJson(PaxosMessage m) {
        return GSON.toJson(m, PaxosMessage.class);
    }

    public static PaxosMessage fromJson(String json) {
        try {
            return GSON.fromJson(json, PaxosMessage.class);
        } catch (RuntimeException e) {
            System.err.println("[PaxosMessageParser] decode failed: " + e.getMessage());
            return null;
        }
    }

    static final class PaxosMessageAdapter extends TypeAdapter<PaxosMessage> {

        @Override
        public void write(JsonWriter out, PaxosMessage m) throws IOException {
            out.beginObject();
            out.name("type").value(m.type().name());
            out.name("from").value(m.from());
            if (m.to() != null) out.name("to").value(m.to());
            if (m.n() != null) out.name("n").value(m.n());
            if (m.v() != null) out.name("v").value(m.v());
            if (m.acceptedN() != null) out.name("acceptedN").value(m.acceptedN());
            if (m.acceptedV() != null) out.name("acceptedV").value(m.acceptedV());
            if (m.promised() != null) out.name("promised").value(m.promised());
            if (m.corr() != null) out.name("corr").value(m.corr());
            out.endObject();
        }

        @Override
        public PaxosMessage read(JsonReader in) throws IOException {
            PaxosMessage.Type type = null;
            Integer from = null, to = null;
            String n = null, v = null, acceptedN = null, acceptedV = null, corr = null;
            Boolean promised = null;

            try {
                in.beginObject();
                while (in.hasNext()) {
                    String name = in.nextName();
                    switch (name) {
                        case "type" -> type = PaxosMessage.Type.valueOf(nextStringOrNull(in));
                        case "from" -> from = nextIntOrNull(in);
                        case "to" -> to = nextIntOrNull(in);
                        case "n" -> n = nextStringOrNull(in);
                        case "v" -> v = nextStringOrNull(in);
                        case "acceptedN" -> acceptedN = nextStringOrNull(in);
                        case "acceptedV" -> acceptedV = nextStringOrNull(in);
                        case "promised" -> promised = nextBoolOrNull(in);
                        case "corr" -> corr = nextStringOrNull(in);
                        default -> in.skipValue(); // ignore unknowns
                    }
                }
                in.endObject();
            } catch (RuntimeException e) {
                // coarse log + null (caller will drop the message)
                System.err.println("[PaxosMessageParser] read error: " + e.getMessage());
                return null;
            }
            // Very light sanity: if even type/from are missing, drop it quietly.
            if (type == null || from == null) return null;

            return PaxosMessage.newMessage(type)
                    .from(from).to(to).n(n).v(v)
                    .acceptedN(acceptedN).acceptedV(acceptedV)
                    .promised(promised).corr(corr)
                    .build();
        }

        private static String nextStringOrNull(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return in.nextString();
        }

        private static Integer nextIntOrNull(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return in.nextInt();
        }

        private static Boolean nextBoolOrNull(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            return in.nextBoolean();
        }
    }

}
