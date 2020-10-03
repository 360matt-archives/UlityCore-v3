package fr.ulity.worldmanager.bukkit.commands;

import de.leonhard.storage.util.FileUtils;
import fr.ulity.core_v3.modules.commandHandlers.DivisionBukkit;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.core_v3.modules.storage.ServerConfig;
import fr.ulity.worldmanager.api.WorldUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DeleteDivision extends DivisionBukkit {
    public DeleteDivision () {
        setLevel(0, "delete", "del");
        // must first arg = delete
    }

    // permission: WorldManager.delete

    @Override
    public void exec(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (requirePermission("WorldManager.delete")) {
            if (arg.require(1)) {
                if (WorldUtils.exist(arg.get(1))) { // first checking file
                    if (arg.requireWorld(1)) { // second checking loaded
                        new ServerConfig(arg.get(1), "worlds")
                                .getFile()
                                .delete();

                        Bukkit.unloadWorld(arg.get(1), false);

                        WorldUtils.delete(arg.get(1));

                        Lang.prepare("commands.wm.expressions.delete.success")
                                .variable("world", arg.get(1))
                                .sendPlayer(sender);
                    }
                } else {
                    Lang.prepare("commands.wm.expressions.delete.no_exist")
                            .variable("world", arg.get(1))
                            .sendPlayer(sender);
                }
            }
        }
    }
}
