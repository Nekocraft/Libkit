package org.libigot.event.entity;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.potion.PotionEffect;

/**
 * This Event is called whenever a Entity gets affected by
 * a Potion effect caused from a Beacon.
 */
public class BeaconPotionEffectEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean isCancelled;
    private List<HumanEntity> humans;
    private List<PotionEffect> potions;

    /**
     * The constructor for the event.
     * 
     * @param humans A list of HumanEntities affected by this event.
     * @param potions A list of PotionEffects the entities should be affected with.
     */
    public BeaconPotionEffectEvent(List<HumanEntity> humans, List<PotionEffect> potions) {
        this.humans = (humans != null ? humans : new ArrayList<HumanEntity>());
        this.potions = (potions != null ? potions : new ArrayList<PotionEffect>());
        this.isCancelled = false;
    }

    /**
     * Sets the cancelled state of the event
     * 
     * @param true if the event should be cancelled, false otherwise.
     */
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    /**
     * Sets affected players.
     * 
     * @param humans A list of HumanEntities affected by this event.
     */
    public void setEntities(List<HumanEntity> humans) {
        this.humans = (humans != null ? humans : new ArrayList<HumanEntity>());
    }

    /**
     * Sets applied potions.
     * 
     * @param potions A list of PotionEffects the entities should be affected with.
     */
    public void setEffects(List<PotionEffect> potions) {
        this.potions = (potions != null ? potions : new ArrayList<PotionEffect>());
    }

    /**
     * Gets affected players.
     * 
     * @return Thelist of HumanEntities affected by this event.
     */
    public List<HumanEntity> getEntities() {
        return this.humans;
    }

    /**
     * Gets applied potions.
     * 
     * @return The list of PotionEffects the entities should be affected with.
     */
    public List<PotionEffect> getEffects() {
        return this.potions;
    }

    /**
     * Gets the cancelled state of the event.
     * 
     * @return true if the event should be cancelled, false otherwise.
     */
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return BeaconPotionEffectEvent.handlers;
    }
}
