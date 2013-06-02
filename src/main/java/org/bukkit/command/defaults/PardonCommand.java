package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class PardonCommand extends VanillaCommand {
    public PardonCommand() {
        super("pardon");
        this.description = "重新允许玩家使用服务器喵";
        this.usageMessage = "/pardon <玩家名>";
        this.setPermission("bukkit.command.unban.player");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length != 1)  {
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }

        Bukkit.getOfflinePlayer(args[0]).setBanned(false);
        Command.broadcastCommandMessage(sender, "已经解除 " + args[0] + "的封禁了哦");
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            List<String> completions = new ArrayList<String>();
            for (OfflinePlayer player : Bukkit.getBannedPlayers()) {
                String name = player.getName();
                if (StringUtil.startsWithIgnoreCase(name, args[0])) {
                    completions.add(name);
                }
            }
            return completions;
        }
        return ImmutableList.of();
    }
}
