package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.NoRepatingMessage;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpEvent implements Listener {

    @EventHandler
    public static void onPvP (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager().getType().equals(EntityType.PLAYER) && e.getEntity().getType().equals(EntityType.PLAYER)) {
                Player player = (Player) e.getDamager();

                Flag<Boolean> flag = new WorldStructureConfig(player.getWorld().getName()).flags.pvpAllowed;
                if (!flag.getValue()) {
                    if (!player.hasPermission(flag.getPermission())) {
                        e.setCancelled(true);
                        if (!flag.getMessage().equals("none") && NoRepatingMessage.canSend(player, "pvp"))
                            player.sendMessage(flag.getMessage());
                    }
                }
            }
        }
    }
}
