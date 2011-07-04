package net.niftymonkey.niftywarp.permissions;

import org.bukkit.entity.Player;

/**
 * Permissions adapter that everyone to have access to all commands and functionality
 */
public class FreeForAllAdapter implements IPermissionsAdapter
{
    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        return true;
    }
}
