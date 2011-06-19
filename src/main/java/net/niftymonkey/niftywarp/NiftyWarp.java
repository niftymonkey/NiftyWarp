package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.AdminCommand;
import net.niftymonkey.niftywarp.commands.DeleteWarpCommand;
import net.niftymonkey.niftywarp.commands.ListWarpsCommand;
import net.niftymonkey.niftywarp.commands.RenameWarpCommand;
import net.niftymonkey.niftywarp.commands.SetWarpTypeCommand;
import net.niftymonkey.niftywarp.commands.WarpCommand;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
        // setup the persistence database
        setupDatabase();

        // create the warp manager
        warpManager = new WarpManager(getDatabase());

        // register commands
        getCommand(AppStrings.COMMAND_ADD).setExecutor(new AddWarpCommand(this));
        getCommand(AppStrings.COMMAND_ADMIN).setExecutor(new AdminCommand(this));
        getCommand(AppStrings.COMMAND_LIST).setExecutor(new ListWarpsCommand(this));
        getCommand(AppStrings.COMMAND_DELETE).setExecutor(new DeleteWarpCommand(this));
        getCommand(AppStrings.COMMAND_RENAME).setExecutor(new RenameWarpCommand(this));
        getCommand(AppStrings.COMMAND_SET).setExecutor(new SetWarpTypeCommand(this));
        getCommand(AppStrings.COMMAND_WARP).setExecutor(new WarpCommand(this));

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

    /**
     * Sets up the database
     */
    private void setupDatabase()
    {
        try
        {
            getDatabase().find(Warp.class).findRowCount();
        }
        catch (PersistenceException ex)
        {
            System.out.println("Installing database for " + getDescription().getName() + " due to first time usage");
            installDDL();
        }
    }

    @Override
    /**
     * Gets the database classes
     */
    public List<Class<?>> getDatabaseClasses()
    {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(Warp.class);
        return list;
    }
}