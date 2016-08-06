package net.jaredbates.EasyPunish.Commands;

import com.mojang.api.profiles.HttpProfileRepository;
import com.mojang.api.profiles.Profile;
import net.jaredbates.EasyPunish.EasyPunish;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class UnPunishCommand implements CommandExecutor {
    private EasyPunish plugin;

    public UnPunishCommand(EasyPunish plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 1) {
            OfflinePlayer target = null;

            Profile[] profiles = new HttpProfileRepository("minecraft").findProfilesByNames(args[0]);

            if (profiles.length > 0) {
                target = plugin.getServer().getOfflinePlayer(UUID.fromString(profiles[0].getId().replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5")));
            }

            ArrayList<String> punishments = new ArrayList<>(plugin.getConfig().getStringList("Offenses." + args[1].toLowerCase() + ".Punishments"));

            if (target != null) {
                if (punishments.size() > 0) {
                    String prefix = target.getUniqueId() + ".Punishments." + args[1].toLowerCase() + ".";
                    if (plugin.getDataConfig().getConfig().get(prefix + "Amount") != null && plugin.getDataConfig().getConfig().getInt(prefix + "Amount") != 0) {
                        plugin.getDataConfig().getConfig().set(prefix + "Amount", plugin.getDataConfig().getConfig().getInt(prefix + "Amount") - 1);
                        try {
                            plugin.getDataConfig().getConfig().save(plugin.getDataConfig().getConfigFile());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sender.sendMessage(BLUE + "UnPunished " + target.getName() + " by one level!");
                    } else {
                        sender.sendMessage(RED + "That player has not been punished before!");
                    }
                } else {
                    sender.sendMessage(RED + "That punishment does not exist!");
                }
            } else {
                sender.sendMessage(RED + "That player does not exist! (Have they joined the server before, or has their username changed?)");
            }
            return true;
        }
        return false;
    }
}
