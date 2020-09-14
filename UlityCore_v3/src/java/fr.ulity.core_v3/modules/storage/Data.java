package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Json;
import fr.ulity.core_v3.Core;

import java.io.File;

public class Data extends Json {
    public Data() {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + "config.yml"));
    }

    public Data(String name) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + name + ".yml"));
    }

    public Data(String name, String path) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + path + File.separator + name + ".yml"));
    }

    public Data(File file) {
        super(file);
    }
}
