package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class AnimalSpawnEvent implements Listener {
    @EventHandler
    public static void onSpawn (EntitySpawnEvent e) {
        if (!e.isCancelled()) {
            if (e.getEntity() instanceof Animals) {
                Flag<Boolean> flag = new WorldStructureConfig(e.getEntity().getWorld().getName()).flags.animalSpawn;
                if (!flag.getValue()) e.setCancelled(true);
            }
        }
    }
}