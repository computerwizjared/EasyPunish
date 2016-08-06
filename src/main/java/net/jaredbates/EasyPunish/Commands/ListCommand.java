package net.jaredbates.EasyPunish.Commands;

import net.jaredbates.EasyPunish.EasyPunish;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class ListCommand implements CommandExecutor {
    EasyPunish plugin;

    public ListCommand(EasyPunish plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(BLUE + "Currently punished:");
        for (String uuid : plugin.getDataConfig().getConfig().getKeys(false)) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            for (String offense : plugin.getDataConfig().getConfig().getConfigurationSection(uuid + ".Punishments").getKeys(false)) {
                if (plugin.getDataConfig().getConfig().get(uuid + ".Punishments." + offense + ".Time") != null) {
                    if (plugin.getDataConfig().getConfig().getLong(uuid + ".Punishments." + offense + ".Time") < System.currentTimeMillis()) {
                        continue;
                    }
                }

                if (plugin.getDataConfig().getConfig().get(uuid + ".Punishments." + offense + ".Amount") != null) {
                    if (plugin.getDataConfig().getConfig().getInt(uuid + ".Punishments." + offense + ".Amount") == 0) {
                        continue;
                    }
                }

                sender.sendMessage(GREEN + player.getName() + " for " + offense);
            }
        }
        return true;
    }
}
