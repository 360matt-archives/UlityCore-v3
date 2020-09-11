package wtf.listenia.survie.core.events;

import fr.ulity.core_v3.messaging.SocketClient;
import wtf.listenia.survie.core.SurvieCore;
import wtf.listenia.survie.core.functions.GetSpawn;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;


public class SpawnMechanicEvent implements Listener {

    @EventHandler (priority = EventPriority.LOW)
    private static void onMove (PlayerMoveEvent e) {

        if (e.getPlayer().getWorld().getName().equals("Spawn")) {
            Material avant2 = e.getFrom().clone().subtract(0, 2, 0).getBlock().getType();
            Material apres2 = e.getTo().clone().subtract(0, 2, 0).getBlock().getType();

            if (!avant2.equals(apres2) && apres2.equals(Material.END_PORTAL)) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SurvieCore.plugin, () -> {
                    e.getPlayer().performCommand("rtp");
                }, 5);
            }


        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e){
        if (e.getPlayer().getWorld().getName().equals("Spawn")) {
            if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL)) {
                e.setCancelled(true);

                e.getPlayer().teleport(GetSpawn.getSpawnLocation());

                Bukkit.getScheduler().runTaskLaterAsynchronously(SurvieCore.plugin, () -> {
                    HashMap<String, Object> datas = new HashMap<>();
                    datas.put("player", e.getPlayer().getName());
                    datas.put("server", "hub");

                    SocketClient.send("bungeecord", "tpServer", datas);
                }, 5);

            }
            else if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL))
                e.setCancelled(true);
        }
    }




}
