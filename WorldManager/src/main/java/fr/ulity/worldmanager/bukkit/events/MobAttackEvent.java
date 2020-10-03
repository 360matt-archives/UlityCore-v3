package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobAttackEvent implements Listener {

    @EventHandler
    public static void onAttack (EntityDamageByEntityEvent e) {
        if (!e.isCancelled()) {
            if (e.getDamager() instanceof Monster) {
                Flag<Boolean> flag = new WorldStructureConfig(e.getDamager().getWorld().getName()).flags.mobAttack;
                if (!flag.getValue()) e.setCancelled(true);
            }
        }

    }
}