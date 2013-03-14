package org.libigot.world;

import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.libigot.world.LibigotEffect;

/**
 * Represents a world in Libigot.
 */
public interface LibigotWorld  {
    
    /**
     * Sets the render distance for this server.
     *
     * @param render The distance: integer from 3 to 15
     */
    public void setRenderDistance(int render);

    /**
     * Fetches the render distance for this world.
     *
     * @return The render distance.
     */
    public int getRenderDistance();

    /**
     * Plays a particle effect.
     *
     * @param loc The location to play at.
     * @param effect The effect to play.
     */
    public void playEffect(Location loc, LibigotEffect effect);

    /**
     * Plays a particle effect.
     *
     * @param x X location.
     * @param y Y location.
     * @param z Z location.
     * @param effect The effect to play.
     */
    public void playEffect(double x, double y, double z, LibigotEffect effect);

    /**
     * Plays a particle effect.
     *
     * @param loc The location to play at.
     * @param effect The effect to play.
     * @param vec The velocity vector of the effect.
     */
    public void playEffect(Location loc, LibigotEffect effect, Vector vec);

    /**
     * Plays a particle effect.
     *
     * @param x X location.
     * @param y Y location.
     * @param z Z location.
     * @param effect The effect to play.
     * @param vec The velocity vector of the effect.
     */
    public void playEffect(double x, double y, double z, LibigotEffect effect, Vector vec);

    /**
     * Gets the Bukkit world.
     * @return The Bukkit world.
     */
    public World getWorld();
}
