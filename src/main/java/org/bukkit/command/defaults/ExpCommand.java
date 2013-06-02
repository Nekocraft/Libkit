package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class ExpCommand extends VanillaCommand {
    public ExpCommand() {
        super("xp");
        this.description = "对指定玩家增加经验喵 负数可以减少哦";
        this.usageMessage = "/xp <经验值> [玩家名] OR /xp <经验值>L [玩家名]";
        this.setPermission("bukkit.command.xp");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (args.length > 0) {
            String inputAmount = args[0];
            Player player = null;

            boolean isLevel = inputAmount.endsWith("l") || inputAmount.endsWith("L");
            if (isLevel && inputAmount.length() > 1) {
                inputAmount = inputAmount.substring(0, inputAmount.length() - 1);
            }

            int amount = getInteger(sender, inputAmount, Integer.MIN_VALUE, Integer.MAX_VALUE);
            boolean isTaking = amount < 0;

            if (isTaking) {
                amount *= -1;
            }

            if (args.length > 1) {
                player = Bukkit.getPlayer(args[1]);
            } else if (sender instanceof Player) {
                player = (Player) sender;
            }

            if (player != null) {
                if (isLevel) {
                    if (isTaking) {
                        player.giveExpLevels(-amount);
                        Command.broadcastCommandMessage(sender, "减少了 " + amount + " 经验等级对 " + player.getName());
                    } else {
                        player.giveExpLevels(amount);
                        Command.broadcastCommandMessage(sender, "增加了 " + amount + " 经验等级给 " + player.getName());
                    }
                } else {
                    if (isTaking) {
                        sender.sendMessage(ChatColor.RED + "减少经验只能使用等级喵");
                        return false;
                    } else {
                        player.giveExp(amount);
                        Command.broadcastCommandMessage(sender, "增加了 " + amount + " 经验点数给 " + player.getName());
                    }
                }
            } else {
                sender.sendMessage("找不到玩家喵 \n" + ChatColor.RED + "用法: " + usageMessage);
                return false;
            }

            return true;
        }

        sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 2) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
