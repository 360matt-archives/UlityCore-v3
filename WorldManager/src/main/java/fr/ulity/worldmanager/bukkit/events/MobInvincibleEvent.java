package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobInvincibleEvent implements Listener {

    @EventHandler
    public static void onDamage (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity() instanceof Monster) {
                Flag<Boolean> flag = new WorldStructureConfig(e.getEntity().getWorld().getName()).flags.mobInvincible;
                if (!flag.getValue()) e.setCancelled(true);
            }
        }

    }
}