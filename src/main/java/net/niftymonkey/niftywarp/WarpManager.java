package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.exceptions.InternalPermissionsException;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to handle the loading, saving, and retrieval of warps
 *
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 1:18 AM
 */
public class WarpManager
{
    private final NiftyWarp plugin;

    private IPersistenceProvider warpProvider;

    
    public WarpManager(NiftyWarp niftyWarp)
    {
        this.plugin = niftyWarp;

        // create the warp provider
        warpProvider = new EbeanServerPersistenceProvider(plugin.getDatabase());
    }

    /**
     * Gets the warp provider instance
     * 
     * @return the warp provider implementation we're using
     */
    public IPersistenceProvider getWarpProvider()
    {
        return warpProvider;
    }

    /**
     * Gets the list of warps for the user
     *
     * @param playerName the name of the player whose warps should be listed
     * @param requestingPlayer the name of the player requesting this list
     *
     * @return a list of warps that the player in the playerName parameter can use
     */
    public List<Warp> getWarpsForUser(String playerName, Player requestingPlayer)
    {
        return getWarpsForUser(playerName, requestingPlayer, getWarpProvider());
    }

    /**
     * Gets the list of warps for the user
     *
     * @param playerName the name of the player whose warps should be listed
     * @param requestingPlayer the name of the player requesting this list
     * @param warpProvider the warp provider class
     *
     * @return a list of warps that the player in the playerName parameter can use
     */
    public List<Warp> getWarpsForUser(String playerName, Player requestingPlayer, IPersistenceProvider warpProvider)
    {
        List<Warp> retVal = new ArrayList<Warp>();
        List<Warp> warpsFromDB = warpProvider.getAllWarps();

        for (Warp warp : warpsFromDB)
        {
            // if this is a public listed warp, it goes in everyone's list
            if(warp.getWarpType() == WarpType.PUBLIC_LISTED)
            {
                retVal.add(warp);
            }
            else // if it's private or unlisted, only the owner should see it
            {
                if(warp.getOwner().equals(playerName))
                {
                    retVal.add(warp);
                }
            }
        }

        return retVal;
    }

