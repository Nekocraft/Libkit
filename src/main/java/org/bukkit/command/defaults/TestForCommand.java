package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TestForCommand extends VanillaCommand {
    public TestForCommand() {
        super("testfor");
        this.description = "测试一个玩家是否在线喵";
        this.usageMessage = "/testfor <玩家名>";
        this.setPermission("bukkit.command.testfor");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }

        sender.sendMessage(ChatColor.RED + "/testfor 只是用来模拟输出的喵.");
        return true;
    }
}
