package org.bukkit.command.defaults;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class ScoreboardCommand extends VanillaCommand {

    private static final List<String> MAIN_CHOICES = ImmutableList.of("objectives", "players", "teams");
    private static final List<String> OBJECTIVES_CHOICES = ImmutableList.of("list", "add", "remove", "setdisplay");
    private static final List<String> OBJECTIVES_CRITERIA = ImmutableList.of("health", "playerKillCount", "totalKillCount", "deathCount", "dummy");
    private static final List<String> PLAYERS_CHOICES = ImmutableList.of("set", "add", "remove", "reset", "list");
    private static final List<String> TEAMS_CHOICES = ImmutableList.of("add", "remove", "join", "leave", "empty", "list", "option");
    private static final List<String> TEAMS_OPTION_CHOICES = ImmutableList.of("color", "friendlyfire", "seeFriendlyInvisibles");
    private static final Map<String, DisplaySlot> OBJECTIVES_DISPLAYSLOT = ImmutableMap.of("belowName", DisplaySlot.BELOW_NAME, "list", DisplaySlot.PLAYER_LIST, "sidebar", DisplaySlot.SIDEBAR);
    private static final Map<String, ChatColor> TEAMS_OPTION_COLOR = ImmutableMap.<String, ChatColor>builder()
            .put("aqua", ChatColor.AQUA)
            .put("black", ChatColor.BLACK)
            .put("blue", ChatColor.BLUE)
            .put("bold", ChatColor.BOLD)
            .put("dark_aqua", ChatColor.DARK_AQUA)
            .put("dark_blue", ChatColor.DARK_BLUE)
            .put("dark_gray",  ChatColor.DARK_GRAY)
            .put("dark_green", ChatColor.DARK_GREEN)
            .put("dark_purple", ChatColor.DARK_PURPLE)
            .put("dark_red", ChatColor.DARK_RED)
            .put("gold", ChatColor.GOLD)
            .put("gray", ChatColor.GRAY)
            .put("green", ChatColor.GREEN)
            .put("italic", ChatColor.ITALIC)
            .put("light_purple", ChatColor.LIGHT_PURPLE)
            .put("obfuscated", ChatColor.MAGIC) // This is the important line
            .put("red", ChatColor.RED)
            .put("reset", ChatColor.RESET)
            .put("strikethrough", ChatColor.STRIKETHROUGH)
            .put("underline", ChatColor.UNDERLINE)
            .put("white", ChatColor.WHITE)
            .put("yellow", ChatColor.YELLOW)
            .build();
    private static final List<String> BOOLEAN = ImmutableList.of("true", "false");

    public ScoreboardCommand() {
        super("scoreboard");
        this.description = "计分板控制喵";
        this.usageMessage = "/scoreboard";
        this.setPermission("bukkit.command.scoreboard");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender))
            return true;
        if (args.length < 1 || args[0].length() == 0) {
            sender.sendMessage(ChatColor.RED + "用法: /scoreboard <objectives|players|teams>");
            return false;
        }

        final Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        if (args[0].equalsIgnoreCase("objectives")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "用法: /scoreboard objectives <list|add|remove|setdisplay>");
                return false;
            }
            if (args[1].equalsIgnoreCase("list")) {
                Set<Objective> objectives = mainScoreboard.getObjectives();
                if (objectives.isEmpty()) {
                    sender.sendMessage(ChatColor.RED + "计分板上没有对象啊喵");
                    return false;
                }
                sender.sendMessage(ChatColor.DARK_GREEN + "显示计分板上的 " + objectives.size() + " 个对象中..");
                for (Objective objective : objectives) {
                    sender.sendMessage("- " + objective.getName() + ": 显示为 '" + objective.getDisplayName() + "' 类型为 '" + objective.getCriteria() + "'");
                }
            } else if (args[1].equalsIgnoreCase("add")) {
                if (args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard objectives add <name> <criteriaType> [display name ...]");
                    return false;
                }
                String name = args[2];
                String criteria = args[3];

                if (criteria == null) {
                    sender.sendMessage(ChatColor.RED + "无效的对象判据类型喵 可以使用的有: " + stringCollectionToString(OBJECTIVES_CRITERIA));
                } else if (name.length() > 16) {
                    sender.sendMessage(ChatColor.RED + "对象的名字 '" + name + "' 太长了喵 最长只能使用16个字符哦");
                } else if (mainScoreboard.getObjective(name) != null) {
                    sender.sendMessage(ChatColor.RED + "一个名为 '" + name + "' 的对象已经存在了喵");
                } else {
                    String displayName = null;
                    if (args.length > 4) {
                        displayName = StringUtils.join(ArrayUtils.subarray(args, 4, args.length), ' ');
                        if (displayName.length() > 32) {
                            sender.sendMessage(ChatColor.RED + "对象的显示名字 '" + displayName + "' 太长了喵 最长只能使用16个字符哦");
                            return false;
                        }
                    }
                    Objective objective = mainScoreboard.registerNewObjective(name, criteria);
                    if (displayName != null && displayName.length() > 0) {
                        objective.setDisplayName(displayName);
                    }
                    sender.sendMessage("添加新的对象 '" + name + "' 成功了喵");
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard objectives remove <name>");
                    return false;
                }
                String name = args[2];
                Objective objective = mainScoreboard.getObjective(name);
                if (objective == null) {
                    sender.sendMessage(ChatColor.RED + "找不到叫做 '" + name + "' 的对象啊喵");
                } else {
                    objective.unregister();
                    sender.sendMessage("成功删除了 '" + name + "' 对象喵");
                }
            } else if (args[1].equalsIgnoreCase("setdisplay")) {
                if (args.length != 3 && args.length != 4) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard objectives setdisplay <slot> [objective]");
                    return false;
                }
                String slotName = args[2];
                DisplaySlot slot = OBJECTIVES_DISPLAYSLOT.get(slotName);
                if (slot == null) {
                    sender.sendMessage(ChatColor.RED + "没有叫做 '" + slotName + "' 的显示形式啊喵");
                } else {
                    if (args.length == 4) {
                        String objectiveName = args[3];
                        Objective objective = mainScoreboard.getObjective(objectiveName);
                        if (objective == null) {
                            sender.sendMessage(ChatColor.RED + "没有对象叫做 '" + objectiveName + "' 的说");
                            return false;
                        }

                        objective.setDisplaySlot(slot);
                        sender.sendMessage("已设置对象 '" + objective.getName() + "' 的显示方式为 '" + slotName);
                    } else {
                        Objective objective = mainScoreboard.getObjective(slot);
                        if (objective != null) {
                            objective.setDisplaySlot(null);
                        }
                        sender.sendMessage("已清除显示方式为 '" + slotName + "' 的对象喵");
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("players")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "/scoreboard players <set|add|remove|reset|list>");
                return false;
            }
            if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
                if (args.length != 5) {
                    if (args[1].equalsIgnoreCase("set")) {
                        sender.sendMessage(ChatColor.RED + "/scoreboard players set <player> <objective> <score>");
                    } else if (args[1].equalsIgnoreCase("add")) {
                        sender.sendMessage(ChatColor.RED + "/scoreboard players add <player> <objective> <count>");
                    } else {
                        sender.sendMessage(ChatColor.RED + "/scoreboard players remove <player> <objective> <count>");
                    }
                    return false;
                }
                String objectiveName = args[3];
                Objective objective = mainScoreboard.getObjective(objectiveName);
                if (objective == null) {
                    sender.sendMessage(ChatColor.RED + "没有对象叫做 '" + objectiveName + "' 的说");
                    return false;
                } else if (!objective.isModifiable()) {
                    sender.sendMessage(ChatColor.RED + "对象 '" + objectiveName + "' 不可写喵");
                    return false;
                }

                String valueString = args[4];
                int value;
                try {
                    value = Integer.parseInt(valueString);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "'" + valueString + "' 不是一个数字喵");
                    return false;
                }
                if (value < 1 && !args[1].equalsIgnoreCase("set")) {
                    sender.sendMessage(ChatColor.RED + "输入的数字 (" + value + ") 太小了, 最小是 1 喵");
                    return false;
                }

                String playerName = args[2];
                if (playerName.length() > 16) {
                    sender.sendMessage(ChatColor.RED + "'" + playerName + "' 玩家名太长了喵");
                    return false;
                }
                Score score = objective.getScore(Bukkit.getOfflinePlayer(playerName));
                int newScore;
                if (args[1].equalsIgnoreCase("set")) {
                    newScore = value;
                } else if (args[1].equalsIgnoreCase("add")) {
                    newScore = score.getScore() + value;
                } else {
                    newScore = score.getScore() - value;
                }
                score.setScore(newScore);
                sender.sendMessage("设置 " + objectiveName + " 对象分数给 " + playerName + " 到 " + newScore);
            } else if (args[1].equalsIgnoreCase("reset")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard players reset <player>");
                    return false;
                }
                String playerName = args[2];
                if (playerName.length() > 16) {
                    sender.sendMessage(ChatColor.RED + "'" + playerName + "' 玩家名太长了喵");
                    return false;
                }
                mainScoreboard.resetScores(Bukkit.getOfflinePlayer(playerName));
                sender.sendMessage("重置 " + playerName + " 的所有分数");
            } else if (args[1].equalsIgnoreCase("list")) {
                if (args.length > 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard players list <player>");
                    return false;
                }
                if (args.length == 2) {
                    Set<OfflinePlayer> players = mainScoreboard.getPlayers();
                    if (players.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "计分板上没有跟踪的玩家哦");
                    } else {
                        sender.sendMessage(ChatColor.DARK_GREEN + "在计分板上查看 " + players.size() + " 个跟踪的玩家");
                        sender.sendMessage(offlinePlayerSetToString(players));
                    }
                } else {
                    String playerName = args[2];
                    if (playerName.length() > 16) {
                        sender.sendMessage(ChatColor.RED + "'" + playerName + "' 玩家名太长了喵");
                        return false;
                    }
                    Set<Score> scores = mainScoreboard.getScores(Bukkit.getOfflinePlayer(playerName));
                    if (scores.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "玩家 " + playerName + " 没有记录的分数喵");
                    } else {
                        sender.sendMessage(ChatColor.DARK_GREEN + "在计分板上查看 " + scores.size() + " 个跟踪的对象 对: " + playerName);
                        for (Score score : scores) {
                            sender.sendMessage("- " + score.getObjective().getDisplayName() + ": " + score.getScore() + " (" + score.getObjective().getName() + ")");
                        }
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("teams")) {
            if (args.length == 1) {
                sender.sendMessage(ChatColor.RED + "/scoreboard teams <list|add|remove|empty|join|leave|option>");
                return false;
            }
            if (args[1].equalsIgnoreCase("list")) {
                if (args.length == 2) {
                    Set<Team> teams = mainScoreboard.getTeams();
                    if (teams.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "计分板上没有注册的队伍哦");
                    } else {
                        sender.sendMessage(ChatColor.DARK_GREEN + "查看计分板中 " + teams.size() + " 个队伍");
                        for (Team team : teams) {
                            sender.sendMessage("- " + team.getName() + ": '" + team.getDisplayName() + "' 有 " + team.getSize() + " 个玩家");
                        }
                    }
                } else if (args.length == 3) {
                    String teamName = args[2];
                    Team team = mainScoreboard.getTeam(teamName);
                    if (team == null) {
                        sender.sendMessage(ChatColor.RED + "找不到叫做 '" + teamName + "' 的队伍");
                    } else {
                        Set<OfflinePlayer> players = team.getPlayers();
                        if (players.isEmpty()) {
                            sender.sendMessage(ChatColor.RED + "队伍 " + team.getName() + " 没有成员");
                        } else {
                            sender.sendMessage(ChatColor.DARK_GREEN + "查看 " + team.getName() + " 队伍中的 " + players.size() + " 个玩家");
                            sender.sendMessage(offlinePlayerSetToString(players));
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams list [name]");
                    return false;
                }
            } else if (args[1].equalsIgnoreCase("add")) {
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams add <name> [display name ...]");
                    return false;
                }
                String name = args[2];
                if (name.length() > 16) {
                    sender.sendMessage(ChatColor.RED + "队伍名字 '" + name + "' 太长了喵, 最长只能使用16个字符哦");
                } else if (mainScoreboard.getTeam(name) != null) {
                    sender.sendMessage(ChatColor.RED + "A team with the name '" + name + "' already exists");
                } else {
                    String displayName = null;
                    if (args.length > 3) {
                        displayName = StringUtils.join(ArrayUtils.subarray(args, 4, args.length), ' ');
                        if (displayName.length() > 32) {
                            sender.sendMessage(ChatColor.RED + "显示的名字 '" + displayName + "' 太长了喵, 最长只能使用32个字符哦");
                            return false;
                        }
                    }
                    Team team = mainScoreboard.registerNewTeam(name);
                    if (displayName != null && displayName.length() > 0) {
                        team.setDisplayName(displayName);
                    }
                    sender.sendMessage("成功添加了 '" + team.getName() + "' 队伍");
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams remove <name>");
                    return false;
                }
                String name = args[2];
                Team team = mainScoreboard.getTeam(name);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "找不到叫做 '" + name + "' 的队伍喵");
                } else {
                    team.unregister();
                    sender.sendMessage("已删除队伍 " + name);
                }
            } else if (args[1].equalsIgnoreCase("empty")) {
                if (args.length != 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams clear <name>");
                    return false;
                }
                String name = args[2];
                Team team = mainScoreboard.getTeam(name);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "找不到叫做 '" + name + "' 的队伍喵");
                } else {
                    Set<OfflinePlayer> players = team.getPlayers();
                    if (players.isEmpty()) {
                        sender.sendMessage(ChatColor.RED + "队伍 " + team.getName() + " 已经空空的了喵");
                    } else {
                        for (OfflinePlayer player : players) {
                            team.removePlayer(player);
                        }
                        sender.sendMessage("从队伍 " + team.getName() + " 删除了 " + players.size() + " 个玩家");
                    }
                }
            } else if (args[1].equalsIgnoreCase("join")) {
                if ((sender instanceof Player) ? args.length < 3 : args.length < 4) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams join <team> [player...]");
                    return false;
                }
                String teamName = args[2];
                Team team = mainScoreboard.getTeam(teamName);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "找不到叫做 '" + teamName + "' 的队伍喵");
                } else {
                    Set<String> addedPlayers = new HashSet<String>();
                    if ((sender instanceof Player) && args.length == 3) {
                        team.addPlayer((Player) sender);
                        addedPlayers.add(sender.getName());
                    } else {
                        for (int i = 3; i < args.length; i++) {
                            String playerName = args[i];
                            OfflinePlayer offlinePlayer;
                            Player player = Bukkit.getPlayerExact(playerName);
                            if (player != null) {
                                offlinePlayer = player;
                            } else {
                                offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                            }
                            team.addPlayer(offlinePlayer);
                            addedPlayers.add(offlinePlayer.getName());
                        }
                    }
                    sender.sendMessage("成功添加了 " + addedPlayers.size() + " 个玩家到 " + team.getName() + ": " + stringCollectionToString(addedPlayers));
                }
            } else if (args[1].equalsIgnoreCase("leave")) {
                if (!(sender instanceof Player) && args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams leave [player...]");
                    return false;
                }
                Set<String> left = new HashSet<String>();
                Set<String> noTeam = new HashSet<String>();
                if ((sender instanceof Player) && args.length == 2) {
                    Team team = mainScoreboard.getPlayerTeam((Player) sender);
                    if (team != null) {
                        team.removePlayer((Player) sender);
                        left.add(sender.getName());
                    } else {
                        noTeam.add(sender.getName());
                    }
                } else {
                    for (int i = 2; i < args.length; i++) {
                        String playerName = args[i];
                        OfflinePlayer offlinePlayer;
                        Player player = Bukkit.getPlayerExact(playerName);
                        if (player != null) {
                            offlinePlayer = player;
                        } else {
                            offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                        }
                        Team team = mainScoreboard.getPlayerTeam(offlinePlayer);
                        if (team != null) {
                            team.removePlayer(offlinePlayer);
                            left.add(offlinePlayer.getName());
                        } else {
                            noTeam.add(offlinePlayer.getName());
                        }
                    }
                }
                if (!left.isEmpty()) {
                    sender.sendMessage("从以下队伍中删除了 " + left.size() + " 个玩家: " + stringCollectionToString(left));
                }
                if (!noTeam.isEmpty()) {
                    sender.sendMessage("不能以下队伍中删除 " + noTeam.size() + " 个玩家: " + stringCollectionToString(noTeam));
                }
            } else if (args[1].equalsIgnoreCase("option")) {
                if (args.length != 4 && args.length != 5) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams option <team> <friendlyfire|color|seefriendlyinvisibles> <value>");
                    return false;
                }
                String teamName = args[2];
                Team team = mainScoreboard.getTeam(teamName);
                if (team == null) {
                    sender.sendMessage(ChatColor.RED + "找不到叫做 '" + teamName + "' 的队伍喵");
                    return false;
                }
                String option = args[3].toLowerCase();
                if (!option.equals("friendlyfire") && !option.equals("color") && !option.equals("seefriendlyinvisibles")) {
                    sender.sendMessage(ChatColor.RED + "/scoreboard teams option <team> <friendlyfire|color|seefriendlyinvisibles> <value>");
                    return false;
                }
                if (args.length == 4) {
                    if (option.equals("color")) {
                        sender.sendMessage(ChatColor.RED + "可用的颜色选项有: " + stringCollectionToString(TEAMS_OPTION_COLOR.keySet()));
                    } else {
                        sender.sendMessage(ChatColor.RED + "可用的 " + option + " 选项有: true 和 false");
                    }
                } else {
                    String value = args[4].toLowerCase();
                    if (option.equals("color")) {
                        ChatColor color = TEAMS_OPTION_COLOR.get(value);
                        if (color == null) {
                            sender.sendMessage(ChatColor.RED + "可用的颜色选项有: " + stringCollectionToString(TEAMS_OPTION_COLOR.keySet()));
                            return false;
                        }
                        team.setPrefix(color.toString());
                        team.setSuffix(ChatColor.RESET.toString());
                    } else {
                        if (!value.equals("true") && !value.equals("false")) {
                            sender.sendMessage(ChatColor.RED + "可用的 " + option + " 选项有: true 和 false");
                            return false;
                        }
                        if (option.equals("friendlyfire")) {
                            team.setAllowFriendlyFire(value.equals("true"));
                        } else {
                            team.setCanSeeFriendlyInvisibles(value.equals("true"));
                        }
                    }
                    sender.sendMessage("设置" + team.getName() + "队伍的 " + option + " 选项为 " + value);
                }
            }
        } else {
            sender.sendMessage(ChatColor.RED + "用法: /scoreboard <objectives|players|teams>");
            return false;
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], MAIN_CHOICES, new ArrayList<String>());
        }
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("objectives")) {
                if (args.length == 2) {
                    return StringUtil.copyPartialMatches(args[1], OBJECTIVES_CHOICES, new ArrayList<String>());
                }
                if (args[1].equalsIgnoreCase("add")) {
                    if (args.length == 4) {
                        return StringUtil.copyPartialMatches(args[3], OBJECTIVES_CRITERIA, new ArrayList<String>());
                    }
                } else if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], this.getCurrentObjectives(), new ArrayList<String>());
                    }
                } else if (args[1].equalsIgnoreCase("setdisplay")) {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], OBJECTIVES_DISPLAYSLOT.keySet(), new ArrayList<String>());
                    }
                    if (args.length == 4) {
                        return StringUtil.copyPartialMatches(args[3], this.getCurrentObjectives(), new ArrayList<String>());
                    }
                }
            } else if (args[0].equalsIgnoreCase("players")) {
                if (args.length == 2) {
                    return StringUtil.copyPartialMatches(args[1], PLAYERS_CHOICES, new ArrayList<String>());
                }
                if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
                    if (args.length == 3) {
                        return super.tabComplete(sender, alias, args);
                    }
                    if (args.length == 4) {
                        return StringUtil.copyPartialMatches(args[3], this.getCurrentObjectives(), new ArrayList<String>());
                    }
                } else {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], this.getCurrentPlayers(), new ArrayList<String>());
                    }
                }
            } else if (args[0].equalsIgnoreCase("teams")) {
                if (args.length == 2) {
                    return StringUtil.copyPartialMatches(args[1], TEAMS_CHOICES, new ArrayList<String>());
                }
                if (args[1].equalsIgnoreCase("join")) {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList<String>());
                    }
                    if (args.length >= 4) {
                        return super.tabComplete(sender, alias, args);
                    }
                } else if (args[1].equalsIgnoreCase("leave")) {
                    return super.tabComplete(sender, alias, args);
                } else if (args[1].equalsIgnoreCase("option")) {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList<String>());
                    }
                    if (args.length == 4) {
                        return StringUtil.copyPartialMatches(args[3], TEAMS_OPTION_CHOICES, new ArrayList<String>());
                    }
                    if (args.length == 5) {
                        if (args[3].equalsIgnoreCase("color")) {
                            return StringUtil.copyPartialMatches(args[4], TEAMS_OPTION_COLOR.keySet(), new ArrayList<String>());
                        } else {
                            return StringUtil.copyPartialMatches(args[4], BOOLEAN, new ArrayList<String>());
                        }
                    }
                } else {
                    if (args.length == 3) {
                        return StringUtil.copyPartialMatches(args[2], this.getCurrentTeams(), new ArrayList<String>());
                    }
                }
            }
        }

        return ImmutableList.of();
    }

    private static String offlinePlayerSetToString(Set<OfflinePlayer> set) {
        StringBuilder string = new StringBuilder();
        String lastValue = null;
        for (OfflinePlayer value : set) {
            string.append(lastValue = value.getName()).append(", ");
        }
        string.delete(string.length() - 2, Integer.MAX_VALUE);
        if (string.length() != lastValue.length()) {
            string.insert(string.length() - lastValue.length(), "和 ");
        }
        return string.toString();

    }

    private static String stringCollectionToString(Collection<String> set) {
        StringBuilder string = new StringBuilder();
        String lastValue = null;
        for (String value : set) {
            string.append(lastValue = value).append(", ");
        }
        string.delete(string.length() - 2, Integer.MAX_VALUE);
        if (string.length() != lastValue.length()) {
            string.insert(string.length() - lastValue.length(), "和 ");
        }
        return string.toString();
    }

    private List<String> getCurrentObjectives() {
        List<String> list = new ArrayList<String>();
        for (Objective objective : Bukkit.getScoreboardManager().getMainScoreboard().getObjectives()) {
            list.add(objective.getName());
        }
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    private List<String> getCurrentPlayers() {
        List<String> list = new ArrayList<String>();
        for (OfflinePlayer player : Bukkit.getScoreboardManager().getMainScoreboard().getPlayers()) {
            list.add(player.getName());
        }
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    private List<String> getCurrentTeams() {
        List<String> list = new ArrayList<String>();
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            list.add(team.getName());
        }
        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
