package fr.ulity.core_v3.modules.language;

import fr.ulity.core_v3.modules.vault.VaultHook;
import fr.ulity.core_v3.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import fr.ulity.core_v3.modules.networking.IntervalServerInfos;
import fr.ulity.core_v3.modules.networking.ServerInfos;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TranslateVars {
    public static String getServerVersion () { return Bukkit.getServer().getClass().getPackage().getName().substring(23); }

    public static int getPlayerPing (Player player) {
        try {
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + getServerVersion() + ".entity.CraftPlayer");
            Object converted = craftPlayer.cast(player);
            Method handle = converted.getClass().getMethod("getHandle");
            Object entityPlayer = handle.invoke(converted);
            Field pingField = entityPlayer.getClass().getField("ping");
            return pingField.getInt(entityPlayer);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private final List<String> input;
    private HashMap<String, Object> vars = new HashMap<>();
    private Player player;

    public TranslateVars (String text) { input = Collections.singletonList(text); }
    public TranslateVars (List<String> text) { input = text; }

    public TranslateVars addVariables (HashMap<String, Object> inVars) { vars = inVars; return this; }
    public TranslateVars addPlayer (Player inPlayer) { player = inPlayer; return this; }

    private List<String> build () {
        vars.put("%online%", Bukkit.getOnlinePlayers().size());
        vars.put("%max_online%", Bukkit.getMaxPlayers());

        if (player != null) {
            vars.put("%world%", player.getWorld().getName());
            vars.put("%ping%", getPlayerPing(player));

            if(VaultHook.isSupported()) {
                if (VaultHook.isEco())
                    vars.put("%money%", Math.round(VaultHook.getEco().getBalance(player) * 100.0) / 100.0);
                if (VaultHook.isPermission())
                    vars.put("%group%", VaultHook.getPermission().getPrimaryGroup(player));
            }
        }

        Text pregenText = new Text(input).setColored().setEncoded();

        Pattern socketPattern = Pattern.compile("%sock_([A-Za-z0-9]*)_(.*)%");
        Matcher matcher = socketPattern.matcher(pregenText.outputString());
        while (matcher.find()) {
            ServerInfos serverInfos = IntervalServerInfos.get(matcher.group(1));
            vars.put("%sock_" + matcher.group(1) + "_player_count%", serverInfos.players.count);
            vars.put("%sock_" + matcher.group(1) + "_player_max%", serverInfos.players.max);
        }



        return pregenText.outputList()
                .stream()
                .map(x -> {
                    for (Map.Entry<String, Object> y : vars.entrySet())
                        x = x.replaceAll(y.getKey(), y.getValue().toString());
                    return x;
                })
                .collect(Collectors.toList());

    }

    public List<String> outputList () { return new Text(build()).setEncoded().setColored().outputList(); }
    public String outputString () { return new Text(build()).setEncoded().setColored().setNewLine().outputString(); }


}
