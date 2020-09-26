package fr.ulity.worldmanager.bukkit;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.loaders.BukkitLoader;
import fr.ulity.worldmanager.api.WorldMan;

public final class WorldManagerBukkit extends BukkitLoader {
    public static WorldManagerBukkit plugin;

    @Override
    public void onEnable() {
        plugin = this;
        Core.initialize(this);

        // reloading all
        WorldMan.reload();

    }

}
