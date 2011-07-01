package net.niftymonkey.niftywarp;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.DeleteWarpCommand;
import net.niftymonkey.niftywarp.commands.HomeCommand;
import net.niftymonkey.niftywarp.commands.HomeSetCommand;
import net.niftymonkey.niftywarp.commands.ListWarpsCommand;
import net.niftymonkey.niftywarp.commands.RenameWarpCommand;
import net.niftymonkey.niftywarp.commands.SetWarpTypeCommand;
import net.niftymonkey.niftywarp.commands.WarpCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
    private Configuration configuration;
    private boolean permissionsPluginEnabled;

    /**
     * Called when the plugin is enabled
     */
    public void onEnable()
    {
        // setup the persistence database
        setupDatabase();

        // setup permissions
        this.setupPermissions();

        // setup config
        this.setupConfiguration();

        // create the warp manager
        warpManager = new WarpManager(this);

        // register commands
        getCommand(AppStrings.COMMAND_ADD).setExecutor(new AddWarpCommand(this));
        getCommand(AppStrings.COMMAND_LIST).setExecutor(new ListWarpsCommand(this));
        getCommand(AppStrings.COMMAND_DELETE).setExecutor(new DeleteWarpCommand(this));
        getCommand(AppStrings.COMMAND_HOME).setExecutor(new HomeCommand(this));
        getCommand(AppStrings.COMMAND_HOMESET).setExecutor(new HomeSetCommand(this));
        getCommand(AppStrings.COMMAND_RENAME).setExecutor(new RenameWarpCommand(this));
        getCommand(AppStrings.COMMAND_SET).setExecutor(new SetWarpTypeCommand(this));
        getCommand(AppStrings.COMMAND_WARP).setExecutor(new WarpCommand(this));

        // log enable success to console
        log.info(AppStrings.getEnabledMessage(this));
    }

    /**
     * Called when the plugin is disabled
     */
    public void onDisable()
    {
        log.info(AppStrings.getDisabledMessage(this));
    }

    /**
     * Gets the WarpManager object to be used for doing any warp-related tasks
     * @return
     */
    public WarpManager getWarpManager()
    {
        return warpManager;
    }

    public PermissionHandler getPermissionHandler()
    {
        return this.permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler)
    {
        this.permissionHandler = permissionHandler;
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
              permissionsPluginEnabled = true;
          } else {
              log.info("Permission system not detected, defaulting to OP");
              permissionsPluginEnabled = false;
          }
      }
    }

    /**
     * Indicates whether or not the permission plugin is enabled
     *
     * @return true if permissions should be used, false if not
     */
    public boolean isPermissionsPluginEnabled()
    {
        return permissionsPluginEnabled;
    }

    /**
     * Delegates to the permissions plugin
     * @param inPlayer The Player
     * @param inKey The string that represents the permission key, for example "niftywarp.add"
     * @param inCommandString the command string that the user typed, for example "nwadd"
     *
     * @return true if the user has permission, false if not
     */
    public boolean hasPermission(Player inPlayer, String inKey, String inCommandString)
    {
        return hasPermission(inPlayer, inKey, inCommandString, true);
    }

    /**
     * Delegates to the permissions plugin
     *
     * @param inPlayer The Player
     * @param inKey The string that represents the permission key, for example "niftywarp.add"
     * @param inCommandString the command string that the user typed, for example "nwadd"
     * @param displayDenialMessage specifies whether or not to display a message when permission is denied
     *
     * @return true if the user has permission, false if not
     */
    public boolean hasPermission(Player inPlayer, String inKey, String inCommandString, boolean displayDenialMessage)
    {
        boolean result = false;

        if(isPermissionsPluginEnabled())
        {
            String worldName = null;
            String playerName = null;

            if(inPlayer != null && inKey != null)
            {
                worldName = inPlayer.getWorld().getName();
                playerName = inPlayer.getName();

                if (this.permissionHandler.has(inPlayer, inKey))
                {
                    result = true;
                }

                // let's only log permission check on failure to reduce logfile chattiness
                if(!result)
                {
                    if(log.isLoggable(Level.INFO))
                    {
                        log.info(AppStrings.PERM_CHECK_FAIL_LOG_PREFIX +
                                 "player:"  + playerName + ", " +
                                 "key:" + inKey + ", " +
                                 "command:" + inCommandString + " ]");
                    }
                }

                // only bother doing this if they passed in true to this method
                if(displayDenialMessage)
                {
                    boolean showFailureMessage = getConfiguration().getBoolean(AppStrings.PROPERTY_MSG_SHOWPERM_FAILURE, true);
                    //--Notify player if permission was denied if they failed and the config says the user of this addon
                    // wants these messages displayed --
                    if(!result && showFailureMessage)
                    {
                        String usrMsg = MessageFormat.format(AppStrings.INSUFFICIENT_PRIVELEGES_1, inCommandString);
                        //if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): usrMsg1 = " + usrMsg);
                        inPlayer.sendMessage(ChatColor.RED + usrMsg);
                        usrMsg = MessageFormat.format(AppStrings.INSUFFICIENT_PRIVELEGES_2, inKey);
                        //if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): usrMsg2 = " + usrMsg);
                        inPlayer.sendMessage(ChatColor.RED + usrMsg);
                        inPlayer.sendMessage(ChatColor.RED + inKey);
                    }
                }
            }
            else
            {
                if(log.isLoggable(Level.WARNING)) log.warning("hasPermission(): Player or key was NULL");
            }
        }
        else // if the permissions plugin isn't enabled, only ops can warp
        {
            if(inPlayer.isOp())
                result = true;
            else
            {
                if(log.isLoggable(Level.INFO))
                {
                    log.info(inPlayer.getDisplayName() + " is not an Op and consequently cannot use the command: " + inCommandString);
                }
            }
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

    /**
     * Sets up the configuration file that will be used for this addon.  If it does not exist, the file will be created. 
     */
    private void setupConfiguration()
    {
        try
        {
            File configFile = new File("./plugins/NiftyWarp/NiftyWarp.yml");
            boolean fileExists = configFile.exists();
            if(!fileExists)
                fileExists = configFile.createNewFile();

            if(fileExists)
            {
                configuration = new Configuration(configFile);
                configuration.load();
            }
        }
        catch (IOException e)
        {
            log.warning(e.getMessage());
        }
    }

    void setConfiguration(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConfiguration()
    {
        return configuration;
    }
}