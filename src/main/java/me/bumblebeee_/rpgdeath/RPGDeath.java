package me.bumblebeee_.rpgdeath;

import lombok.Getter;
import me.bumblebeee_.rpgdeath.commands.MortoCommand;
import me.bumblebeee_.rpgdeath.commands.ReloadCommand;
import me.bumblebeee_.rpgdeath.commands.ResuscitaCommand;
import me.bumblebeee_.rpgdeath.commands.TrovaanimaCommand;
import me.bumblebeee_.rpgdeath.listeners.*;
import me.bumblebeee_.rpgdeath.util.Cooldowns;
import me.bumblebeee_.rpgdeath.util.GhostFactory;
import me.bumblebeee_.rpgdeath.util.Runnables;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RPGDeath extends JavaPlugin {

    private static @Getter Plugin instance;
    public static @Getter Permission permission;
    private static @Getter GhostFactory ghosts;

    Runnables runnables = new Runnables();
    Cooldowns cooldowns = new Cooldowns();

    //TODO
    //Compass

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        setupPermissions();
        registerEvents();
        registerCommands();

        ghosts = new GhostFactory(this);
        runnables.ghosts();
        runnables.compassUpdater();
        cooldowns.savaData();
    }

    @Override
    public void onDisable() {
        cooldowns.loadData();
    }

    public void registerEvents() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPortal(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerRespawn(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new FoodLevelChange(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    public void registerCommands() {
        Bukkit.getServer().getPluginCommand("morto").setExecutor(new MortoCommand());
        Bukkit.getServer().getPluginCommand("resuscita").setExecutor(new ResuscitaCommand());
        Bukkit.getServer().getPluginCommand("trovaanima").setExecutor(new TrovaanimaCommand());
        Bukkit.getServer().getPluginCommand("death").setExecutor(new ReloadCommand());
    }

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
