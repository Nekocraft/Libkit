package org.libigot;

import java.io.Serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.libigot.Libigot;
import org.libigot.world.LibigotWorld;

/**
 * A Location which can be saved over time without creating memory leaks.
 */
public class LibigotLocation implements Serializable {

    private static final long serialVersionUID = 3230676404429286743L;
    private final String world;
    private final double x, y, z;
    private final float yaw, pitch;

    /**
     * Create a SurgeLocation from a Bukkit Location
     * 
     * @param location The Bukkit Location
     */
    public LibigotLocation(Location location) {
        this(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Create a SurgeLocation from a Bukkit Block
     * 
     * @param location The Bukkit Block
     */
    public LibigotLocation(Block block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * Create a SurgeLocation from a World and coordinates
     * 
     * @param world The World
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public LibigotLocation(World world, double x, double y, double z) {
        this(world, x, y, z, 0.0F, 0.0F);
    }

    /**
     * Create a SurgeLocation from a World name and coordinates
     * 
     * @param world The World
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     */
    public LibigotLocation(String world, double x, double y, double z) {
        this(world, x, y, z, 0.0F, 0.0F);
    }

    /**
     * Create a SurgeLocation from a World, coordinates, yaw and pitch
     * 
     * @param world The World
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param yaw The yaw
     * @param pitch The Z pitch
     */
    public LibigotLocation(World world, double x, double y, double z, float yaw, float pitch) {
        this(world.getName(), x, y, z, yaw, pitch);
    }

    /**
     * Create a SurgeLocation from a World name, coordinates, yaw and pitch
     * 
     * @param world The World
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param yaw The yaw
     * @param pitch The Z pitch
     */
    public LibigotLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Get the World saved in this location
     * 
     * @return The world or null if the world is unloaded
     */
    public LibigotWorld getWorld() {
        return Libigot.getServer().getWorld(this.world);
    }

    /**
     * Get the World name saved in this location
     * 
     * @return The world name
     */
    public String getWorldName() {
        return this.world;
    }

    /**
     * Get the X coordinate
     * 
     * @return The X coordinate
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the Y coordinate
     * 
     * @return The Y coordinate
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the Z coordinate
     * 
     * @return The Z coordinate
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Get the yaw
     * 
     * @return The yaw
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Get the pitch
     * 
     * @return The pitch
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Get the corresponding Bukkit Location
     * 
     * @return The Bukkit Location or null if the world is unloaded
     */
    public Location getHandle() {
        World world = Bukkit.getWorld(this.world);
        if(world == null) {
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }

    /**
     * Get the Block at this location
     * 
     * @return The Block or null if the world is unloaded
     */
    public Block getBlock() {
        World world = Bukkit.getWorld(this.world);
        if(world == null) {
            return null;
        }
        return world.getBlockAt((int)this.x, (int)this.y, (int)this.z);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(pitch);
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Float.floatToIntBits(yaw);
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof LibigotLocation)) {
            return false;
        }
        LibigotLocation other = (LibigotLocation) obj;
        return this.world.equals(other.world) &&
                this.x == other.x &&
                this.y == other.y &&
                this.z == other.z &&
                this.yaw == other.yaw &&
                this.pitch == other.pitch;
    }

    @Override
    public LibigotLocation clone() {
        return new LibigotLocation(world, x, y, z, yaw, pitch);
    }
}
