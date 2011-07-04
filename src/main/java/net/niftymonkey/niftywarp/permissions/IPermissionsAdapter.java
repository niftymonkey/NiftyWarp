package net.niftymonkey.niftywarp.permissions;

import org.bukkit.entity.Player;

/**
 * User: Mark Lozano
 * Date: 7/2/11
 * Time: 9:03 PM
 */
public interface IPermissionsAdapter
{
    /**
     * Tests for permissions for the command requested.
     *
     * @param player the player requesting use of the command
     * @param commandRequested the command that was requested
     * @param adminFunctionality indicates that this is a request for admin usage of the command (for instance deleting someone else's warp)
     *
     * @return true if permission is granted, false if not
     */
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality);
}
