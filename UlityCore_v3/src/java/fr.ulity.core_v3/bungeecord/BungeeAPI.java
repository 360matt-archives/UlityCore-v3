package fr.ulity.core_v3.bungeecord;


import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.storage.Config;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;


public class BungeeAPI {
    public static Plugin principalPlugin;

    public static Plugin plugin;

    public static void init(Plugin plugin) {
        BungeeAPI.plugin = plugin;

        if (principalPlugin == null)
            principalPlugin = plugin;

        Config serverConf = new Config(new File((new File(".")).getAbsolutePath() + File.separator + "plugins" + File.separator + "core.yml"));
        Core.basePath = new File(serverConf.getOrSetDefault("basePath", (new File(".")).getAbsolutePath() + File.separator + "core"));
        Core.serverPath = plugin.getDataFolder();

        Core.servername = serverConf.getOrSetDefault("servername", "changeMe");

    }
}
