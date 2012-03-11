package net.niftymonkey.niftywarp;

import org.bukkit.Server;
import org.bukkit.World;

/**
 * User: Mark
 * Date: 7/15/11
 * Time: 12:49 AM
 */
public class NWUtils
{

    public static boolean isValidDouble(String doubleValStr)
    {
        boolean retVal = false;

        try
        {
            //Double doubleVal = Double.valueOf(doubleValStr); //was never used, will be removed in next cleanup
            retVal = true;
        }
        catch (NumberFormatException ignored)
        {
        }

        return retVal;
    }

    public static boolean isValidWorld(String worldValStr, Server server)
    {
        boolean retVal = false;

        World world = server.getWorld(worldValStr);
        if(world != null)
            retVal = true;

        return retVal;
    }
}
