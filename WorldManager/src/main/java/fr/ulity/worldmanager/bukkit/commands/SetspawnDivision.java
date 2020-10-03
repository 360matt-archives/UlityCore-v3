package fr.ulity.worldmanager.bukkit.commands;

import fr.ulity.core_v3.modules.commandHandlers.DivisionBukkit;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SetspawnDivision extends DivisionBukkit {
    public SetspawnDivision () {
        setLevel(0, "setspawn", "sp");
        // must first arg = setspawn
    }

    // permission: WorldManager.setspawn

    @Override
    public void exec(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (requirePermission("WorldManager.setspawn")) {
            if (requirePlayer()) { // must be a player for get player location
                String worldName = getPlayer().getWorld().getName();
                WorldStructureConfig structure = new WorldStructureConfig(worldName);

                structure.datas.setSpawnLocation(getPlayer().getLocation());

                Lang.prepare("commands.wm.expressions.setspawn.success")
                        .variable("world", worldName)
                        .sendPlayer(sender);
            }
        }
    }
}