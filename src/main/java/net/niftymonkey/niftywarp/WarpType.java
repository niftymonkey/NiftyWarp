package net.niftymonkey.niftywarp;

import org.bukkit.ChatColor;
import org.bukkit.util.config.Configuration;

/**
 * User: Mark Lozano
 * Date: 6/18/11
 * Time: 12:42 AM
 */
public enum WarpType
{
    // only the owner of this warp can see it in their list, and only the owner can use this warp
    PRIVATE,

    // Anyone can see this warp in their list
    LISTED,

    // Only the owner of this warp will see it in their list, however others can still use this warp
    UNLISTED;

    /**
     * Returns the default warp type to use (when a warp type is not specified or when unable to resolve warp type)
     *
     * @return the default warp type
     * @param configuration the configuration object to use to get the default type
     */
    public static WarpType getDefaultWarpType(Configuration configuration)
    {
        WarpType retVal;

        // grab the default type from the config, defaulting to unlisted
        String type = configuration.getString(AppStrings.PROPERTY_WARP_DEFAULT_WARPTYPE, AppStrings.WARP_TYPE_UNLISTED);
        // get the type for the string specified
        retVal = getTypeForString(type);

        // handle the case where they put in an invalid value
        if(retVal == null)
            retVal = UNLISTED;

        return retVal;
    }

    /**
     * Gets the warp type for a given string
     *
     * @param typeStr the string representation of the warp type
     *
     * @return the warp type that matches the string, or null if none found
     * @see #getDefaultWarpType(org.bukkit.util.config.Configuration)
     */
    public static WarpType getTypeForString(String typeStr)
    {
        WarpType retVal = null;

        if(typeStr.equals(AppStrings.WARP_TYPE_PRIVATE))
            retVal = PRIVATE;
        if(typeStr.equals(AppStrings.WARP_TYPE_LISTED))
            retVal = LISTED;
        if(typeStr.equals(AppStrings.WARP_TYPE_UNLISTED))
            retVal = UNLISTED;

        return retVal;
    }

    /**
     * Gets the ChatColor for the type of Warp this is
     *
     * @return the relevant ChatColor
     */
    public ChatColor getTypeColor()
    {
        ChatColor retVal = ChatColor.WHITE;

        if(this.equals(PRIVATE))
            retVal = ChatColor.DARK_GRAY;
        if(this.equals(UNLISTED))
            retVal = ChatColor.DARK_PURPLE;

        return retVal;
    }
}
