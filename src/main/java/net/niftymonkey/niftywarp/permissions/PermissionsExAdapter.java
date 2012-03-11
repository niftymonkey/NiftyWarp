package net.niftymonkey.niftywarp.permissions;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import org.bukkit.entity.Player;
//import org.bukkit.permissions.Permission;

import java.util.logging.Logger;

/**
 * User: Mark
 * Date: 9/17/11
 * Time: 8:19 PM
 */
public class PermissionsExAdapter implements IPermissionsAdapter
{
    private static Logger log = Logger.getLogger("Minecraft");

    /**
     * The plugin
     */
    //private final NiftyWarp plugin; // never used, but kept in case of expansion and reference

    public PermissionsExAdapter(NiftyWarp plugin)
    {
        //this.plugin = plugin; 

        log.info(AppStrings.ADDON_MSG_PREFIX + "Integrated with PermissionsEx");
    }

    /**
     * Tests for permissions for the command requested.
     *
     * @param player             the player requesting use of the command
     * @param commandRequested   the command that was requested
     * @param adminFunctionality indicates that this is a request for admin usage of the command (for instance deleting someone else's warp)
     * @return true if permission is granted, false if not
     */
    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        String permissonNode = (adminFunctionality)?
                               (PermissionNodeMapper.getAdminPermissionNode(commandRequested)):
                               (PermissionNodeMapper.getPermissionNode(commandRequested));

        return player.hasPermission(permissonNode);
    }
}
