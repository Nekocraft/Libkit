package org.bukkit.entity;

/**
 * Represents an arrow.
 */
public interface Arrow extends Projectile {
    
    /**
     * Gets the base damage this Arrow deals when it strikes.
     * Arrow damage is baseDamage * norm of the velocity vector (roughly).
     * Base damage may be higher than the default if the arrow was shot with an enchanted bow.
     * @return damage
     */
    public double getDamage();
    
    /**
     * Sets the base damage the arrow will deal when it lands.  Arrow damage is always modified by the velocity of the projectile.
     * Arrow damage is baseDamage * norm of the velocity vector (roughly).
     * @param damage
     */
    public void setDamage(double damage);
}
