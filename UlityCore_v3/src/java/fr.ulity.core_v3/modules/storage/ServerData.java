package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Json;
import fr.ulity.core_v3.Core;

import java.io.File;


public class ServerData extends Json {
    public ServerData() {
        super(new File(Core.serverPath.getAbsoluteFile() + File.separator + "config.yml"));
    }

    public ServerData(String name) {
        super(new File(Core.serverPath.getAbsoluteFile() + File.separator + name + ".yml"));
    }

    public ServerData(String name, String path) {
        super(new File(Core.serverPath.getAbsoluteFile() + File.separator + path + File.separator + name + ".yml"));
    }

    public ServerData(File file) {
        super(file);
    }
}
