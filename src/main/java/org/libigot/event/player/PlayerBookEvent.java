package org.libigot.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.meta.BookMeta;

public abstract class PlayerBookEvent extends PlayerEvent {
    private BookMeta oldBookMeta;
    private BookMeta newBookMeta;
    private int slot;

    public PlayerBookEvent(Player who, int slot, BookMeta oldBookMeta, BookMeta newBookMeta) {
        super(who);
        this.oldBookMeta = oldBookMeta;
        this.newBookMeta = newBookMeta;
        this.slot = slot;
    }

    /**
     * Gets the book meta data currently on the book
     * <p />
     * Note: this is a copy of the book meta data.  You cannot use this object to
     * change the existing book meta data.
     * 
     * @return The book meta data currently on the book
     */
    public BookMeta getOldBookMeta() {
        return oldBookMeta.clone();
    }

    /**
     * Gets the book meta data that the player is attempting to add to the book
     * <p />
     * Note: this is a copy of the proposed new book meta data.  Use {@link #setNewBookMeta(BookMeta)}
     * to change what will actually be added to the book.
     * 
     * @return The book meta data that the player is attempting to add
     */
    public BookMeta getNewBookMeta() {
        return newBookMeta.clone();
    }

    /**
     * Gets the inventory slot number for the book item that triggered this event
     * <p />
     * This is a slot number on the player's hotbar in the range 0-8.
     * 
     * @return The inventory slot number that the book item occupies
     */
    public int getSlot() {
        return slot;
    }
    
    /**
     * Sets the book meta data that will actually be added to the book
     * 
     * @param bookMeta New book meta data
     */
    public void setNewBookMeta(BookMeta newBookMeta) {
        this.newBookMeta = newBookMeta.clone();
    }
}
