package net.niftymonkey.niftywarp.permissions;

import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.AppStrings;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permissions adapter that uses Permissions version 3 signature(s)
 */
public class PermissionsV3Adapter implements IPermissionsAdapter
{
    private static Logger log = Logger.getLogger("Minecraft");

    private Permissions permissionsPlugin = null;

    public PermissionsV3Adapter(Permissions permissionsPlugin)
    {
        this.permissionsPlugin = permissionsPlugin;
        log.info(AppStrings.ADDON_MSG_PREFIX + "Integrated with " + permissionsPlugin.getDescription().getFullName());
    }

    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        boolean retVal = false;

        String worldName = player.getWorld().getName();
        String playerName = player.getDisplayName();
        String permissonNode = (adminFunctionality)?
                               (PermissionNodeMapper.getAdminPermissionNode(commandRequested)):
                               (PermissionNodeMapper.getPermissionNode(commandRequested));

        if (permissonNode != null)
        {
            try
            {
                if(permissionsPlugin.getHandler().has(worldName, playerName, permissonNode))
                {
                    retVal = true;
                }
                else
                {
                    // let's only log permission check on failure to reduce logfile chattiness
                    if (log.isLoggable(Level.INFO))
                    {
                        log.info(AppStrings.PERM_CHECK_FAIL_LOG_PREFIX +
                                 "player:" + player.getDisplayName() + ", " +
                                 "key:" + permissonNode + ", " +
                                 "command:" + commandRequested + " ]");
                    }
                }
            }
            catch (NullPointerException e)
            {
                log.warning("Exception thrown while attempting to check permission node.");
                if(permissionsPlugin.getDescription().getVersion().startsWith("3.0"))
                {
                    String badPermissionsVersion = "You're using a buggy version of Permissions.  " +
                            "See bukkit.org forum post for NiftyWarp for a possible solution.";
                    log.warning(badPermissionsVersion);
                }
            }
        }

        return retVal;
    }
}
