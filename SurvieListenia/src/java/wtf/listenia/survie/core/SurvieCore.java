package wtf.listenia.survie.core;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.loaders.BukkitLoader;
import fr.ulity.core_v3.modules.storage.ServerConfig;
import wtf.listenia.survie.core.commands.*;
import wtf.listenia.survie.core.events.*;

public final class SurvieCore extends BukkitLoader {
    public static SurvieCore plugin;
    public static ServerConfig config;



    @Override
    public void onEnable() {
        plugin = this;
        Core.initialize(this);

        config = new ServerConfig("config");



        new DiscordCommand();
        new SiteCommand();
        new RetournerCommand();
        new HelpCommand();


        registerEvent(new DeathEvent());
        registerEvent(new OnWarzoneQuit());
        registerEvent(new SpawnProtectEvent());
        registerEvent(new SpawnMechanicEvent());



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
