package fr.ulity.worldmanager.bukkit.commands;

import fr.ulity.core_v3.modules.commandHandlers.DivisionBukkit;
import fr.ulity.core_v3.modules.language.Lang;
import fr.ulity.core_v3.utils.Text;
import fr.ulity.worldmanager.api.WorldUtils;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CreateDivision extends DivisionBukkit {
    Pattern seedPattern = Pattern.compile("\\[\\[([0-9]*)]]");

    public CreateDivision () {
        setLevel(0, "create");
    }

    // permission: WorldManager.create

    // Division
    public void exec(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (requirePermission("WorldManager.create")) {
            if (arg.require(1)) {
                if (arg.is(2)) {
                    if (!WorldUtils.exist(arg.get(1))) {
                        String attributs = Text.multiArg(args, 2).toLowerCase();

                        WorldStructureConfig.WorldProperties properties = new WorldStructureConfig.WorldProperties();

                        if (attributs.contains("void")) {
                            properties.type = WorldType.FLAT;
                            properties.isVoid = true;
                        }

                        if (attributs.contains("nether"))
                            properties.environment = World.Environment.NETHER;
                        else if (attributs.contains("end"))
                            properties.environment = World.Environment.THE_END;

                        if (attributs.contains("flat"))
                            properties.type = WorldType.FLAT;
                        else if (attributs.contains("amplified"))
                            properties.type = WorldType.AMPLIFIED;
                        else if (attributs.contains("large_biome")) // miss S
                            properties.type = WorldType.LARGE_BIOMES;


                        Matcher matcherSeed = seedPattern.matcher(attributs);
                        if (matcherSeed.find())
                            properties.seed = Long.parseLong(matcherSeed.group(1));

                        Lang.prepare("commands.wm.expressions.create.preparing")
                                .variable("world", arg.get(1))
                                .sendPlayer(sender);

                        new WorldStructureConfig(arg.get(1), properties);

                        Lang.prepare("commands.wm.expressions.create.success")
                                .variable("world", arg.get(1))
                                .sendPlayer(sender);

                    } else {
                        Lang.prepare("commands.wm.expressions.create.already_exist")
                                .variable("world", arg.get(1))
                                .sendPlayer(sender);
                    }
                } else
                    Lang.prepare("commands.wm.expressions.create.notice").sendPlayer(sender);
            }
        }
    }
}
