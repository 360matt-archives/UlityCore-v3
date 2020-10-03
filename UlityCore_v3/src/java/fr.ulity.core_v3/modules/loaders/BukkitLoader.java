package fr.ulity.core_v3.modules.loaders;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin {
    public void registerEvent(Listener event) {
        Bukkit.getPluginManager().registerEvents(event, this);
    }
    public void unregisterEvent (Listener event) { HandlerList.unregisterAll(event); }

}