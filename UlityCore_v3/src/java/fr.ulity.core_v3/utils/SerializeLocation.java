package fr.ulity.core_v3.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializeLocation {
    public static String getSerializedLocation (Location loc) { //Converts location -> String
        return loc.getX() + ";"
             + loc.getY() + ";"
             + loc.getZ() + ";"
             + loc.getYaw() + ";"
             + loc.getPitch() + ";"
             + loc.getWorld().getName();
    }

    public static Location getDeserializedLocation (String s) { //Converts String -> Location
        String [] parts = s.split(";");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        float yaw = Float.parseFloat(parts[3]);
        float pitch = Float.parseFloat(parts[4]);
        World w = Bukkit.getServer().getWorld(parts[5]);
        return new Location(w, x, y, z, yaw, pitch); //can return null if the world no longer exists
    }
}
