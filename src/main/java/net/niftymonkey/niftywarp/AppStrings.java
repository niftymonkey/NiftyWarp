package net.niftymonkey.niftywarp;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * In lieu of implementing real i18n right now, I'm going to just centralize my strings here so that I can do that later
 *
 * User: Mark Lozano
 * Date: 6/13/11
 * Time: 11:16 PM
 */
public class AppStrings
{
    // commands
    public static final String COMMAND_ADD      = "nwadd";
    public static final String COMMAND_ADMIN    = "nwadmin";
    public static final String COMMAND_LIST     = "nwlist";
    public static final String COMMAND_DELETE   = "nwdelete";
    public static final String COMMAND_RENAME   = "nwrename";
    public static final String COMMAND_SET      = "nwset";
    public static final String COMMAND_WARP     = "nwwarp";

    // message strings (public)
    public static final String WARPED_TO_PREFIX             = "Warped to:  ";
    public static final String WARP_NOT_FOUND_PREFIX        = "No warp found for name:  ";
    public static final String WARP_ADDED_PREFIX            = "Added warp: ";
    public static final String WARP_REMOVED_PREFIX          = "Removed warp: ";
    public static final String WARP_RENAMED_PREFIX          = "Renamed warp: ";
    public static final String WARP_SET_PREFIX              = "Type set for warp: ";
    public static final String AVAILABLE_WARPS_PREFIX       = "Available warps: ";
    public static final String NO_AVAILABLE_WARPS           = "No warps found.";
    public static final String DB_INSTALL_PREFIX        = "Installing database due to first time usage for:  ";
    public static final String INSUFFICIENT_PRIVELEGES_1    = "Sorry, you don't have permission to use '{0}'.";
    public static final String INSUFFICIENT_PRIVELEGES_2    = "Please ask your server administrator about permission:";

    // message strings (private)
    private static final String ADDON_MSG_PREFIX    = "[NiftyWarp] - ";
    private static final String ENABLED_MSG_SUFFIX  = " has been enabled";
    private static final String DISABLED_MSG_SUFFIX = " has been disabled";

    // properties
    private static final String PROPERTY_MSG_SHOWPREFIX = "messages.showprefix";

    // constants
    public static final String WARP_TYPE_PRIVATE  = "private";
    public static final String WARP_TYPE_LISTED   = "listed";
    public static final String WARP_TYPE_UNLISTED = "unlisted";
	public static final String FQL_DELIMITER      = ".";

    // command permissions Keys
    public static final String COMMAND_ADD_PERMISSION      = "net.niftymonkey.niftywarp.nwadd";
    public static final String COMMAND_ADMIN_PERMISSION    = "net.niftymonkey.niftywarp.nwadmin";
    public static final String COMMAND_LIST_PERMISSION     = "net.niftymonkey.niftywarp.nwlist";
    public static final String COMMAND_DELETE_PERMISSION   = "net.niftymonkey.niftywarp.nwdelete";
    public static final String COMMAND_RENAME_PERMISSION   = "net.niftymonkey.niftywarp.nwrename";
    public static final String COMMAND_SET_PERMISSION      = "net.niftymonkey.niftywarp.nwset";
    public static final String COMMAND_WARP_PERMISSION     = "net.niftymonkey.niftywarp.nwwarp";


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
            retVal = desc.getFullName() + ENABLED_MSG_SUFFIX;

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
