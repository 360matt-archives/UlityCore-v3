package fr.ulity.core_v3.modules.storage;

import de.leonhard.storage.Json;
import fr.ulity.core_v3.Core;

import java.io.File;


public class ServerData extends Json {
    public ServerData() {
        super(new File(Core.serverPath.getAbsoluteFile() + File.separator + "config.yml"));
    }

    public ServerData(String name) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + "servers" + File.separator + Core.servername + File.separator + name + ".json"));
    }

    public ServerData(String name, String path) {
        super(new File(Core.basePath.getAbsoluteFile() + File.separator + "servers" + File.separator + Core.servername + File.separator + path + File.separator + name + ".json"));
    }

    public ServerData(File file) {
        super(file);
    }
}
