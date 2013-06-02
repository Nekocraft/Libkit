package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class TimeCommand extends VanillaCommand {
    private static final List<String> TABCOMPLETE_ADD_SET = ImmutableList.of("add", "set");
    private static final List<String> TABCOMPLETE_DAY_NIGHT = ImmutableList.of("day", "night");

    public TimeCommand() {
        super("time");
        this.description = "改变每个世界的时间喵";
        this.usageMessage = "/time set <value>\n/time add <value>";
        this.setPermission("bukkit.command.time.add;bukkit.command.time.set");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法:\n" + usageMessage);
            return false;
        }

        int value;

        if (args[0].equals("set")) {
            if (!sender.hasPermission("bukkit.command.time.set")) {
                sender.sendMessage(ChatColor.RED + "你没有权限设置时间喵");
                return true;
            }

            if (args[1].equals("day")) {
                value = 0;
            } else if (args[1].equals("night")) {
                value = 12500;
            } else {
                value = getInteger(sender, args[1], 0);
            }

            for (World world : Bukkit.getWorlds()) {
                world.setTime(value);
            }

            Command.broadcastCommandMessage(sender, "时间设置为 " + value);
        } else if (args[0].equals("add")) {
            if (!sender.hasPermission("bukkit.command.time.add")) {
                sender.sendMessage(ChatColor.RED + "你没有权限设置时间喵");
                return true;
            }

            value = getInteger(sender, args[1], 0);

            for (World world : Bukkit.getWorlds()) {
                world.setFullTime(world.getFullTime() + value);
            }

            Command.broadcastCommandMessage(sender, "添加 " + value + " 到时间");
        } else {
            sender.sendMessage("位置的方法 用法: " + usageMessage);
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], TABCOMPLETE_ADD_SET, new ArrayList<String>(TABCOMPLETE_ADD_SET.size()));
        } else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            return StringUtil.copyPartialMatches(args[1], TABCOMPLETE_DAY_NIGHT, new ArrayList<String>(TABCOMPLETE_DAY_NIGHT.size()));
        }
        return ImmutableList.of();
    }
}
