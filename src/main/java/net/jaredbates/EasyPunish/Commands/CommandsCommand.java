package net.jaredbates.EasyPunish.Commands;

import net.jaredbates.EasyPunish.EasyPunish;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.GREEN;

public class CommandsCommand implements CommandExecutor {
    private EasyPunish plugin;

    public CommandsCommand(EasyPunish plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(AQUA + "Available offenses:");
        for (String offense : plugin.getConfig().getConfigurationSection("Offenses").getKeys(false)) {
            sender.sendMessage(GREEN + offense);
        }
        return true;
    }
}
