package fr.ulity.core_v3.modules.datas;

import de.leonhard.storage.sections.FlatFileSection;
import fr.ulity.core_v3.utils.Time;

import java.util.Date;

public class UserCooldown {
    private final UserData userData;
    public String id;

    public UserCooldown (String playername, String id) {
        this.userData = new UserData(playername);
        this.id = id;
    }

    public UserCooldown (UserData userData, String id) {
        this.userData = userData;
        this.id = id;
    }

    public void applique (int seconds) {
        FlatFileSection section = this.userData.getSection("cooldown." + this.id);
        section.set("timestamp", new Date().getTime());
        section.set("expire", new Date().getTime() + (seconds * 1000));
    }

    public void applique (Time time) {
        FlatFileSection section = this.userData.getSection("cooldown." + this.id);
        section.set("timestamp", (new Date()).getTime());
        section.set("expire", new Date().getTime() + time.milliseconds);
    }

    public boolean isWaiting () { return getSecondLeft() != 0; }

    public void remove () {
        this.userData.remove("cooldown." + this.id);
    }

    public int getSecondLeft () {
        long expire = this.userData.getLong("cooldown." + this.id + ".expire");
        long now = new Date().getTime();
        if (expire <= (new Date()).getTime())
            return 0;
        return (int) (expire - now)/1000;
    }

    public Time getTimeLeft () {
        return new Time(getSecondLeft());
    }
}
