package fr.ulity.worldmanager.api;

import fr.ulity.core_v3.Core;


import java.io.File;
import java.util.Arrays;

public class WorldMan {
    private static final File worldsFolder = new File(Core.basePath.getAbsoluteFile() + "/servers/" + Core.servername + "/worlds");

    public static void reload () {
        if (!worldsFolder.exists())
            worldsFolder.mkdir();

        Arrays.stream(worldsFolder.listFiles()).forEach(x -> {
            String name = x.getName().replace(".yml", "");

            if (WorldUtils.exist(name))
                new WorldStructureConfig(name);
        });
    }
}
