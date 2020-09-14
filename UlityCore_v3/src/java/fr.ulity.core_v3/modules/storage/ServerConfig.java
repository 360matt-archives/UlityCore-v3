package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Yaml;
import fr.ulity.core_v3.Core;

import java.io.File;


public class ServerConfig extends Yaml {
    public ServerConfig() {
        super(new File(Core.serverPath.getAbsoluteFile() + File.separator + "config.yml"));
    }

    public ServerConfig(String name) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + Core.servername + File.separator + name + ".yml"));
    }

    public ServerConfig(String name, String path) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + Core.servername + File.separator + path + File.separator + name + ".yml"));
    }

    public ServerConfig(File file) {
        super(file);
    }
}
