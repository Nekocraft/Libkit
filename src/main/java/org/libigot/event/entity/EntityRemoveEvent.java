package org.libigot.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

/**
 * This Event is called whenever a Entity is removed
 * from a world.
 */
public class EntityRemoveEvent extends EntityEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * The constructor for the event.
     *
     * @param entity The entity that gets removed.
     */
    public EntityRemoveEvent(Entity entity) {
        super(entity);
    }

    /**
     * Gets the entity that caused the aremove event.
     *
     * @return The entity that caused the remove event.
     */
    @Override
    public Entity getEntity(){
        return this.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return EntityRemoveEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return EntityRemoveEvent.handlers;
    }
}
