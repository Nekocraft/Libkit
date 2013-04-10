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

    /**
     * Gets the knockback strength of this Arrow which is by default added by the enchantment punch.
     * The knockback effect by impact on a entity.
     * @return knockbackStrength
     */
    public int getKnockbackStrength();

    /**
     * Sets the knockback which will be given to a entity on impact.
     * @param strength
     */
    public void setKnockbackStrength(int strength);

    /**
     * Gets if the bow was complete charged when shooting this Arrow. (Particle effect.)
     * @return critical
     */
    public boolean isCritical();

    /**
     * Sets if the arrow has a critical effect on it. (Particle effect.)
     * @param critical
     */
    public void setCritical(boolean critical);
}
