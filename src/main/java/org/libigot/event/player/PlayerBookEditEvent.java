package org.libigot.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.meta.BookMeta;

/**
 * Called when a player edits the text in a book and quill item.
 */
public class PlayerBookEditEvent extends PlayerBookEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private boolean cancel = false;

    public PlayerBookEditEvent(Player player, int slot, BookMeta oldBookMeta, BookMeta newBookMeta) {
        super(player, slot, oldBookMeta, newBookMeta);
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
