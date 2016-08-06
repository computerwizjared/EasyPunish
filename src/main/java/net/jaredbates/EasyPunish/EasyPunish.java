package net.jaredbates.EasyPunish;

import net.jaredbates.EasyPunish.API.Configuration;
import net.jaredbates.EasyPunish.Commands.*;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyPunish extends JavaPlugin {
    private Permission permission;
    private Configuration dataConfig;
    private static EasyPunish instance;

    public void onEnable() {
        instance = this;

        // Dependencies
        setupPermissions();

        // Configurations
        saveDefaultConfig();
        dataConfig = new Configuration(this, "Data");

        // Commands
        getCommand("punish").setExecutor(new PunishCommand(this));
        getCommand("unpunish").setExecutor(new UnPunishCommand(this));
        getCommand("punishreload").setExecutor(new ReloadCommand(this));
        getCommand("punishlist").setExecutor(new ListCommand(this));
        getCommand("punishcmds").setExecutor(new CommandsCommand(this));
    }

    public Configuration getDataConfig() {
        return dataConfig;
    }

    public Permission getPermission() {
        return permission;
    }

    public static EasyPunish getInstance() {
        return instance;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
