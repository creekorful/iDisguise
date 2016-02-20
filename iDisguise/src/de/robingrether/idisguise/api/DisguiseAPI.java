package de.robingrether.idisguise.api;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.robingrether.idisguise.disguise.Disguise;
import de.robingrether.idisguise.disguise.DisguiseType;
import de.robingrether.idisguise.management.Sounds;

/**
 * The API to hook into iDisguise. The following code returns an object:<br>
 * <code>Bukkit.getServicesManager().getRegistration(DisguiseAPI.class).getProvider();</code>
 * 
 * @since 2.1.3
 * @author RobinGrether
 */
public interface DisguiseAPI {
	
	/**
	 * Disguises a player.
	 * 
	 * @deprecated replaced by <code>disguise(OfflinePlayer, Disguise)</code>
	 * @since 3.0.1
	 * @param player the player to disguise
	 * @param disguise the disguise
	 */
	@Deprecated
	public void disguiseToAll(Player player, Disguise disguise);
	
	/**
	 * Undisguises a player.
	 * 
	 * @deprecated replaced by <code>undisguise(OfflinePlayer)</code>
	 * @since 2.1.3
	 * @param player the player to undisguise
	 */
	@Deprecated
	public void undisguiseToAll(Player player);
	
	/**
	 * Disguise a player (may be offline).<br>
	 * Calling <code>disguise(player, disguise)</code> is equivalent to calling <code>disguise(player, disguise, true)</code>.<br>
	 * This method always fires a {@linkplain DisguiseEvent} which may be cancelled by other plugins.
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player) to disguise
	 * @param disguise the disguise
	 * @return <code>false</code>, in case the {@linkplain DisguiseEvent} has been cancelled
	 */
	public boolean disguise(OfflinePlayer player, Disguise disguise);
	
	/**
	 * Disguise a player (may be offline).
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player) to disguise
	 * @param disguise the disguise
	 * @param fireEvent whether a {@linkplain DisguiseEvent} should be fired (event may be cancelled by other plugins)
	 * @return <code>false</code>, in case the {@linkplain DisguiseEvent} has been cancelled
	 */
	public boolean disguise(OfflinePlayer player, Disguise disguise, boolean fireEvent);
	
	/**
	 * Undisguise a player (may be offline).<br>
	 * Calling <code>undisguise(player)</code> is equivalent to calling <code>undisguise(player, true)</code>.<br>
	 * This method always fires an {@linkplain UndisguiseEvent} which may be cancelled by other plugins.
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player) to undisguise
	 * @return <code>false</code>, in case the {@linkplain UndisguiseEvent} has been cancelled
	 */
	public boolean undisguise(OfflinePlayer player);
	
	/**
	 * Undisguise a player (may be offline).
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player) to undisguise
	 * @param fireEvent whether an {@linkplain UndisguiseEvent} should be fired (event may be cancelled by other plugins)
	 * @return <code>false</code>, in case the {@linkplain UndisguiseEvent} has been cancelled
	 */
	public boolean undisguise(OfflinePlayer player, boolean fireEvent);
	
	/**
	 * Undisguise everyone.
	 * 
	 * @since 2.1.3
	 */
	public void undisguiseAll();
	
	/**
	 * Checks whether a player is disguised.
	 * 
	 * @deprecated replaced by <code>isDisguised(OfflinePlayer)</code>
	 * @since 2.1.3
	 * @param player the player to check
	 * @return true if disguised, false if not
	 */
	@Deprecated
	public boolean isDisguised(Player player);
	
	/**
	 * Check whether a player is disguised.
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player) to check
	 * @return <code>true</code>, if the player is disguised
	 */
	public boolean isDisguised(OfflinePlayer player);
	
	/**
	 * Gets a copy of a player's disguise.
	 * 
	 * @deprecated replaced by <code>getDisguise(OfflinePlayer)</code>
	 * @since 2.1.3
	 * @param player the player
	 * @return the disguise or null if not disguised
	 */
	@Deprecated
	public Disguise getDisguise(Player player);
	
	/**
	 * Get a copy of a player's current disguise.
	 * 
	 * @since 5.1.1
	 * @param player the player (or offline player)
	 * @return a <strong>copy</strong> of the given player's disguise
	 */
	public Disguise getDisguise(OfflinePlayer player);
	
	/**
	 * Counts the amount of online players who are disguised.
	 * 
	 * @since 2.1.3
	 * @return the counted amount
	 */
	public int getOnlineDisguiseCount();
	
	/**
	 * Gets the {@link Sounds} for a specific entity type.
	 * 
	 * @since 5.1.1
	 * @param type the entity/disguise type
	 * @return the sounds for the given entity/disguise type
	 */
	public Sounds getSoundsForEntity(DisguiseType type);
	
	/**
	 * Sets the {@link Sounds} for a specific entity type.
	 * 
	 * @since 5.1.1
	 * @param type the entity/disguise type
	 * @param sounds the sounds
	 * @return <code>true</code>, if the sounds have been set
	 */
	public boolean setSoundsForEntity(DisguiseType type, Sounds sounds);
	
	/**
	 * Indicates whether the disguised players' sounds are currently replaced.
	 * 
	 * @since 5.1.1
	 * @return <code>true</code>, if they are replaced
	 */
	public boolean isSoundsEnabled();
	
	/**
	 * Sets whether the disguised players' sounds are replaced.
	 * 
	 * @since 5.1.1
	 * @param enabled <code>true</code>, if they shall be replaced
	 */
	public void setSoundsEnabled(boolean enabled);
	
}