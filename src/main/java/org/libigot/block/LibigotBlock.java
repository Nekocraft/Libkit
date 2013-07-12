package org.libigot.block;

import org.bukkit.block.Block;
import org.libigot.LibigotLocation;

public interface LibigotBlock {

    public void forcePhysicUpdate();

    /**
     * Gets the Bukkit block.
     *
     * @return The Bukkit block.
     */
    public Block getBlock();

    /**
     * Gets a BlockStorage from the current block state.
     *
     * @return The BlockStorage.
     */
    public BlockStorage toStorage();

    /**
     * Gets the location of the block.
     *
     * @return The location.
     */
    public LibigotLocation getLocation();
}
