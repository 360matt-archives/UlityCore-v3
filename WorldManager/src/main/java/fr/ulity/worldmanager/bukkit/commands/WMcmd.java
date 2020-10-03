package fr.ulity.worldmanager.bukkit.commands;

import fr.ulity.core_v3.bukkit.BukkitAPI;
import fr.ulity.core_v3.modules.commandHandlers.CommandBukkit;
import fr.ulity.core_v3.modules.language.Lang;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class WMcmd extends CommandBukkit {
    public WMcmd() {
        super("wm");
        setAliases(Arrays.asList("m", "monde", "w", "world"));
        register(BukkitAPI.commandMap);

        addDivision(new ImportDivision());
        addDivision(new CreateDivision());
        addDivision(new TpDivision());
        addDivision(new DeleteDivision());
        addDivision(new SetspawnDivision());
    }

    @Override
    public void exec(CommandSender sender, String label, String[] args) {
        Lang.prepare("commands.wm.expressions.help")
                .variable("cmd", label)
                .sendPlayer(sender);
    }
}
