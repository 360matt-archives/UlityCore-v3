package fr.ulity.core_v3.modules.commandHandlers.bukkit;

import fr.ulity.core_v3.modules.language.Lang;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;


public class ArgBukkit {
    private final CommandSender sender;
    private final Command cmd;
    private final String[] args;

    public ArgBukkit(CommandSender sender, Command command, String[] args) {
        this.sender = sender;
        this.cmd = command;
        this.args = args;
    }

    public boolean is(int ind) {
        return (this.args.length >= ind + 1);
    }

    public boolean isNumber(int ind) {
        try {
            if (is(ind)) {
                Double.parseDouble(this.args[ind]);
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    public boolean isPlayer(int ind) {
        return (is(ind) && Bukkit.getPlayer(this.args[ind]) != null);
    }

    public boolean isWorld(int ind) {
        return (is(ind) && Bukkit.getWorld(this.args[ind]) != null);
    }

    public boolean isAlphaNumeric(int ind) {
        return (is(ind) && StringUtils.isAlphanumeric(this.args[ind]));
    }

    public String get(int ind) {
        return is(ind) ? this.args[ind] : "";
    }

    public long getLong(int ind) {
        return is(ind) ? Long.parseLong(this.args[ind]) : 0L;
    }

    public Player getPlayer(int ind) {
        return is(ind) ? Bukkit.getPlayer(this.args[ind]) : null;
    }

    public World getWorld(int ind) {
        return is(ind) ? Bukkit.getWorld(this.args[ind]) : null;
    }

    public boolean require(int ind) {
        if (is(ind)) return true;
        Lang.prepare("arg_needed.default").sendPlayer(this.sender);
        return false;
    }

    public boolean requirePlayer(int ind) {
        if (isPlayer(ind)) return true;
        if (is(ind)) {
            Lang.prepare("global.invalid_player")
                    .variable("player", get(ind))
                    .sendPlayer(this.sender);
        } else
            Lang.prepare("arg_needed.player").sendPlayer(this.sender);
        return false;
    }

    public boolean requireWorld(int ind) {
        if (isWorld(ind)) return true;
        if (is(ind)) {
            Lang.prepare("global.invalid_world")
                    .variable("world", get(ind))
                    .sendPlayer(this.sender);
        } else
            Lang.prepare("arg_needed.world").sendPlayer(this.sender);

        return false;
    }

    public boolean requirePlayerNoSelf(int ind) {
        if (requirePlayer(ind)) {
            if (!getPlayer(ind).getName().equals(this.sender.getName())) return true;
            Lang.prepare("global.no_self").sendPlayer(this.sender);
        }
        return false;
    }

    public boolean requireNumber(int ind) {
        if (isNumber(ind)) return true;
        Lang.prepare("arg_needed.number").sendPlayer(this.sender);
        return false;
    }

    public boolean inRange(int minimal, int maximal) {
        return (this.args.length <= maximal && this.args.length >= minimal);
    }

    public boolean compare(String source, String... target) {
        return Arrays.stream(target).anyMatch(source::equalsIgnoreCase);
    }

    public boolean compare(int ind, String... target) {
        return Arrays.stream(target).anyMatch(get(ind)::equalsIgnoreCase);
    }
}
