package wtf.listenia.survie.core.functions;

import fr.ulity.core_v3.modules.storage.ServerConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class GetSpawn {

    private static final ServerConfig serverConfig = new ServerConfig("spawn");

    public static Location getSpawnLocation () {
        World world = Bukkit.getWorld(serverConfig.getString("world"));

        double x = serverConfig.getDouble("x");
        double y = serverConfig.getDouble("y");
        double z = serverConfig.getDouble("z");
        float pitch = serverConfig.getFloat("pitch");
        float yaw = serverConfig.getFloat("yaw");

        return (world != null) ? new Location(world, x, y, z, yaw, pitch) : null;
    }

}
