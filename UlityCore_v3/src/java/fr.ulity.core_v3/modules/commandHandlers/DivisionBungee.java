package fr.ulity.core_v3.modules.commandHandlers;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class DivisionBungee extends CommandBungee {
    public DivisionBungee() {
        super("§");
    }

    public final HashMap<Integer, Set<String>> requiredArgs = new HashMap<>();

    public void setLevel (int indice, String... argList) {
        requiredArgs.put(indice, Arrays.stream(argList).collect(Collectors.toSet()));
    }

    public abstract void exec(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
}
