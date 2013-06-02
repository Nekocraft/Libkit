package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import com.google.common.collect.ImmutableList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class EnchantCommand extends VanillaCommand {
    private static final List<String> ENCHANTMENT_NAMES = new ArrayList<String>();

    public EnchantCommand() {
        super("enchant");
        this.description = "对玩家目前持有的物品附加一个附魔~ 使用等级0来移除 指定等级忽略正常的附魔等级限制";
        this.usageMessage = "/enchant <玩家名> <附魔ID> [level|max|0] [force]";
        this.setPermission("bukkit.command.enchant");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }

        boolean force = false;
        if (args.length > 2) {
            force = args[args.length > 3 ? 3 : 2].equalsIgnoreCase("force");
        }

        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            sender.sendMessage("喵 找不到玩家 " + args[0]);
        } else {
            ItemStack item = player.getItemInHand();
            if (item.getType() == Material.AIR) {
                sender.sendMessage("玩家手上空空的喵");
            } else {
                String itemName = item.getType().toString().replaceAll("_", " ");
                itemName = WordUtils.capitalizeFully(itemName);

                Enchantment enchantment = getEnchantment(args[1].toUpperCase());
                if (enchantment == null) {
                    sender.sendMessage(String.format("附魔并不存在~ : %s", args[1]));
                }  else {
                    String enchantmentName = enchantment.getName().replaceAll("_", " ");
                    enchantmentName = WordUtils.capitalizeFully(enchantmentName);

                    if (!force && !enchantment.canEnchantItem(item)) {
                        sender.sendMessage(String.format("%s 不能被应用到 %s 喵", enchantmentName, itemName));
                    } else {
                        int level = 1;
                        if (args.length > 2) {
                            Integer integer = getInteger(args[2]);
                            int minLevel = enchantment.getStartLevel();
                            int maxLevel = force ? Short.MAX_VALUE : enchantment.getMaxLevel();

                            if (integer != null) {
                                if (integer == 0) {
                                    item.removeEnchantment(enchantment);
                                    Command.broadcastCommandMessage(sender, String.format("移除了 %s 的 %s 上的 %s 附魔喵", player.getName(), itemName, enchantmentName));
                                    return true;
                                }

                                if (integer < minLevel || integer > maxLevel) {
                                    sender.sendMessage(String.format("%s 的附魔等级必须在 %d 和 %d 之间喵", enchantmentName, minLevel, maxLevel));
                                    sender.sendMessage("使用等级0来移除");
                                    return true;
                                }

                                level = integer;
                            }

                            if ("max".equals(args[2])) {
                                level = maxLevel;
                            }
                        }

                        Map<Enchantment, Integer> enchantments = item.getEnchantments();
                        boolean conflicts = false;

                        if (!force && !enchantments.isEmpty()) { // TODO: Improve this to use a "hasEnchantments" call
                            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                Enchantment enchant = entry.getKey();

                                if (enchant.equals(enchantment)) continue;
                                if (enchant.conflictsWith(enchantment)) {
                                    sender.sendMessage(String.format("喵 不能应用附魔效果 %s 在一个已经有 %s 附魔的物品上", enchantmentName, WordUtils.capitalizeFully(enchant.getName().replaceAll("_", " "))));
                                    conflicts = true;
                                    break;
                                }
                            }
                        }

                        if (!conflicts) {
                            item.addUnsafeEnchantment(enchantment, level);

                            Command.broadcastCommandMessage(sender, String.format("增加了 %s 的 %s 上的 %s (Lvl %d) 附魔喵", player.getName(), itemName, enchantmentName, level), false);
                            sender.sendMessage(String.format("附魔成功喵, 增加了你的 %s 上的 %s (Lvl %d) 附魔", itemName, enchantmentName, level));
                        }
                    }
                }
            }
        }
        return true;
    }

     @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return super.tabComplete(sender, alias, args);
        }

        if (args.length == 2) {
            return StringUtil.copyPartialMatches(args[1], ENCHANTMENT_NAMES, new ArrayList<String>(ENCHANTMENT_NAMES.size()));
        }

        if (args.length == 3 || args.length == 4) {
            if (!args[args.length - 2].equalsIgnoreCase("force")) {
                return ImmutableList.of("force");
            }
        }

        return ImmutableList.of();
     }

    private Enchantment getEnchantment(String lookup) {
        Enchantment enchantment = Enchantment.getByName(lookup);

        if (enchantment == null) {
            Integer id = getInteger(lookup);
            if (id != null) {
                enchantment = Enchantment.getById(id);
            }
        }

        return enchantment;
    }

    public static void buildEnchantments() {
        if (!ENCHANTMENT_NAMES.isEmpty()) {
            throw new IllegalStateException("Enchantments have already been built!");
        }

        for (Enchantment enchantment : Enchantment.values()) {
            ENCHANTMENT_NAMES.add(enchantment.getName());
        }

        Collections.sort(ENCHANTMENT_NAMES);
    }
}
