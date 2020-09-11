package wtf.listenia.survie.core.events;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnProtectEvent implements Listener {

    @EventHandler
    private static void onPlace (BlockPlaceEvent e) {
        if (e.getBlock().getWorld().getName().equals("Spawn"))
            if (!e.getPlayer().hasPermission("grade.builder")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cRespecte le spawn s'il te plait !");
            }
    }

    @EventHandler
    private static void onBreak (BlockBreakEvent e) {
        if (e.getBlock().getWorld().getName().equals("Spawn"))
            if (!e.getPlayer().hasPermission("grade.builder")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§cRespecte le spawn s'il te plait !");
            }
    }

    @EventHandler
    private static void onTeleport (PlayerTeleportEvent e) {
        if (e.getTo().getWorld().getName().equals("Spawn"))
            if (!e.getPlayer().hasPermission("grade.staff"))
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    private static void onJoin (PlayerJoinEvent e) {
        if (e.getPlayer().getWorld().getName().equals("Spawn"))
            if (!e.getPlayer().hasPermission("grade.staff"))
                e.getPlayer().setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    private static void onDamage (EntityDamageEvent e) {
        if (e.getEntity().getWorld().getName().equals("Spawn"))
            e.setCancelled(true);
    }

    @EventHandler
    private static void on (EntitySpawnEvent e) {
        if (e.getEntity().getWorld().getName().equals("Spawn"))
            if (!e.getEntity().getType().equals(EntityType.ITEM_FRAME))
                 e.setCancelled(true);
    }

    @EventHandler
    private static void onFoodLevelChange (FoodLevelChangeEvent e) {
        if (e.getEntity().getWorld().getName().equals("Spawn"))
            e.setCancelled(true);
    }

    @EventHandler
    private static void onExplode (BlockIgniteEvent e) {
        if (e.getBlock().getWorld().getName().equals("Spawn"))
            e.setCancelled(true);
    }

}
