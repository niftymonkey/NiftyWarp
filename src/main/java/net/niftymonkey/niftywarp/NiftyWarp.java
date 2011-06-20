package net.niftymonkey.niftywarp;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.AdminCommand;
import net.niftymonkey.niftywarp.commands.DeleteWarpCommand;
import net.niftymonkey.niftywarp.commands.ListWarpsCommand;
import net.niftymonkey.niftywarp.commands.RenameWarpCommand;
import net.niftymonkey.niftywarp.commands.SetWarpTypeCommand;
import net.niftymonkey.niftywarp.commands.WarpCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.logging.Level;
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

    public PermissionHandler permissionHandler;

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

        // setup permissions
        this.setupPermissions();

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

    public PermissionHandler getPermissionHandler()
    {
        return this.permissionHandler;
    }

    /**
     * Courtesy of the example:
     * https://github.com/TheYeti/Permissions/wiki/API-Reference
     */
    private void setupPermissions()
    {
      Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");

      if (this.permissionHandler == null) {
          if (permissionsPlugin != null) {
              this.permissionHandler = ((Permissions) permissionsPlugin).getHandler();
          } else {
              log.info("Permission system not detected, defaulting to OP");
          }
      }
    }

    /**
     * Delegates to the permissions plugin
     * @param inPlayer The Player
     * @param inKey The string that represents the permission key, for example "net.niftymonkey.niftywarp.addwarp"
     * @return
     */
    public boolean hasPermission(Player inPlayer, String inKey, String inCommandString)
    {
        boolean result = false;
        String worldName = null;
        String playerName = null;

        if(inPlayer != null && inKey != null)
        {
            worldName = inPlayer.getWorld().getName();
            playerName = inPlayer.getName();

            if (this.permissionHandler.has(worldName, playerName, inKey))
            {
                result = true;
            }
            if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): Checking permission for " +
                    "player = '"  + playerName + "', \nkey = '" + inKey +
                    ", \ncommand = '" + inCommandString +
                    "', \nresult = " + result);
        }
        else
        {
            if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): Player or key was NULL");
        }
        //--Notify player if permission was denied--
        if(!result)
        {
            String usrMsg = MessageFormat.format(AppStrings.INSUFFICIENT_PRIVELEGES_1, inCommandString);
            //if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): usrMsg1 = " + usrMsg);
            inPlayer.sendMessage(ChatColor.RED + usrMsg);
            usrMsg = MessageFormat.format(AppStrings.INSUFFICIENT_PRIVELEGES_2, inKey);
            //if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): usrMsg2 = " + usrMsg);
            inPlayer.sendMessage(ChatColor.RED + usrMsg);
            inPlayer.sendMessage(ChatColor.RED + inKey);
        }
        return result;
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
            System.out.println(AppStrings.DB_INSTALL_PREFIX + getDescription().getName());
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

