package net.niftymonkey.niftywarp.permissions;

//import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.AppStrings;
import org.bukkit.entity.Player;
import com.nijikokun.bukkit.Permissions.Permissions;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permissions adapter that uses Permissions version 2 signature(s)
 */
@Deprecated
public class PermissionsV2Adapter implements IPermissionsAdapter
{
    private static Logger log = Logger.getLogger("Minecraft");

    private Permissions permissionsPlugin = null; 

    public PermissionsV2Adapter(Permissions permissionsPlugin)
    {
        this.permissionsPlugin = permissionsPlugin;
        log.info(AppStrings.ADDON_MSG_PREFIX + "Integrated with " + permissionsPlugin.getDescription().getFullName());
    }

    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        boolean retVal = false;

        String permissonNode = (adminFunctionality)?
                               (PermissionNodeMapper.getAdminPermissionNode(commandRequested)):
                               (PermissionNodeMapper.getPermissionNode(commandRequested));

        if (permissonNode != null)
        {   
        	if(permissionsPlugin.getHandler().has(player, permissonNode))
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

        return retVal;
    }
}
