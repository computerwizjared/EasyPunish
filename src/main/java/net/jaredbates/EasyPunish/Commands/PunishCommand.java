package net.jaredbates.EasyPunish.Commands;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import net.jaredbates.EasyPunish.EasyPunish;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;

import java.io.IOException;
import java.util.*;

import static org.bukkit.ChatColor.*;

public class PunishCommand implements CommandExecutor {
    private EasyPunish plugin;

    public PunishCommand(EasyPunish plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 1) {
            return false;
        }

        OfflinePlayer target = null;

        Profile[] profiles = new HttpProfileRepository("minecraft").findProfilesByNames(args[0]);

        if (profiles.length > 0) {
            target = plugin.getServer().getOfflinePlayer(UUID.fromString(profiles[0].getId().replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5")));
        }

        List<String> punishments = new ArrayList<>(plugin.getConfig().getStringList("Offenses." + args[1].toLowerCase() + ".Punishments"));

        if (target == null) {
            sender.sendMessage(RED + "That player does not exist! (Have they joined the server before, or has their username changed?)");
            return true;
        }

        try {
            plugin.getPermission().playerHas(plugin.getServer().getWorlds().get(0).getName(), target, "EasyPunish.UnPunishable");
        } catch (IllegalArgumentException e) {
            sender.sendMessage(RED + "That player does not exist! (Have they joined the server before, or has their username changed?)");
            return true;
        }

        if (!plugin.getPermission().playerHas(plugin.getServer().getWorlds().get(0).getName(), target, "EasyPunish.UnPunishable")) {
            sender.sendMessage(RED + "You cannot punish that player!");
            return true;
        }

        if (punishments.size() == 0) {
            sender.sendMessage(RED + "That punishment does not exist!");
            return true;
        }

        String prefix = target.getUniqueId() + ".Punishments." + args[1].toLowerCase() + ".";

        if (plugin.getDataConfig().getConfig().get(prefix + "Time") != null) {
            if (plugin.getDataConfig().getConfig().getLong(prefix + "Time") < System.currentTimeMillis()) {
                if (plugin.getDataConfig().getConfig().getInt(prefix + "Amount") == punishments.size()) {
                    plugin.getDataConfig().getConfig().set(prefix + "Amount", 0);
                    try {
                        plugin.getDataConfig().getConfig().save(plugin.getDataConfig().getConfigFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (plugin.getDataConfig().getConfig().get(prefix + "Amount") == null) {
            plugin.getDataConfig().getConfig().set(prefix + "Amount", 0);
            try {
                plugin.getDataConfig().getConfig().save(plugin.getDataConfig().getConfigFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (plugin.getDataConfig().getConfig().getInt(prefix + "Amount") < punishments.size()) {
            sender.sendMessage(BLUE + "Punished " + target.getName() + "!");
            String commandOriginal = punishments.get(plugin.getDataConfig().getConfig().getInt(prefix + "Amount")).replace("%p", target.getName());
            if (commandOriginal.contains("||")) {
                for (String command : commandOriginal.split("\\|\\|")) {
                    plugin.getServer().dispatchCommand(((plugin.getConfig().getString("CommandSender").equalsIgnoreCase("sender")) ? sender : plugin.getServer().getConsoleSender()), command);
                }
            } else {
                plugin.getServer().dispatchCommand(((plugin.getConfig().getString("CommandSender").equalsIgnoreCase("sender")) ? sender : plugin.getServer().getConsoleSender()), commandOriginal);
            }
            plugin.getDataConfig().getConfig().set(prefix + "Amount", plugin.getDataConfig().getConfig().getInt(prefix + "Amount") + 1);
            if (plugin.getConfig().getLong("Offenses." + args[1] + ".ExpireTime") == 0)
                plugin.getDataConfig().getConfig().set(prefix + "Time", System.currentTimeMillis() + (plugin.getConfig().getLong("Offenses." + args[1] + ".ExpireTime") * 60000));
            try {
                plugin.getDataConfig().getConfig().save(plugin.getDataConfig().getConfigFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            sender.sendMessage(RED + "That player has received maximum punishment for that offense!");
        }

        return true;
    }
}
