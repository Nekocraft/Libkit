package org.libigot.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;

public class EntityRemoveEvent extends EntityEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public EntityRemoveEvent(Entity entity) {
        super(entity);
    }
    
    /**
     * Gets the entity that caused the add event
     * 
     * @return The entity that caused the add event
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
