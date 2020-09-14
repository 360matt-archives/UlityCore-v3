package fr.ulity.core_v3.internal.config;

import fr.ulity.core_v3.Core;

public class DefaultCommonConfig {
    public static void apply() {
        Core.config.setHeader("### ### ### ### ### ### ### ### ### ### ### ", "### ", "### UlityCore: a reliable network Core ", "### ", "### When you use Bungeecord / Waterfall, ", "### Please enable Bungeecord mod, setting a socket port ( ... and set your firewall ) ", " ");
        Core.config.setDefault("global.lang", "fr");
    }
}