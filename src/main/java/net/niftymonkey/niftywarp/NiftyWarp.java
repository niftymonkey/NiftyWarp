package net.niftymonkey.niftywarp;

import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.DeleteWarpCommand;
import net.niftymonkey.niftywarp.commands.HomeCommand;
import net.niftymonkey.niftywarp.commands.HomeSetCommand;
import net.niftymonkey.niftywarp.commands.ListWarpsCommand;
import net.niftymonkey.niftywarp.commands.RenameWarpCommand;
import net.niftymonkey.niftywarp.commands.SetWarpTypeCommand;
import net.niftymonkey.niftywarp.commands.WarpCommand;
import net.niftymonkey.niftywarp.commands.WarpToCoordCommand;
import net.niftymonkey.niftywarp.permissions.FreeForAllAdapter;
import net.niftymonkey.niftywarp.permissions.IPermissionsAdapter;
import net.niftymonkey.niftywarp.permissions.OpsForAdminFunctionsAdapter;
import net.niftymonkey.niftywarp.permissions.OpsOnlyAdapter;
import net.niftymonkey.niftywarp.permissions.PermissionNodeMapper;
import net.niftymonkey.niftywarp.permissions.PermissionsV2Adapter;
import net.niftymonkey.niftywarp.permissions.PermissionsV3Adapter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import javax.persistence.PersistenceException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main plugin class
 *
 * User: Mark
 * Date: 6/12/11
 * Time: 10:06 PM
 */
public class NiftyWarp extends JavaPlugin
{
    private static Logger log = Logger.getLogger("Minecraft");

    public IPermissionsAdapter permissionAdapter;

    private WarpManager warpManager;
    private Configuration configuration;

    /**
     * Called when the plugin is enabled
     */
    public void onEnable()
    {
        // setup the persistence database
        setupDatabase();

        // setup config
        this.setupConfiguration();

        // setup permissions
        this.setupPermissions();

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
        getCommand(AppStrings.COMMAND_WARPTOCOORD).setExecutor(new WarpToCoordCommand(this));

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
     *
     * @return the instance of warp manager
     */
    public WarpManager getWarpManager()
    {
        return warpManager;
    }

    public IPermissionsAdapter getPermissionAdapter()
    {
        return this.permissionAdapter;
    }

    public void setPermissionAdapter(IPermissionsAdapter permissionAdapter)
    {
        this.permissionAdapter = permissionAdapter;
    }

    /**
     *
     */
    private void setupPermissions()
    {
        boolean usePermissionsPlugin = getConfiguration().getBoolean(AppStrings.PROPERTY_PERMISSION_USE_PLUGIN, true);

        // attempt to use the Permissions plugin
        if (usePermissionsPlugin)
        {
            Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
            if (permissionsPlugin != null)
            {
                String version = permissionsPlugin.getDescription().getVersion();

                if(version.startsWith("2"))
                    this.permissionAdapter = new PermissionsV2Adapter((Permissions) permissionsPlugin);
                if(version.startsWith("3"))
                    this.permissionAdapter = new PermissionsV3Adapter((Permissions) permissionsPlugin);
            }
        }
        else
        {
            String ruleset = getConfiguration().getString(AppStrings.PROPERTY_PERMISSION_RULESET);
            if(ruleset.equalsIgnoreCase(AppStrings.RULESET_FFA))
                this.permissionAdapter = new FreeForAllAdapter();
            if(ruleset.equalsIgnoreCase(AppStrings.RULESET_OPS_ONLY))
                this.permissionAdapter = new OpsOnlyAdapter();
            if(ruleset.equalsIgnoreCase(AppStrings.RULESET_OPS_FOR_ADMIN))
                this.permissionAdapter = new OpsForAdminFunctionsAdapter();
        }

        // default to the FFA permissions if no configuration could be found
        if(this.permissionAdapter == null)
        {
            // if they did specify permissions plugin usage, let's warn them that we couldn't find a proper adapter
            if(usePermissionsPlugin)
            {
                log.warning("Unable to integrate with the Permissions plugin.  Defaulting to free for all permission set");
            }

            this.permissionAdapter = new FreeForAllAdapter();
        }
    }

    /**
     * Delegates to the permissions plugin
     *
     * @param player The Player
     * @param commandRequested the command string that the user typed, for example "nwadd"
     * @param isAdminFunctionality specifies whether or not this permission check is for admin functionality.  This comes into play when
     *                             the player is attempting to do things to other people's warps.
     * @param displayDenialMessage specifies whether or not to display a message when permission is denied
     *
     * @return true if the user has permission, false if not
     */
    public boolean hasPermission(Player player, String commandRequested, boolean isAdminFunctionality, boolean displayDenialMessage)
    {
        boolean result = false;

        if (this.permissionAdapter.hasPermission(player, commandRequested, isAdminFunctionality))
        {
            result = true;
        }

        // only bother doing this if they passed in true to this method
        if(displayDenialMessage)
        {
            boolean showFailureMessage = getConfiguration().getBoolean(AppStrings.PROPERTY_MSG_SHOWPERM_FAILURE, true);

            // Notify player if permission was denied if they failed ... but only if the config says the user of this addon
            // wants these messages displayed
            if(!result && showFailureMessage)
            {
                String permissonNode = (isAdminFunctionality)?
                                       (PermissionNodeMapper.getAdminPermissionNode(commandRequested)):
                                       (PermissionNodeMapper.getPermissionNode(commandRequested));

                player.sendMessage(ChatColor.RED + AppStrings.INSUFFICIENT_PRIVELEGES_1);
                player.sendMessage(ChatColor.RED + AppStrings.INSUFFICIENT_PRIVELEGES_2);
                player.sendMessage(ChatColor.RED + permissonNode);
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