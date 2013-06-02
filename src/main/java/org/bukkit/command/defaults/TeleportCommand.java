package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.google.common.collect.ImmutableList;

public class TeleportCommand extends VanillaCommand {

    public TeleportCommand() {
        super("tp");
        this.description = "传送指定玩家 (or yourself) to another player or coordinates";
        this.usageMessage = "/tp [玩家名] <目标玩家名> and/or <x> <y> <z>";
        this.setPermission("bukkit.command.teleport");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1 || args.length > 4) {
            sender.sendMessage(ChatColor.RED + "用法¨: " + usageMessage);
            return false;
        }

        Player player;

        if (args.length == 1 || args.length == 3) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage("指定一个有效的玩家喵!");
                return true;
            }
        } else {
            player = Bukkit.getPlayerExact(args[0]);
        }

        if (player == null) {
            sender.sendMessage("找不到玩家: " + args[0]);
            return true;
        }

        if (args.length < 3) {
            Player target = Bukkit.getPlayerExact(args[args.length - 1]);
            if (target == null) {
                sender.sendMessage("找不到玩家 " + args[args.length - 1] + ". 没有传送喵.");
                return true;
            }
            player.teleport(target, TeleportCause.COMMAND);
            Command.broadcastCommandMessage(sender, "传送 " + player.getDisplayName() + " 到 " + target.getDisplayName());
        } else if (player.getWorld() != null) {
            Location playerLocation = player.getLocation();
            double x = getCoordinate(sender, playerLocation.getX(), args[args.length - 3]);
            double y = getCoordinate(sender, playerLocation.getY(), args[args.length - 2], 0, 0);
            double z = getCoordinate(sender, playerLocation.getZ(), args[args.length - 1]);

            if (x == MIN_COORD_MINUS_ONE || y == MIN_COORD_MINUS_ONE || z == MIN_COORD_MINUS_ONE) {
                sender.sendMessage("必需指定一个有效的地点喵!");
                return true;
            }

            playerLocation.setX(x);
            playerLocation.setY(y);
            playerLocation.setZ(z);

            player.teleport(playerLocation);
            Command.broadcastCommandMessage(sender, String.format("传送到 %s 到 %.2f, %.2f, %.2f", player.getDisplayName(), x, y, z));
        }
        return true;
    }

    private double getCoordinate(CommandSender sender, double current, String input) {
        return getCoordinate(sender, current, input, MIN_COORD, MAX_COORD);
    }

    private double getCoordinate(CommandSender sender, double current, String input, int min, int max) {
        boolean relative = input.startsWith("~");
        double result = relative ? current : 0;

        if (!relative || input.length() > 1) {
            boolean exact = input.contains(".");
            if (relative) input = input.substring(1);

            double testResult = getDouble(sender, input);
            if (testResult == MIN_COORD_MINUS_ONE) {
                return MIN_COORD_MINUS_ONE;
            }
            result += testResult;

            if (!exact && !relative) result += 0.5f;
        }
        if (min != 0 || max != 0) {
            if (result < min) {
                result = MIN_COORD_MINUS_ONE;
            }

            if (result > max) {
                result = MIN_COORD_MINUS_ONE;
            }
        }

        return result;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1 || args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
