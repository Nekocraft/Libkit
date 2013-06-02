package org.bukkit.command.defaults;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

public class EffectCommand extends VanillaCommand {
    private static final List<String> effects;

    public EffectCommand() {
        super("effect");
        this.description = "给玩家增加药水效果喵";
        this.usageMessage = "/effect <玩家名> <效果名> [持续秒数] [等级]";
        this.setPermission("bukkit.command.effect");
    }

    static {
        ImmutableList.Builder<String> builder = ImmutableList.<String>builder();

        for (PotionEffectType type : PotionEffectType.values()) {
            if (type != null) {
                builder.add(type.getName());
            }
        }

        effects = builder.build();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(getUsage());
            return true;
        }

        final Player player = sender.getServer().getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(ChatColor.RED + String.format("玩家 %s 并~ 不~ 存~ 在~", args[0]));
            return true;
        }

        PotionEffectType effect = PotionEffectType.getByName(args[1]);

        if (effect == null) {
            effect = PotionEffectType.getById(getInteger(sender, args[1], 0));
        }

        if (effect == null) {
            sender.sendMessage(ChatColor.RED + String.format("效果 %s 并~ 不~ 存~ 在~", args[1]));
            return true;
        }

        int duration = 600;
        int duration_temp = 30;
        int amplification = 0;

        if (args.length >= 3) {
            duration_temp = getInteger(sender, args[2], 0, 1000000);
            if (effect.isInstant()) {
                duration = duration_temp;
            } else {
                duration = duration_temp * 20;
            }
        } else if (effect.isInstant()) {
            duration = 1;
        }

        if (args.length >= 4) {
            amplification = getInteger(sender, args[3], 0, 255);
        }

        if (duration_temp == 0) {
            if (!player.hasPotionEffect(effect)) {
                sender.sendMessage(String.format("不能取消对 %s 的效果 %s 他还没有这个效果呢", args[0], effect.getName()));
                return true;
            }

            player.removePotionEffect(effect);
            broadcastCommandMessage(sender, String.format("取消了 %s 的 %s 效果", args[0], effect.getName()));
        } else {
            final PotionEffect applyEffect = new PotionEffect(effect, duration, amplification);

            player.addPotionEffect(applyEffect, true);
            broadcastCommandMessage(sender, String.format("设置 %s (ID %d) 特效 %d 给 %s 持续 %d 秒", effect.getName(), effect.getId(), amplification, args[0], duration));
        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String commandLabel, String[] args) {
        if (args.length == 1) {
            return super.tabComplete(sender, commandLabel, args);
        } else if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], effects, new ArrayList<String>(effects.size()));
        }

        return ImmutableList.of();
    }
}
