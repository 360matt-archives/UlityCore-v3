package fr.ulity.worldmanager.bukkit;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.loaders.BukkitLoader;
import fr.ulity.worldmanager.api.WorldMan;
import fr.ulity.worldmanager.bukkit.commands.ImportDivision;
import fr.ulity.worldmanager.bukkit.commands.WMcmd;
import fr.ulity.worldmanager.bukkit.events.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public final class WorldManagerBukkit extends BukkitLoader implements Listener {
    public static WorldManagerBukkit plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Core.initialize(this);


        registerEvent(new AnimalAttackEvent());
        registerEvent(new AnimalInvincibleEvent());
        registerEvent(new AnimalSpawnEvent());

        registerEvent(new MobAttackEvent());
        registerEvent(new MobInvincibleEvent());
        registerEvent(new MobSpawnEvent());

        registerEvent(new PlaceEvent());
        registerEvent(new BrokeEvent());
        registerEvent(new PvpEvent());
        registerEvent(new TntEvent());

        registerEvent(new PortalEnterEvent());


        new WMcmd();
        new ImportDivision();

        // reloading all
        WorldMan.reload();

    }

    @EventHandler
    public void onQuit (PlayerQuitEvent e) {
        Core.temp.remove("players." + e.getPlayer().getName());
    }

}
