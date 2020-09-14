package fr.ulity.core_v3.modules.datas;

import fr.ulity.core_v3.CoreSettings;
import fr.ulity.core_v3.modules.storage.Data;

import java.io.File;

public class IPData
        extends Data {
    public static File path = new File(CoreSettings.getDataPath().getPath() + "/ip");
    private final String ip;

    public IPData(String ip) {
        super(new File(path + File.separator + ip + ".json"));

        this.ip = ip;
    }
}