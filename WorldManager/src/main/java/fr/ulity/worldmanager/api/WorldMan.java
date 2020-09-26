package fr.ulity.worldmanager.api;

import fr.ulity.core_v3.Core;
import org.bukkit.World;
import org.bukkit.WorldCreator;


import java.io.File;
import java.util.Arrays;

public class WorldMan {
    private static final File worldsFolder = new File(Core.basePath.getAbsoluteFile() + "/servers/" + Core.servername + "/worlds");

    public static void reload () {
        if (!worldsFolder.exists())
            worldsFolder.mkdir();

        Arrays.stream(worldsFolder.listFiles()).forEach(x -> {
            String name = x.getName().replace(".yml", "");

            World world = new WorldCreator(name).createWorld();

            WorldStructureConfig woConf = new WorldStructureConfig(world);




        });

    }
}
