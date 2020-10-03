package fr.ulity.worldmanager.api;

import fr.ulity.core_v3.Core;
import org.bukkit.entity.Player;

import java.util.Date;

public class NoRepatingMessage {
    public static boolean canSend (Player player, String id) {
        String path = "players" + player.getName() + ".norepeat." + id;
        Long now = new Date().getTime();
        if (Core.temp.getOrSetDefault(path, now) >= now) {
            Core.temp.set(path, now + 2000);
            return true;
        } else return false;
    }
}
