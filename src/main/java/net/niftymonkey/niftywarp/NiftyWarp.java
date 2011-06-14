package net.niftymonkey.niftywarp;

import java.util.logging.Logger;

import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.ListWarpsCommand;
import net.niftymonkey.niftywarp.commands.WarpCommand;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class
 *
 * User: Mark Lozano
 * Date: 6/12/11
 * Time: 10:06 PM
 */
public class NiftyWarp extends JavaPlugin
{
    private static Logger log = Logger.getLogger("Minecraft");

    private WarpManager warpManager;

    public void onEnable()
    {
        // register commands
        getCommand(AppStrings.COMMAND_WARP).setExecutor(new WarpCommand(this));
        getCommand(AppStrings.COMMAND_ADD_WARP).setExecutor(new AddWarpCommand(this));
        getCommand(AppStrings.COMMAND_LIST_WARPS).setExecutor(new ListWarpsCommand(this));

        // create the warp manager
        warpManager = new WarpManager();

        log.info(AppStrings.getEnabledMessage(this));
    }

    public void onDisable()
    {
        log.info(AppStrings.getDisabledMessage(this));
    }

    public WarpManager getWarpManager()
    {
        return warpManager;
    }
}
