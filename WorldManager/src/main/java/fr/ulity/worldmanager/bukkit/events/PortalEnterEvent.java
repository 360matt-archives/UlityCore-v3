package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.core_v3.messaging.SocketClient;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.HashMap;

public class PortalEnterEvent implements Listener {

    @EventHandler
    public static void onPortal (PlayerPortalEvent e) {
        Player p = e.getPlayer();
        World w = p.getWorld();
        WorldStructureConfig structure = new WorldStructureConfig(p.getWorld().getName());

        Location portalLoc = p.getLocation().clone().add(0, 1, 0);
        while (portalLoc.getY() != 0) {
            final Material material = w.getBlockAt(portalLoc).getType();
            if (!material.equals(Material.NETHER_PORTAL)) {
                structure.worldConf.keySet("portals").forEach(server -> {
                    if (structure.worldConf.getList("portals." + server).contains(material.name())) {
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("server", server);
                        data.put("player", p.getName());
                        SocketClient.send("bungeecord", "tpServer", data);
                    }
                });
                break;
            }
            portalLoc.subtract(0, 1, 0);
        }
    }
}
