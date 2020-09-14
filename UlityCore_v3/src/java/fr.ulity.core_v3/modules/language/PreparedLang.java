package fr.ulity.core_v3.modules.language;

import fr.ulity.core_v3.utils.Text;

import java.util.HashMap;
import java.util.Map;

public class PreparedLang {
    private final String exp;
    private final HashMap<String, String> vars = new HashMap<>();
    private String prefix = "";
    private String suffix = "";

    public PreparedLang(String exp) {
        this.exp = exp;
    }

    public PreparedLang prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public PreparedLang suffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public PreparedLang variable(String name, String replacement) {
        this.vars.put(name, replacement);
        return this;
    }

    public void sendPlayer(org.bukkit.entity.Player player) {
        player.sendMessage(getOutput(player));
    }

    public void sendPlayer(org.bukkit.command.CommandSender player) {
        player.sendMessage(getOutput(player));
    }

    public void sendPlayer(net.md_5.bungee.api.CommandSender player) {
        player.sendMessage(getOutput(player));
    }

    public String getOutput(Object lang) {
        String output = Text.getColored(Text.getConverted(Lang.getPreferedLang(lang).getString(this.exp)));

        for (Map.Entry<String, String> x : this.vars.entrySet())
            output = output.replaceAll("%" + x.getKey() + "%", x.getValue());
        return this.prefix + output + this.suffix;
    }

    public String getOutput() {
        return getOutput(Lang.getDefaulLang());
    }
}
