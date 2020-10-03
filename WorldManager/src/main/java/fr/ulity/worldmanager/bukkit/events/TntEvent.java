package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class TntEvent implements Listener {

    @EventHandler
    public static void onTnt (EntitySpawnEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity().getType().equals(EntityType.PRIMED_TNT) && e.getEntity().getType().equals(EntityType.MINECART_TNT)) {
                Flag<Boolean> flag = new WorldStructureConfig(e.getEntity().getWorld().getName()).flags.placeAllowed;
                if (!flag.getValue()) e.setCancelled(true);
            }
        }

    }
}
