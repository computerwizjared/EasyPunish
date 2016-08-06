package net.jaredbates.EasyPunish.API;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Configuration {
    private File configFile;
    private FileConfiguration config;

    public Configuration(JavaPlugin plugin, String configName) {
        configFile = new File(plugin.getDataFolder() + File.separator + configName + ".yml");

        config = YamlConfiguration.loadConfiguration(configFile);
        try {
            configFile.createNewFile();
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public File getConfigFile() {
        return configFile;
    }
}
