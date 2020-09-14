package fr.ulity.core_v3;

import java.io.File;

public class CoreSettings {
    private static File dataPath = new File(Core.basePath.getPath() + "/data");

    public static File getDataPath() {
        return dataPath;
    }

    public static void setDataPath(File dataPath) {
        CoreSettings.dataPath = dataPath;
    }
}