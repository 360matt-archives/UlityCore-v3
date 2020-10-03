package fr.ulity.worldmanager.bukkit.events;

import fr.ulity.worldmanager.api.Flag;
import fr.ulity.worldmanager.api.NoRepatingMessage;
import fr.ulity.worldmanager.api.WorldStructureConfig;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceEvent implements Listener {

    @EventHandler
    public static void onPlace (BlockPlaceEvent e) {
        if (!e.isCancelled()) {
            Player player = e.getPlayer();
            World world = player.getWorld();

            Flag<Boolean> flag = new WorldStructureConfig(world.getName()).flags.placeAllowed;
            if (!flag.getValue()) {
                if (!player.hasPermission(flag.getPermission())) {
                    e.setCancelled(true);
                    if (!flag.getMessage().equals("none") && NoRepatingMessage.canSend(player, "place"))
                        player.sendMessage(flag.getMessage());
                }
            }
        }
    }
}
