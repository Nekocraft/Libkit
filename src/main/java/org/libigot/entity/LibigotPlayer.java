package org.libigot.entity;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

/**
 * Represents a player in Libigot.
 */
public interface LibigotPlayer extends HumanEntity, Conversable, CommandSender, OfflinePlayer, PluginMessageRecipient {
    
    /**
     * Gets a players ping
     *
     * @return The players ping
     */
    public int getPing();
}
