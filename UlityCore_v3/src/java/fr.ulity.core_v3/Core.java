package fr.ulity.core_v3;

import de.leonhard.storage.internal.settings.ConfigSettings;
import fr.ulity.core_v3.bukkit.BukkitAPI;
import fr.ulity.core_v3.bungeecord.BungeeAPI;
import fr.ulity.core_v3.internal.config.DefaultBungeecordConfig;
import fr.ulity.core_v3.internal.config.DefaultCommonConfig;
import fr.ulity.core_v3.messaging.defaults.SocketExpressions;
import fr.ulity.core_v3.messaging.SocketClient;
import fr.ulity.core_v3.messaging.SocketServer;
import fr.ulity.core_v3.messaging.events.ServStatSockEvent;
import fr.ulity.core_v3.messaging.events.TPserverSockEvent;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.core_v3.modules.networking.ServerInfos;
import fr.ulity.core_v3.modules.storage.Config;
import fr.ulity.core_v3.modules.storage.Temp;
import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Core {
    public static ServerApiType type;
    public static File basePath;
    public static File serverPath;
    public static Config config;
    public static String servername;
    public static Temp temp;

    public static void initialize(JavaPlugin plugin) {
        type = ServerApiType.BUKKIT;
        BukkitAPI.init(plugin);
        commonInit();
        ServStatSockEvent.bukkit();
    }

    public static void initialize(Plugin plugin) {
        type = ServerApiType.BUNGEE;
        BungeeAPI.init(plugin);
        commonInit();
        ServStatSockEvent.bungee();
        TPserverSockEvent.registerBungee();
    }

    private static void commonInit() {
        config = new Config();
        config.setConfigSettings(ConfigSettings.PRESERVE_COMMENTS);

        DefaultCommonConfig.apply();
        DefaultBungeecordConfig.apply();

        Lang.reload();

        try {
            temp = new Temp();

            SocketExpressions.change();
            if (type.equals(ServerApiType.BUNGEE)) {
                SocketServer.start();
            }
            SocketClient.start();

            ServerInfos.registerListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public enum ServerApiType {
        BUKKIT, BUNGEE;
    }
}