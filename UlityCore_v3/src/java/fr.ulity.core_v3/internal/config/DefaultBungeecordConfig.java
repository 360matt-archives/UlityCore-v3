package fr.ulity.core_v3.internal.config;

import fr.ulity.core_v3.Core;

public class DefaultBungeecordConfig {
    public static void apply() {
        Core.config.setDefault("bungeecord.enabled", false);
        Core.config.setDefault("bungeecord.socket_port", "6379");
    }
}
