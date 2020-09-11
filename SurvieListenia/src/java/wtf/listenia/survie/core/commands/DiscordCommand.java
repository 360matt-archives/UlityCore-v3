package wtf.listenia.survie.core.commands;

import fr.ulity.core_v3.modules.commandHandlers.CommandBukkit;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import wtf.listenia.survie.core.SurvieCore;

public class DiscordCommand extends CommandBukkit {
    public DiscordCommand() {
        super("discord");
        setDescription("§ePermet d'obtenir le lien d'invitation du Discord du serveur");
        setUsage("§eRécupérer l'invitation: §7/discord");
    }

    @Override
    public void exec (@NotNull CommandSender sender, @NotNull String s, @NotNull String[] strings) {
        sender.sendMessage("§eLe lien du Discord est: §6" + SurvieCore.config.getOrSetDefault("discord.invite", "discord.gg/"));
    }


}