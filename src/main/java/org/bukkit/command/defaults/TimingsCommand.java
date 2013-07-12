package org.bukkit.command.defaults;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

// Spigot start
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
// Spigot end

public class TimingsCommand extends BukkitCommand {
    private static final List<String> TIMINGS_SUBCOMMANDS = ImmutableList.of("merged", "reset", "separate");

    public static long timingStart = 0; // Spigot
    public TimingsCommand(String name) {
        super(name);
        this.description = "记录所有插件处理用时喵";
        this.usageMessage = "/timings <reset|merged|separate|paste|on|off>"; // Spigot
        this.setPermission("bukkit.command.timings");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;
        if (args.length < 1)  { // Spigot
            sender.sendMessage(ChatColor.RED + "用法: " + usageMessage);
            return false;
        }
        /*if (!sender.getServer().getPluginManager().useTimings()) {
            sender.sendMessage("Please enable timings by setting \"settings.plugin-profiling\" to true in bukkit.yml");
            return true;
        }*/

        // Spigot start - dynamic enable
        if ( "on".equals( args[0] ) )
        {
            ( (org.bukkit.plugin.SimplePluginManager) Bukkit.getPluginManager() ).useTimings( true );
            sender.sendMessage( "启用计时" );
        } else if ( "off".equals( args[0] ) )
        {
            ( (org.bukkit.plugin.SimplePluginManager) Bukkit.getPluginManager() ).useTimings( false );
            sender.sendMessage( "禁用计时" );
        }
        // Spigot end

        boolean separate = "separate".equals(args[0]);
        boolean paste = "paste".equals( args[0] ); // Spigot
        if ("on".equals(args[0]) || "reset".equals(args[0])) { // Spigot
            // Spigot start
            if ( !"on".equals( args[0] ) && !Bukkit.getPluginManager().useTimings() )
            {
                sender.sendMessage( "使用 /timings on 来启用计时喵" );
                return true;
            }
            // Spigot end
            for (HandlerList handlerList : HandlerList.getHandlerLists()) {
                for (RegisteredListener listener : handlerList.getRegisteredListeners()) {
                    if (listener instanceof TimedRegisteredListener) {
                        ((TimedRegisteredListener)listener).reset();
                    }
                }
            }
            // Spigot start
            org.spigotmc.CustomTimingsHandler.reload();
            timingStart = System.nanoTime();
            sender.sendMessage("Timings reset");
        } else if ("merged".equals(args[0]) || separate || paste) {
            if ( !Bukkit.getPluginManager().useTimings() )
            {
                sender.sendMessage( "使用 /timings on 来启用计时喵" );
                return true;
            }
            long sampleTime = System.nanoTime() - timingStart;
            // Spigot end
            int index = 0;
            int pluginIdx = 0;
            File timingFolder = new File("timings");
            timingFolder.mkdirs();
            File timings = new File(timingFolder, "timings.txt");
            File names = null;
            ByteArrayOutputStream bout = ( paste ) ? new ByteArrayOutputStream() : null; // Spigot
            while (timings.exists()) timings = new File(timingFolder, "timings" + (++index) + ".txt");
            PrintStream fileTimings = null;
            PrintStream fileNames = null;
            try {
                fileTimings = ( paste ) ? new PrintStream( bout ) : new PrintStream( timings );
                if (separate) {
                    names = new File(timingFolder, "names" + index + ".txt");
                    fileNames = new PrintStream(names);
                }
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    pluginIdx++;
                    long totalTime = 0;
                    if (separate) {
                        fileNames.println(pluginIdx + " " + plugin.getDescription().getFullName());
                        fileTimings.println("Plugin " + pluginIdx);
                    }
                    else fileTimings.println(plugin.getDescription().getFullName());
                    for (RegisteredListener listener : HandlerList.getRegisteredListeners(plugin)) {
                        if (listener instanceof TimedRegisteredListener) {
                            TimedRegisteredListener trl = (TimedRegisteredListener) listener;
                            long time = trl.getTotalTime();
                            int count = trl.getCount();
                            if (count == 0) continue;
                            long avg = time / count;
                            totalTime += time;
                            Event event = trl.getEvent();
                            if (count > 0 && event != null) {
                                fileTimings.println("    " + event.getClass().getSimpleName() + (trl.hasMultiple() ? " (and others)" : "") + " Time: " + time + " Count: " + count + " Avg: " + avg + " Violations: " + trl.violations); // Spigot
                            }
                        }
                    }
                    fileTimings.println("    Total time " + totalTime + " (" + totalTime / 1000000000 + "s)");
                }

                // Spigot start
                org.spigotmc.CustomTimingsHandler.printTimings(fileTimings);
                fileTimings.println( "Sample time " + sampleTime + " (" + sampleTime / 1E9 + "s)" ); // Spigot
                // Spigot start
                if ( paste )
                {
                    new PasteThread( sender, bout ).start();
                    return true;
                }
                // Spigot end
                sender.sendMessage("计时信息已经保存到 " + timings.getPath());
                sender.sendMessage( "将文件的内容粘贴到 http://aikar.co/timings.php 读取结果." );
                if (separate) sender.sendMessage("名字已经保存到 " + names.getPath());
            } catch (IOException e) {
            } finally {
                if (fileTimings != null) {
                    fileTimings.close();
                }
                if (fileNames != null) {
                    fileNames.close();
                }
            }
        }
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], TIMINGS_SUBCOMMANDS, new ArrayList<String>(TIMINGS_SUBCOMMANDS.size()));
        }
        return ImmutableList.of();
    }

    // Spigot start
    private static class PasteThread extends Thread
    {

        private final CommandSender sender;
        private final ByteArrayOutputStream bout;

        public PasteThread(CommandSender sender, ByteArrayOutputStream bout)
        {
            super( "Timings paste thread" );
            this.sender = sender;
            this.bout = bout;
        }

        @Override
        public void run()
        {
            try
            {
                HttpURLConnection con = (HttpURLConnection) new URL( "http://paste.ubuntu.com/" ).openConnection();
                con.setDoOutput( true );
                con.setRequestMethod( "POST" );
                con.setInstanceFollowRedirects( false );

                OutputStream out = con.getOutputStream();
                out.write( "poster=Spigot&syntax=text&content=".getBytes( "UTF-8" ) );
                out.write( URLEncoder.encode( bout.toString( "UTF-8" ), "UTF-8" ).getBytes( "UTF-8" ) );
                out.close();
                con.getInputStream().close();

                String location = con.getHeaderField( "Location" );
                String pasteID = location.substring( "http://paste.ubuntu.com/".length(), location.length() - 1 );
                sender.sendMessage( ChatColor.GREEN + "计时结果已经保存到 " + location );
                sender.sendMessage( ChatColor.GREEN + "你可以在 http://aikar.co/timings.php?url=" + pasteID + " 查看结果喵");
            } catch ( IOException ex )
            {
                sender.sendMessage( ChatColor.RED + "计时出错 检查控制台获取更多信息喵" );
                Bukkit.getServer().getLogger().log( Level.WARNING, "Could not paste timings", ex );
            }
        }
    }
    // Spigot end
}
