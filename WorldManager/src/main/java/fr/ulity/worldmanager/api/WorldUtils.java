package fr.ulity.worldmanager.api;

import java.io.File;
import java.util.Arrays;

public class WorldUtils {
    public static boolean exist (String worldname) {
        return new File("./" + worldname + "/uid.dat").exists();
    }

    public static void delete (String worldname) {
        File folder = new File("./" + worldname);
        deleteRecursively(folder);
        folder.delete();
    }

    public static void deleteRecursively (File folder) {
        Arrays.stream(folder.listFiles()).forEach(x -> {
            if (x.isDirectory())
                deleteRecursively(x);
            x.delete();
        });
    }
}
