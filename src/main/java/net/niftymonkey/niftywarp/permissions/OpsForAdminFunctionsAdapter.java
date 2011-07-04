package net.niftymonkey.niftywarp.permissions;

import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permissions adapter that allows everyone to have the regular command access
 * but only allows Ops to have access to admin functionality
 */
public class OpsForAdminFunctionsAdapter implements IPermissionsAdapter
{
    private static Logger log = Logger.getLogger("Minecraft");

    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        boolean retVal = true;

        if (adminFunctionality && !player.isOp())
        {
            retVal = false;

            if(log.isLoggable(Level.INFO))
            {
                log.info(player.getDisplayName() + " is not an Op and consequently cannot admin-use the command: " + commandRequested);
            }
        }

        return retVal;
    }
}
