package fr.ulity.worldmanager.bukkit.commands;

import fr.ulity.core_v3.modules.commandHandlers.DivisionBukkit;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.worldmanager.api.WorldUtils;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ImportDivision extends DivisionBukkit {

    public ImportDivision () {
        setLevel(0, "import");
    }

    // permission: WorldManager.import

    // Division
    public void exec(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (requirePermission("WorldManager.import")) {
            if (arg.require(1)) {
                boolean exist = WorldUtils.exist(arg.get(1));
                if (exist)
                    new WorldStructureConfig(arg.get(1));

                Lang.prepare("commands.wm.expressions.import." + ((exist) ? "success" : "no_exist"))
                        .variable("world", arg.get(1))
                        .sendPlayer(sender);
            }
        }
    }
}
