package fr.ulity.superrtp.bukkit;

import fr.ulity.superrtp.bukkit.commands.CommandRTP;
import fr.ulity.superrtp.bukkit.events.InventoryEventRTP;
import fr.ulity.superrtp.bukkit.events.InvincibleRTP;
import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.loaders.BukkitLoader;
import fr.ulity.core_v3.modules.storage.ServerConfig;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;


import java.util.HashMap;

import static org.bukkit.Bukkit.getPluginManager;

public final class MainBukkitRTP extends BukkitLoader {
    public static MainBukkitRTP plugin;
    public static ServerConfig config;
    public static HashMap<String, String> items = new HashMap<>();

    public static HashMap<String, HashMap<String, Object>> invincible = new HashMap<>();


    @Override
    public void onEnable() {
        plugin = this;
        Core.initialize(this);

        config = new ServerConfig("config", "SuperRTP");
        ConfigCopy.setDefault();

        new CommandRTP();

        getPluginManager().registerEvents(new InventoryEventRTP(), this);
        getPluginManager().registerEvents(new InvincibleRTP(), this);

    }

    @Override
    public void onDisable() { }
}