package org.libigot.block;

import java.io.Serializable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.libigot.LibigotLocation;
import org.libigot.world.LibigotWorld;

public class BlockStorage implements Serializable {

    private static final long serialVersionUID = 5935371904387514534L;
    private final LibigotLocation loc;
    private final int id;
    private final byte data;

    /**
     * Create a BlockStorage from a Bukkit block
     * 
     * @param block The bukkit block
     */
    public BlockStorage(Block block) {
        this(new LibigotLocation(block), block.getTypeId(), block.getData());
    }

    /**
     * Create a BlockStorage from a Location, a block ID and block data
     * @param loc The Location
     * @param id The block ID
     * @param data The block data
     */
    public BlockStorage(Location loc, int id, byte data) {
        this(new LibigotLocation(loc), id, data);
    }

    /**
     * Create a BlockStorage from a SurgeLocation, a block ID and block data
     * @param loc The Location
     * @param id The block ID
     * @param data The block data
     */
    public BlockStorage(LibigotLocation loc, int id, byte data) {
        this.loc = loc;
        this.id = id;
        this.data = data;
    }

    /**
     * Create a BlockStorage from a world name, x, y and z coordinates as well as a block ID and block data
     * @param world The worlds name
     * @param x The X coordinate
     * @param y The Y coordinate
     * @param z The Z coordinate
     * @param id The block ID
     * @param data The block data
     */
    public BlockStorage(String world, int x, int y, int z, int id, byte data) {
        this(new LibigotLocation(world, x, y, z), id, data);
    }

    /**
     * Get a SurgeLocation
     * 
     * @return The SurgeLocation
     */
    public LibigotLocation getLocation() {
        return this.loc;
    }

    /**
     * Get the block ID
     * 
     * @return the ID
     */
    public int getID() {
        return this.id;
    }

    /**
     * Get the block data
     * 
     * @return the data
     */
    public byte getData() {
        return this.data;
    }

    /**
     * Set the block back in the world.
     * 
     * @return True if the block has been setted, false otherwise
     */
    public boolean set() {
        return set(true);
    }

    /**
     * Set the block back in the world.
     * 
     * @param applyPhysics Apply physics after setting the block?
     * @return True if the block has been setted, false otherwise
     */
    public boolean set(boolean applyPhysics) {
        LibigotWorld world = loc.getWorld();
        if(world == null) {
            return false;
        }
        Block block = world.getWorld().getBlockAt((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
        boolean ret = true;
        if(block.getTypeId() != this.id || block.getData() != this.data) {
            ret = block.setTypeIdAndData(this.id, this.data, applyPhysics);
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data;
        result = prime * result + id;
        result = prime * result + ((loc == null) ? 0 : loc.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof BlockStorage)) {
            return false;
        }
        BlockStorage other = (BlockStorage) obj;
        return loc.equals(other.loc) && id == other.id && data == other.data;
    }

    @Override
    public BlockStorage clone() {
        return new BlockStorage(loc, id, data);
    }
}
