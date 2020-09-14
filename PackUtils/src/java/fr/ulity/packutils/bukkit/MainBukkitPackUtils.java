package fr.ulity.packutils.bukkit;

import fr.ulity.packutils.bukkit.commands.economy.*;
import fr.ulity.packutils.bukkit.commands.gamemode.*;
import fr.ulity.packutils.bukkit.commands.players.*;
import fr.ulity.packutils.bukkit.commands.teleports.*;
import fr.ulity.packutils.bukkit.commands.time.DayCommand;
import fr.ulity.packutils.bukkit.commands.time.NightCommand;
import fr.ulity.packutils.bukkit.commands.weather.*;

import fr.ulity.core_v3.Core;
import fr.ulity.core_v3.modules.storage.ServerConfig;
import fr.ulity.packutils.bukkit.methods.EconomyMethods;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainBukkitPackUtils extends JavaPlugin {
    public static MainBukkitPackUtils plugin;
    public static ServerConfig config;

    public static CommandEnabler enabler;

    @Override
    public void onEnable() {
        plugin = this;
        Core.initialize(this);

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        getServer().getServicesManager().register(Economy.class, new EconomyMethods(), vault, ServicePriority.Highest);


        config = new ServerConfig("config");
        DefaultConfig.applique();

        enabler = new CommandEnabler();

            new GamemodeCommand();
            new GmcCommand();
            new GmsCommand();
            new GmaCommand();
            new GmpCommand();

            new FlyCommand();
            new TpCommand();
            new TpaCommand();
            new TpyesCommand();
            new TpnoCommand();
            new TopCommand();
            new BackCommand();
            new SetspawnCommand();
            new SpawnCommand();
            new SethomeCommand();
            new HomeCommand();
            new DelhomeCommand();

            new EcoCommand();
            new BalanceCommand();
            new PayCommand();

            new SunCommand();
            new RainCommand();
            new ThunderCommand();

            new DayCommand();
            new NightCommand();




    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
