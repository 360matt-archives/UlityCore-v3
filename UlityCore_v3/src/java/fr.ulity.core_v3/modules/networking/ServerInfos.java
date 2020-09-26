package fr.ulity.core_v3.modules.networking;


import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.messaging.SocketClient;
import net.md_5.bungee.api.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class ServerInfos {
    public static class PlayersStructure {
        public int count, max;
        public Set<String> names;
    }


    public final boolean isOnline;
    public final PlayersStructure players = new PlayersStructure();

    public ServerInfos (JSONObject json) {
        isOnline = json != null;

        if (isOnline) {
            players.count = json.getInt("players.count");
            players.max = json.getInt("players.max");
            players.names = json.getJSONArray("players.names").toList().stream().map(Object::toString).collect(Collectors.toSet());
        }
    }


    public static void registerListener () {
        SocketClient.listen("serverInfo", x -> {
            final JSONObject rep = new JSONObject("{}");
            if (Core.type.equals(Core.ServerApiType.BUKKIT)) {
                rep.put("players.count", org.bukkit.Bukkit.getOnlinePlayers().size());
                rep.put("players.max", org.bukkit.Bukkit.getMaxPlayers());
                rep.put("players.names", Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            } else if (Core.type.equals(Core.ServerApiType.BUNGEE)) {
                rep.put("players.count", net.md_5.bungee.api.ProxyServer.getInstance().getOnlineCount());
                rep.put("players.max", net.md_5.bungee.api.ProxyServer.getInstance().getConfig().getPlayerLimit());
                rep.put("players.names", net.md_5.bungee.api.ProxyServer.getInstance().getPlayers().stream().map(CommandSender::getName).collect(Collectors.toSet()));
            }

           SocketClient.reply(x, rep);
        });
    }


    public static ServerInfos get (String server) {
        final CompletableFuture<JSONObject> future = new CompletableFuture<>();
        SocketClient.send(server, "serverInfo", new HashMap<>())
                .callback(future::complete)
                .timeout((v0id) -> future.complete(null));
        return new ServerInfos(future.join());
    }


}
