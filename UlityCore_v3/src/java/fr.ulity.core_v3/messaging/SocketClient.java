package fr.ulity.core_v3.messaging;

import fr.ulity.core_v3.Core;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.function.Consumer;

public class SocketClient {
    private static fr.ulity.socketexchange.api.SocketClient client = null;

    public static boolean isEnabled() {
        return Core.config.getBoolean("bungeecord.enabled");
    }

    public static void start() {
        if (isEnabled() && client == null) {
            client = new fr.ulity.socketexchange.api.SocketClient(Core.servername, "127.0.0.1", Core.config.getInt("bungeecord.socket_port"));
        }
    }

    public static void listen (String channel, Consumer<JSONObject> callback) {
        client.listen(channel, callback);
    }

    public static void reply(JSONObject before, JSONObject reply) {
        client.reply(before, reply);
    }

    public static void reply(JSONObject before, HashMap<String, Object> reply) {
        client.reply(before, reply);
    }

    public static fr.ulity.socketexchange.api.SocketClient.Callback send (String server, String channel, JSONObject data) {
        return client.send(server, channel, data);
    }

    public static fr.ulity.socketexchange.api.SocketClient.Callback send (String server, String channel, HashMap<String, Object> data) {
        return client.send(server, channel, data);
    }



    public static boolean isOnline(String server) { return client.isOnline(server); }
    public static int getPing(String server) { return client.getPing(server); }

}
