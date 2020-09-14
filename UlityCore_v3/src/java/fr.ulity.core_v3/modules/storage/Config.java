package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Yaml;
import fr.ulity.core_v3.Core;

import java.io.File;

public class Config extends Yaml {
    public Config() {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + "config.yml"));
    }

    public Config(String name) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + name + ".yml"));
    }

    public Config(String name, String path) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + path + File.separator + name + ".yml"));
    }

    public Config(File file) {
        super(file);
    }
}

