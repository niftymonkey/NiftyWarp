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

    public static WarpType getTypeForString(String typeStr)
    {
        // default to private
        WarpType retVal = PRIVATE;

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
