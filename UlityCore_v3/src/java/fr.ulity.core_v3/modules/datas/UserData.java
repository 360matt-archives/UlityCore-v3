package fr.ulity.core_v3.modules.datas;

import fr.ulity.core_v3.CoreSettings;
import fr.ulity.core_v3.modules.storage.Data;

import java.io.File;

public class UserData extends Data {
    public static File path = new File(CoreSettings.getDataPath().getPath() + "/players");
    private final String playername;

    public UserData(String playername) {
        super(new File(path + File.separator + playername + ".json"));
        this.playername = playername;
    }

    public UserCooldown getCooldown(String id) {
        return new UserCooldown(this.playername, id);
    }
}
