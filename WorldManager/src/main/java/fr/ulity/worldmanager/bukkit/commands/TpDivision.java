package fr.ulity.worldmanager.bukkit.commands;

import fr.ulity.core_v3.modules.commandHandlers.DivisionBukkit;
import fr.ulity.core_v3.modules.commandHandlers.bukkit.Status;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.regex.Pattern;

public class TpDivision extends DivisionBukkit {
    Pattern seedPattern = Pattern.compile("\\[\\[([0-9]*)]]");

    public TpDivision() {
        setLevel(0, "tp", "teleport");
        // must first arg = tp
    }

    // permission: WorldManager.tp
    // permission for tp other players : WorldManager.tp.others

    // Division
    public void exec(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (requirePermission("WorldManager.tp")) {
            if (arg.require(1)) {
                File mapRegions = new File("./" + arg.get(1) + "/uid.dat");
                if (mapRegions.exists()) {
                    if (requirePlayer()) {
                        Player player = (Player) sender;
                        if (arg.is(2) && requirePermission("WorldManger.tp.others") && arg.requirePlayer(2)) {
                            Lang.prepare("commands.wm.expresssions.tp.player_teleported")
                                    .variable("target", arg.get(2))
                                    .variable("world", arg.get(1))
                                    .sendPlayer(sender);
                            player = arg.getPlayer(2);
                        }

                        if (status.equals(Status.SUCCESS)) {
                            WorldStructureConfig structureConfig = new WorldStructureConfig(arg.get(1));
                            player.teleport(structureConfig.datas.getSpawnLocation());
                        }
                    }

                } else {
                    Lang.prepare("commands.wm.expressions.tp.no_exist")
                            .variable("world", arg.get(1))
                            .sendPlayer(sender);
                }
            }
        }
    }
}