    /**
     * Gets a named warp object from persistence
     *
     * @param warpName the name of the warp to get
     * @param requestingPlayer the player who is requesting this warp
     *
     * @return a warp object
     */
    public Warp getWarp(String warpName, Player requestingPlayer)
    {
        Warp retVal = null;

        if(warpName != null && requestingPlayer != null)
        {
            // if the warp name contains a dot, then this is the fully qualified name, so we can use it as-is
            if(warpName.contains("."))
            {
                retVal = plugin.getDatabase().find(Warp.class).where().ieq("fullyQualifiedName", warpName).findUnique();
                // we need to null this back out if this player doesn't have access to this warp
                if(!retVal.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName()) &&
                   retVal.getWarpType() == WarpType.PRIVATE)
                {
                    retVal = null;
                }
            }
            else // if it doesn't have a dot, the user didn't specify an owner, so default to self and build the fully qualified name
            {
                // get the list of warps from the db that have the name requested
                List<Warp> warpList = plugin.getDatabase().find(Warp.class).where().ieq("name", warpName).findList();
                // if there's more than one, iterate through until we find the one owned by the player
                if(warpList.size() > 1)
                {
                    for (Warp warp : warpList)
                    {
                        if(warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName()))
                        {
                            retVal = warp;
                            break;
                        }
                    }
                }
                else if(warpList.size() == 1)
                {
                    Warp warp = warpList.get(0);
                    // since we're looking up by name only (not fully qualified), let's make sure we don't return a private one
                    if(warp.getWarpType() != WarpType.PRIVATE)
                        retVal = warp;
                }
            }
        }

        return retVal;
    }

    /**
     * Adds a warp to the list using the supplied parameters
     *
     *
     * @param warpName the name of the warp
     * @param owner the player creating this warp
     * @param warpType the warpType for this warp
     * @param location the {@link org.bukkit.Location} object that represents this warp
     *
     * @return the Warp that was created
     */
    public Warp addWarp(String warpName, Player owner, WarpType warpType, Location location)
    {
        // if one exists, delete it
        // NOTE: doing this for now because save() doesn't seem to be updating existing warps
        Warp retVal = getWarp(warpName, owner);
        plugin.getDatabase().delete(retVal);

        // now let's make a new one
        retVal = new Warp();
        retVal.setName(warpName);
        retVal.setOwner(owner.getDisplayName());
        retVal.setWarpType(warpType);
        retVal.setLocation(location);

        plugin.getDatabase().save(retVal);

        return retVal;
    }

    /**
     * Deletes a warp from the list
     *
     * @param warpName the name of the warp
     * @param requestingPlayer the player requesting this action
     *
     * @return true if deleted, false if not
     *
     * @throws InternalPermissionsException if a method required permissions that the requesting player does not have
     */
    public boolean deleteWarp(String warpName, Player requestingPlayer)
        throws InternalPermissionsException
    {
        boolean retVal = false;

        if(warpName != null && requestingPlayer != null)
        {
            Warp warp = getWarp(warpName, requestingPlayer);
            if(warp != null)
            {
                boolean isOwner = warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName());
                boolean hasAdminDelete = plugin.hasPermission(requestingPlayer,
                                                              AppStrings.COMMAND_ADMIN_DELETE_PERMISSION,
                                                              AppStrings.COMMAND_DELETE,
                                                              false);

                // either they're deleting their own, or they aren't but they have the admin delete priv
                if( isOwner || hasAdminDelete )
                {
                    plugin.getDatabase().delete(warp);
                    retVal = true;
                }
                else
                    throw new InternalPermissionsException(AppStrings.WARP_CANNOT_REMOVE_OTHERS);
            }
        }

        return retVal;
    }

    /**
     * Renames a warp in the system.
     *
     * @param warpName the name of the warp to rename
     * @param newWarpName the new name of the warp
     * @param requestingPlayer the player requesting this action
     *
     * @return true if renamed, false if not
     *
     * @throws InternalPermissionsException if a method required permissions that the requesting player does not have
     */
    public boolean renameWarp(String warpName, String newWarpName, Player requestingPlayer)
        throws InternalPermissionsException
    {
        boolean retVal = false;

        if(warpName != null && newWarpName != null && requestingPlayer != null)
        {
            // since it exists, let's get it out so we can modify it
            Warp warp = getWarp(warpName, requestingPlayer);

            // if we're changing one we don't own, we only need to change the name to the part after the dot
            if(!warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName()))
                newWarpName = newWarpName.substring(newWarpName.indexOf(".")+1);

            boolean isOwner = warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName());
            boolean hasAdminRename = plugin.hasPermission(requestingPlayer,
                                                          AppStrings.COMMAND_ADMIN_RENAME_PERMISSION,
                                                          AppStrings.COMMAND_RENAME,
                                                          false);

            // either they're renaming their own, or they aren't but they have the admin rename priv
            if( isOwner || hasAdminRename )
            {
                // change the name
                warp.setName(newWarpName);

                // update the database
                plugin.getDatabase().update(warp);

                retVal = true;
            }
            else
                throw new InternalPermissionsException(AppStrings.WARP_CANNOT_RENAME_OTHERS);
        }

        return retVal;
    }

    /**
     * Sets the warp type of an existing named warp
     *
     * @param warpName the name of the warp we're going to modify
     * @param type the type to set that warp to
     * @param requestingPlayer the player requesting this action
     *
     * @return true if type was set, false if not
     *
     * @throws InternalPermissionsException if a method required permissions that the requesting player does not have
     */
    public boolean setWarpType(String warpName, WarpType type, Player requestingPlayer)
        throws InternalPermissionsException
    {
        boolean retVal = false;

        // make sure we have valid params
        if(warpName != null && type != null && requestingPlayer != null)
        {
            // get our warp
            Warp warp = getWarp(warpName, requestingPlayer);

            if (warp != null)
            {
                boolean isOwner = warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName());
                boolean hasAdminSetType = plugin.hasPermission(requestingPlayer,
                                                               AppStrings.COMMAND_ADMIN_SET_PERMISSION,
                                                               AppStrings.COMMAND_SET,
                                                               false);

                // either they're setting type on their own, or they aren't but they have the admin set type priv
                if( isOwner || hasAdminSetType )
                {
                    // change the warpType
                    warp.setWarpType(type);
                    // update the database
                    plugin.getDatabase().update(warp);
                    retVal = true;
                }
                else
                    throw new InternalPermissionsException(AppStrings.WARP_CANNOT_SET_OTHERS);
            }
        }

        return retVal;
    }
}
