package org.libigot;

import org.bukkit.entity.Player;
import org.libigot.entity.LibigotPlayer;
import org.libigot.world.LibigotWorld;

public interface LibigotServer {

    /**
     * Returns the servers Minecraft version.
     *
     * @return Minecraft version.
     */
    public String getMinecraftVersion();

    /**
     * Gets the servers current TPS
     *
     * @return The servers current TPS
     */
    public double getCurrentTPS();

    /**
     * Gets a LibigotPlayer from their exact name
     *
     * @param name The players name
     * @return The LibigotPlayer instance
     */
    public LibigotPlayer getPlayerExact(String name);

    /**
     * Matches a name to a LibigotPlayer
     *
     * @param name The players name
     * @return The LibigotPlayer instance
     */
    public LibigotPlayer getPlayer(String name);

    /**
     * Returns a Libigot player.
     *
     * @param player The Bukkit player
     * @return The Libigot player.
     */
    public LibigotPlayer getPlayer(Player player);

    /**
     * Gets all Online Players as a LibigotPlayer array
     *
     * @return The LibigotPlayer array
     */
    public LibigotPlayer[] getOnlinePlayers();
    
    /**
     * Returns a Libigot world
     *
     * @param name World's name
     * @return The world
     */
    public LibigotWorld getWorld(String name);
    
    /**
     * Get if the server is in debug mode
     * 
     * @return true if the server is in debug mode, false otherwise
     */
    public boolean getDebug();
    
    /**
     * Set the servers debug mode
     * 
     * @param debug true to set the server is in debug mode, false to disable it
     */
    public void setDebug(boolean debug);
}
