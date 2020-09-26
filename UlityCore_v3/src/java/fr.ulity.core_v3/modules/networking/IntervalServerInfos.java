package fr.ulity.core_v3.modules.networking;

import fr.ulity.core_v3.messaging.SocketClient;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class IntervalServerInfos {
    public static ConcurrentHashMap<String, ServerInfos> refreshed = new ConcurrentHashMap<>();

    public static void init (String servername) {
        new Thread(() -> {
            try {
                while (true) {
                    SocketClient.send(servername, "serverInfo", new HashMap<>())
                            .callback(x -> refreshed.put(servername, new ServerInfos(x)))
                            .timeout((x) -> refreshed.put(servername, new ServerInfos(null)));
                    TimeUnit.MILLISECONDS.sleep(250);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static ServerInfos get (String servername) {
        if (!refreshed.containsKey(servername)) {
            init(servername);
            return ServerInfos.get(servername);
        } else return refreshed.get(servername);
    }

}
