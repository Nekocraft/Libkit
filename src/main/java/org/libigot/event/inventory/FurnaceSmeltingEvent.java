package org.libigot.event.inventory;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Called when an item is being smelted.
 */
public class FurnaceSmeltingEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private ItemStack source;
    private int cookTime;
    private boolean cancelled;
    
    public FurnaceSmeltingEvent(final Block furnace, final ItemStack source, final int cookTime) {
        super(furnace);
        this.source = source;
        this.cookTime = cookTime;
        this.cancelled = false;
    }
    
    /**
     * Gets the smelting ItemStack for this event
     *
     * @return smelting source ItemStack
     */
    public ItemStack getSource() {
        return source;
    }
    
    /**
     * Gets the cooktime for this item
     * 
     * @return cookTime the current cooking time for the item
     */
    public int getCookTime() {
        return cookTime;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
