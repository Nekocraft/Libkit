package org.libigot.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityAddEvent extends EntityEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;

    public EntityAddEvent(Entity entity) {
        super(entity);
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
     * Gets the entity that caused the add event
     *
     * @return The entity that caused the add event
     */
    @Override
    public Entity getEntity() {
        return this.entity;
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
        return EntityAddEvent.handlers;
    }
    
    public static HandlerList getHandlerList() {
        return EntityAddEvent.handlers;
    }
}
