package org.bukkit.command.defaults;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class BanIpCommand extends VanillaCommand {
    public static final Pattern ipValidity = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public BanIpCommand() {
        super("ban-ip");
        this.description = "禁止指定IP的玩家进入此服务器";
        this.usageMessage = "/ban-ip <IP地址|玩家名> [理由 ...]";
        this.setPermission("bukkit.command.ban.ip");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }

        // TODO: Ban Reason support
        if (ipValidity.matcher(args[0]).matches()) {
            processIPBan(args[0], sender);
        } else {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
                return false;
            }

            processIPBan(player.getAddress().getAddress().getHostAddress(), sender);
        }

        return true;
    }

    private void processIPBan(String ip, CommandSender sender) {
        // TODO: Kick on ban
        Bukkit.banIP(ip);

        Command.broadcastCommandMessage(sender, "已封禁IP: " + ip);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
