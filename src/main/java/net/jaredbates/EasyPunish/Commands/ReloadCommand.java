package net.jaredbates.EasyPunish.Commands;

import net.jaredbates.EasyPunish.EasyPunish;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.GREEN;

public class ReloadCommand implements CommandExecutor {
    private EasyPunish plugin;

    public ReloadCommand(EasyPunish plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(GREEN + "Reloaded Configuration!");
        return true;
    }
}
