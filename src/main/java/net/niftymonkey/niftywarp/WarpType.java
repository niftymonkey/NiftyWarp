package net.niftymonkey.niftywarp;

import org.bukkit.ChatColor;

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
    PUBLIC_LISTED,

    // Only the owner of this warp will see it in their list, however others can still use this warp
    PUBLIC_UNLISTED;

    /**
     * Returns the default warp type to use (when a warp type is not specified or when unable to resolve warp type)
     *
     * @return the default warp type
     */
    public static WarpType getDefaultWarpType()
    {
        // TODO:  this is where we should probably have a config item
        return PUBLIC_UNLISTED;
    }

    /**
     * Gets the warp type for a given string
     *
     * @param typeStr the string representation of the warp type
     *
     * @return the warp type that matches the string, or null if none found
     * @see #getDefaultWarpType()
     */
    public static WarpType getTypeForString(String typeStr)
    {
        WarpType retVal = null;

        if(typeStr.equals(AppStrings.WARP_TYPE_PRIVATE))
            retVal = PRIVATE;
        if(typeStr.equals(AppStrings.WARP_TYPE_LISTED))
            retVal = PUBLIC_LISTED;
        if(typeStr.equals(AppStrings.WARP_TYPE_UNLISTED))
            retVal = PUBLIC_UNLISTED;

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
        if(this.equals(PUBLIC_UNLISTED))
            retVal = ChatColor.DARK_PURPLE;

        return retVal;
    }
}
