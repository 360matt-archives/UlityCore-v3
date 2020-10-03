package fr.ulity.worldmanager.bukkit.mechanicEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class KeepSpawnInMemoryEvent implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void worldInit(org.bukkit.event.world.WorldInitEvent e) {
        e.getWorld().setKeepSpawnInMemory(false);
    }
}
