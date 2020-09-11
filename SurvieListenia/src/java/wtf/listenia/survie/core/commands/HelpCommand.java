package wtf.listenia.survie.core.commands;


import fr.ulity.core_v3.modules.commandHandlers.CommandBukkit;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandBukkit {
    public HelpCommand() {
        super("help");
        setDescription("§ePermet d'avoir de l'aide sur le serveur");
        setUsage("§eRecevoir de l'aide: §7/help");
    }

    @Override
    public void exec (CommandSender sender, String label, String[] args) {
        sender.sendMessage("§3Un dialogue avec un §aStaff §3vaut mieux qu'une simple commande !");
        sender.sendMessage("§3Un système de §aquestion-réponse §3intelligent arrivera bientôt !");
    }

}