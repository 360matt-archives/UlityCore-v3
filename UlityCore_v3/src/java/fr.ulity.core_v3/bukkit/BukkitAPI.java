package fr.ulity.core_v3.bukkit;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.storage.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Field;


public class BukkitAPI {
    public static JavaPlugin principalPlugin;

    public static JavaPlugin plugin;
    public static CommandMap commandMap;


    public static void init(JavaPlugin plugin) {
        BukkitAPI.plugin = plugin;

        if (principalPlugin == null)
            principalPlugin = plugin;

        Config serverConf = new Config(new File((new File(".")).getAbsolutePath() + File.separator + "plugins" + File.separator + "core.yml"));
        Core.basePath = new File(serverConf.getOrSetDefault("basePath", (new File(".")).getAbsolutePath() + File.separator + "core"));
        Core.serverPath = plugin.getDataFolder();

        Core.servername = serverConf.getOrSetDefault("servername", "changeMe");

        commandMap = getCommandMap();
    }


    public static CommandMap getCommandMap() {
        try {
            Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            f.setAccessible(true);
            return (CommandMap) f.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}