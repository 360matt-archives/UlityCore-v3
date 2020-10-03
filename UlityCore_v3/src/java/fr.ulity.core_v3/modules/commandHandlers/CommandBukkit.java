package fr.ulity.core_v3.modules.commandHandlers;

import fr.ulity.core_v3.bukkit.BukkitAPI;
import fr.ulity.core_v3.modules.commandHandlers.bukkit.ArgBukkit;
import fr.ulity.core_v3.modules.commandHandlers.bukkit.Status;
import fr.ulity.core_v3.modules.commandHandlers.bukkit.TabCommandBukkit;
import fr.ulity.core_v3.modules.datas.UserCooldown;
import fr.ulity.core_v3.modules.language.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class CommandBukkit extends BukkitCommand implements Listener {
    private final HashMap<Integer, ArrayList<TabCommandBukkit>> tabComplete;
    private final List<DivisionBukkit> divisions = new ArrayList<>();

    public CommandSender sender;
    public Command cmd;
    public String[] args;
    public ArgBukkit arg;
    public UserCooldown cooldown;
    public Status status = Status.SUCCESS;

    public CommandBukkit(@NotNull String name) {
        super(name);
        this.tabComplete = new HashMap<>();
        BukkitAPI.getCommandMap().register(getName(), this);
    }

    public boolean execDivision (CommandSender sender, String label, String[] args) {
        AtomicBoolean stat = new AtomicBoolean(false);

        divisions.forEach(x -> {
            int count = 0;
            for (String y : args) {
                if (x.requiredArgs.containsKey(count)) {
                    if (!x.requiredArgs.get(count).contains(y))
                        break;
                    count++;
                    if (args.length >= count) {
                        stat.set(true);
                        x.execute(sender, label, args);
                    }
                } else break;
            }
        });
        return stat.get();
    }

    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        this.sender = sender;
        this.cmd = this;
        this.args = args;
        this.arg = new ArgBukkit(sender, this.cmd, args);
        this.cooldown = new UserCooldown(sender.getName(), getName());

        if (!execDivision(sender, label, args)) {
            if (getUsage().equals("/" + getName()))
                setUsage(Lang.get("commands." + getName() + ".usage"));
            exec(sender, label, args);
            operateStatus();
        }
        return true;
    }

    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    public Player getPlayer() {
        return (Player) this.sender;
    }

    public boolean requirePlayer() {
        if (isPlayer())
            return true;
        this.status = Status.PLAYER_ONLY;
        return false;
    }

    public boolean hasPermission(String permission) {
        return this.sender.hasPermission(permission);
    }

    public boolean requirePermission(String permission) {
        if (hasPermission(permission))
            return true;
        this.status = Status.NOPERM;
        return false;
    }

    private void operateStatus() {
        if (this.status.equals(Status.PLAYER_ONLY))
            Lang.prepare("global.player_only").sendPlayer(this.sender);
        else if (this.status.equals(Status.SYNTAX))
            this.sender.sendMessage(ChatColor.RED + getUsage());
        else if (this.status.equals(Status.NOPERM))
            Lang.prepare("global.no_perm").sendPlayer(this.sender);
        this.status = Status.SUCCESS;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addDivision (DivisionBukkit div) { divisions.add(div); }
    public void removeDivision (DivisionBukkit div) { divisions.remove(div); }






    public @NotNull List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        int indice = args.length - 1;
        if ((getPermission() != null && !sender.hasPermission(getPermission())) || this.tabComplete.size() == 0 || !this.tabComplete.containsKey(Integer.valueOf(indice)))
            return super.tabComplete(sender, alias, args);
        List<String> list = this.tabComplete.get(indice).stream().filter(tabCommand -> (tabCommand.getTextAvant() == null || tabCommand.getTextAvant().contains(args[indice - 1]))).filter(tabCommand -> (tabCommand.getPermission() == null || sender.hasPermission(tabCommand.getPermission()))).filter(tabCommand -> tabCommand.getText().startsWith(args[indice])).map(TabCommandBukkit::getText).sorted(String.CASE_INSENSITIVE_ORDER).collect(Collectors.toList());
        return (list.size() < 1) ? super.tabComplete(sender, alias, args) : list;
    }

    public void addTabbComplete(int indice, String permission, String[] beforeText, String... args) {
        if (args != null && args.length > 0 && indice >= 0)
            if (this.tabComplete.containsKey(indice))
                this.tabComplete.get(indice).addAll(Arrays.stream(args).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommandBukkit(indice, s, permission, beforeText)), ArrayList::addAll));
            else
                this.tabComplete.put(indice, Arrays.stream(args).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommandBukkit(indice, s, permission, beforeText)), ArrayList::addAll));
    }

    public CommandBukkit addArrayTabbComplete(int indice, String permission, String[] beforeText, String[] args) {
        if (args != null && args.length > 0 && indice >= 0)
            if (this.tabComplete.containsKey(indice))
                this.tabComplete.get(indice).addAll(Arrays.stream(args).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommandBukkit(indice, s, permission, beforeText)), ArrayList::addAll));
            else
                this.tabComplete.put(indice, Arrays.stream(args).collect(ArrayList::new, (tabCommands, s) -> tabCommands.add(new TabCommandBukkit(indice, s, permission, beforeText)), ArrayList::addAll));
        return this;
    }

    protected CommandBukkit addListTabbComplete(int indice, String permission, String[] beforeText, String... arg) {
        return addArrayTabbComplete(indice, permission, beforeText, arg);
    }

    public void addTabbComplete(int indice, String... arg) {
        addTabbComplete(indice, null, null, arg);
    }

    public abstract void exec(@NotNull CommandSender paramCommandSender, @NotNull String paramString, @NotNull String[] paramArrayOfString);
}