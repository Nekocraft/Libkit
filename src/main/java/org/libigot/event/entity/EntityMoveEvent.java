package org.libigot.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityMoveEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private Location from;
    private Location to;

    public EntityMoveEvent(final Entity entity, final Location from, final Location to) {
        super(entity);
        this.from = from;
        this.to = to;
        this.isCancelled = false;
    }
    
    /**
     * Sets the cancelled state of the event
     *
     * @param isCancelled The new cancel state
     */
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
    
    /**
     * Sets the entities new location
     *
     * @param to The new location of the entity
     */
    public void setTo(Location to) {
        this.to = to;
    }

    /**
     * Sets the entities old location
     *
     * @param from The old location of the entity
     */
    public void setFrom(Location from) {
        this.from = from;
    }

    /**
     * Gets the entity that caused the move event
     *
     * @return The entity that caused the move event
     */
    @Override
    public Entity getEntity() {
        return this.entity;
    }

    /**
     * Gets the location this entity moved from
     *
     * @return Location the entity moved from
     */
    public Location getFrom() {
        return this.from;
    }

    /**
     * Gets the location this entity moved to
     *
     * @return Location the entity moved to
     */
    public Location getTo() {
        return this.to;
    }

    /**
     * Gets the cancelled state of the event
     *
     * @return The cancelled state
     */
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityMoveEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return EntityMoveEvent.handlers;
    }
}
