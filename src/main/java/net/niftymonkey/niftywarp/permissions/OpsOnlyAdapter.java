package net.niftymonkey.niftywarp.permissions;

import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Permissions adapter that allows only Ops to use the commands
 */
public class OpsOnlyAdapter implements IPermissionsAdapter
{
    private static Logger log = Logger.getLogger("Minecraft");

    @Override
    public boolean hasPermission(Player player, String commandRequested, boolean adminFunctionality)
    {
        boolean retVal = false;

        if(player.isOp())
            retVal = true;
        else
        {
            if(log.isLoggable(Level.INFO))
            {
                log.info(player.getDisplayName() + " is not an Op and consequently cannot use the command: " + commandRequested);
            }
        }

        return retVal;
    }
}
