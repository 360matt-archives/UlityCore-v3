package fr.ulity.core_v3.modules.loaders;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeLoader extends Plugin {
    public void registerEvent(Listener event) {
        ProxyServer.getInstance().getPluginManager().registerListener(this, event);
    }
}
