package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SpawnpointCommand extends VanillaCommand {

    public SpawnpointCommand() {
        super("spawnpoint");
        this.description = "设置玩家的出生点";
        this.usageMessage = "/spawnpoint 或 /spawnpoint <玩家名> 或 /spawnpoint <玩家名> <x> <y> <z>";
        this.setPermission("bukkit.command.spawnpoint");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        Player player;

        if (args.length == 0) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("请指定一个玩家喵!");
                return true;
            }
        } else {
            player = Bukkit.getPlayerExact(args[0]);
            if (player == null) {
                sender.sendMessage("找不到玩家喵 " + args[0]);
                return true;
            }
        }

        World world = player.getWorld();

        if (args.length == 4) {
            if (world != null) {
                int pos = 1;
                final int x, y, z;
                try {
                    x = getInteger(sender, args[pos++], MIN_COORD, MAX_COORD, true);
                    y = getInteger(sender, args[pos++], 0, world.getMaxHeight());
                    z = getInteger(sender, args[pos], MIN_COORD, MAX_COORD, true);
                } catch(NumberFormatException ex) {
                    sender.sendMessage(ex.getMessage());
                    return true;
                }

                player.setBedSpawnLocation(new Location(world, x, y, z), true);
                Command.broadcastCommandMessage(sender, "已设置玩家 " + player.getDisplayName() + " 的出生点为 " + x + ", " + y + ", " + z);
            }
        } else if (args.length <= 1) {
            Location location = player.getLocation();
            player.setBedSpawnLocation(location, true);
            Command.broadcastCommandMessage(sender, "已设置玩家 " + player.getDisplayName() + " 的出生点为 " + location.getX() + ", " + location.getY() + ", " + location.getZ());
        } else {
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }

        return ImmutableList.of();
    }
}
