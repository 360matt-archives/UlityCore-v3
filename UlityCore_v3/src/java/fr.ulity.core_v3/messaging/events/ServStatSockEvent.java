package fr.ulity.core_v3.messaging.events;

import fr.ulity.core_v3.messaging.SocketClient;

import java.util.HashMap;

public class ServStatSockEvent {
    public static void bukkit () {
        SocketClient.listen("getOnline", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", org.bukkit.Bukkit.getOnlinePlayers().size());
            SocketClient.reply(json, hashMap);
        });
        SocketClient.listen("getMaxOnline", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("max", org.bukkit.Bukkit.getMaxPlayers());
            SocketClient.reply(json, hashMap);
        });
        SocketClient.listen("getOnlineAndMax", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", org.bukkit.Bukkit.getOnlinePlayers().size());
            hashMap.put("max", org.bukkit.Bukkit.getMaxPlayers());
            SocketClient.reply(json, hashMap);
        });

    }

    public static void bungee () {
        SocketClient.listen("getOnline", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", net.md_5.bungee.api.ProxyServer.getInstance().getOnlineCount());
            SocketClient.reply(json, hashMap);
        });
        SocketClient.listen("getMaxOnline", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("max", net.md_5.bungee.api.ProxyServer.getInstance().getConfig().getPlayerLimit());
            SocketClient.reply(json, hashMap);
        });
        SocketClient.listen("getOnlineAndMax", json -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("online", net.md_5.bungee.api.ProxyServer.getInstance().getOnlineCount());
            hashMap.put("max", net.md_5.bungee.api.ProxyServer.getInstance().getConfig().getPlayerLimit());
            SocketClient.reply(json, hashMap);
        });
    }
}
