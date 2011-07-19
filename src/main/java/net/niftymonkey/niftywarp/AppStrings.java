package net.niftymonkey.niftywarp;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * In lieu of implementing real i18n right now, I'm going to just centralize my strings here so that I can do that later
 *
 * User: Mark
 * Date: 6/13/11
 * Time: 11:16 PM
 */
public class AppStrings
{

    ///////////////////////////////////
    // commands
    ///////////////////////////////////

    public static final String COMMAND_ADD          = "nwadd";
    public static final String COMMAND_LIST         = "nwlist";
    public static final String COMMAND_DELETE       = "nwdelete";
    public static final String COMMAND_RENAME       = "nwrename";
    public static final String COMMAND_SET          = "nwset";
    public static final String COMMAND_WARP         = "nwwarp";
    public static final String COMMAND_HOME         = "nwhome";
    public static final String COMMAND_HOMESET      = "nwhomeset";
    public static final String COMMAND_WARPTOCOORD  = "nwwarptocoord";

    ///////////////////////////////////
    // message bundle lookup keys
    ///////////////////////////////////

    // info & success
    public static final String WARP_ADDED          = "WARP_ADDED";
    public static final String WARP_DELETED        = "WARP_DELETED";
    public static final String WARPED_TO           = "WARPED_TO";
    public static final String HOME_SET            = "HOME_SET";
    public static final String NO_AVAILABLE_WARPS  = "NO_AVAILABLE_WARPS";
    public static final String WARPS_YOURS         = "WARPS_YOURS";
    public static final String WARPS_OTHERS        = "WARPS_OTHERS";
    public static final String WARP_RENAMED        = "WARP_RENAMED";
    public static final String WARP_TYPE_SET       = "WARP_TYPE_SET";
    public static final String WARPED_TO_COORD     = "WARPED_TO_COORD";

    // error
    public static final String ERR_ALL_WARP_SLOTS_USED = "ERR_ALL_WARP_SLOTS_USED";
    public static final String ERR_WARP_NOT_FOUND      = "ERR_WARP_NOT_FOUND";
    public static final String ERR_INVALID_WORLD       = "ERR_INVALID_WORLD";
    public static final String ERR_INVALID_WARP_TYPE   = "ERR_INVALID_WARP_TYPE";
    public static final String ERR_INVALID_COORDINATE  = "ERR_INVALID_COORDINATE";
    public static final String ERR_PERMISSION_FAIL_1   = "ERR_PERMISSION_FAIL_1";
    public static final String ERR_PERMISSION_FAIL_2   = "ERR_PERMISSION_FAIL_2";
    public static final String ERR_REMOVE_OTHERS_WARP  = "ERR_REMOVE_OTHERS_WARP";
    public static final String ERR_RENAME_OTHERS_WARP  = "ERR_RENAME_OTHERS_WARP";
    public static final String ERR_SETTYPE_OTHERS_WARP = "ERR_SETTYPE_OTHERS_WARP";

    ///////////////////////////////////
    // log messages
    ///////////////////////////////////

    public static final String ADDON_MSG_PREFIX           = "[NiftyWarp] - ";
    public static final String DB_INSTALL_PREFIX          = "Installing database due to first time usage for:  ";
    public static final String PERM_CHECK_FAIL_LOG_PREFIX = "Failed permission check [ ";

    private static final String ENABLED_MSG_SUFFIX  = " has been enabled";
    private static final String DISABLED_MSG_SUFFIX = " has been disabled";

    ///////////////////////////////////
    // properties
    ///////////////////////////////////

    public static final String PROPERTY_MSG_SHOWPREFIX        = "messages.show-prefix";
    public static final String PROPERTY_MSG_SHOWPERM_FAILURE  = "messages.permissions.show-fail-message";
    public static final String PROPERTY_WARP_DEFAULT_WARPTYPE = "warps.default-type";
    public static final String PROPERTY_WARP_MAXWARPS         = "warps.max-warps";
    public static final String PROPERTY_PERMISSION_USE_PLUGIN = "permissions.use-plugin";
    public static final String PROPERTY_PERMISSION_RULESET    = "permissions.ruleset";

    ///////////////////////////////////
    // constants
    ///////////////////////////////////

    public static final String WARP_TYPE_PRIVATE     = "private";
    public static final String WARP_TYPE_LISTED      = "listed";
    public static final String WARP_TYPE_UNLISTED    = "unlisted";
	public static final String FQL_DELIMITER         = ".";
    public static final String HOME_WARP_NAME        = "home";
    public static final String RULESET_FFA           = "ffa";
    public static final String RULESET_OPS_ONLY      = "ops-only";
    public static final String RULESET_OPS_FOR_ADMIN = "ops-for-admin";

    ///////////////////////////////////
    // command permissions Keys
    ///////////////////////////////////

    public static final String COMMAND_ADD_PERMISSION          = "niftywarp.use.add";
    public static final String COMMAND_LIST_PERMISSION         = "niftywarp.use.list";
    public static final String COMMAND_DELETE_PERMISSION       = "niftywarp.use.delete";
    public static final String COMMAND_RENAME_PERMISSION       = "niftywarp.use.rename";
    public static final String COMMAND_SET_PERMISSION          = "niftywarp.use.set";
    public static final String COMMAND_WARP_PERMISSION         = "niftywarp.use.warp";
    public static final String COMMAND_HOME_PERMISSION         = "niftywarp.use.home";
    public static final String COMMAND_HOMESET_PERMISSION      = "niftywarp.use.homeset";
    public static final String COMMAND_WARPTOCOORD_PERMISSION  = "niftywarp.use.warptocoord";

    public static final String COMMAND_ADMIN_DELETE_PERMISSION = "niftywarp.admin.delete";
    public static final String COMMAND_ADMIN_RENAME_PERMISSION = "niftywarp.admin.rename";
    public static final String COMMAND_ADMIN_SET_PERMISSION    = "niftywarp.admin.set";


    /**
     * Gets the message that will be displayed in the server console when the addon is enabled
     *
     * @param plugin the java plugin instance
     *
     * @return an "enabled" message
     */
    public static String getEnabledMessage(JavaPlugin plugin)
    {
        String retVal = "";

        PluginDescriptionFile desc = plugin.getDescription();
        if(desc != null)
            retVal = ADDON_MSG_PREFIX + desc.getFullName() + ENABLED_MSG_SUFFIX;

        return retVal;
    }

    /**
     * Gets the message that will be displayed in the server console when the addon is disabled
     *
     * @param plugin the java plugin instance
     *
     * @return a "disabled" message
     */
    public static String getDisabledMessage(JavaPlugin plugin)
    {
        String retVal = "";

        PluginDescriptionFile desc = plugin.getDescription();
        if(desc != null)
            retVal = desc.getFullName() + DISABLED_MSG_SUFFIX;

        return retVal;
    }

    /**
     * Gets the string that will be used before all addon messages to the user.  If configured to not use the addon message prefix,
     * this will return an empty string
     *
     * @param plugin the java plugin instance
     *
     * @return a message prefix, or an empty string ... based on config
     */
    public static String getAddonMsgPrefix(JavaPlugin plugin)
    {
        // default to the constant
        String retVal = ADDON_MSG_PREFIX;

        // try to get the value out of the config.  Default to true
        boolean useAddonMessagePrefix = plugin.getConfiguration().getBoolean(PROPERTY_MSG_SHOWPREFIX, true);

        // blank out the message prefix the config was set to false
        if(!useAddonMessagePrefix)
            retVal = "";

        return retVal;
    }
}
