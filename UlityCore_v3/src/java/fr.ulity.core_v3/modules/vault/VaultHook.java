package fr.ulity.core_v3.modules.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicesManager;

public class VaultHook {
    private static final ServicesManager srvman = Bukkit.getServicesManager();

    public static boolean isSupported () {
        return Bukkit.getPluginManager().getPlugin("Vault") != null;
    }


    public static net.milkbowl.vault.chat.Chat getChat () { return srvman.getRegistration(net.milkbowl.vault.chat.Chat.class).getProvider(); }
    public static net.milkbowl.vault.permission.Permission getPermission () { return srvman.getRegistration(net.milkbowl.vault.permission.Permission.class).getProvider(); }
    public static net.milkbowl.vault.economy.Economy getEco () { return srvman.getRegistration(net.milkbowl.vault.economy.Economy.class).getProvider(); }

    public static boolean isChat () { return isSupported() && (srvman.getRegistration(net.milkbowl.vault.chat.Chat.class) != null); }
    public static boolean isPermission () { return isSupported() && (srvman.getRegistration(net.milkbowl.vault.permission.Permission.class) != null); }
    public static boolean isEco () { return isSupported() && (srvman.getRegistration(net.milkbowl.vault.economy.Economy.class) != null); }











}
