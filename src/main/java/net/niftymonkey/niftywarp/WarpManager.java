package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.exceptions.InternalPermissionsException;
import net.niftymonkey.niftywarp.persistence.EbeanServerPersistenceProvider;
import net.niftymonkey.niftywarp.persistence.IPersistenceProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to handle the loading, saving, and retrieval of warps
 *
 * User: Mark
 * Date: 6/14/11
 * Time: 1:18 AM
 */
public class WarpManager
{
    private final NiftyWarp plugin;

    private IPersistenceProvider persistenceProvider;

    
    public WarpManager(NiftyWarp niftyWarp)
    {
        this.plugin = niftyWarp;

        // create the persistence provider
        setPersistenceProvider(new EbeanServerPersistenceProvider(plugin.getDatabase()));
    }

    /**
     * Gets the persistence provider instance
     * 
     * @return the warp provider implementation we're using
     */
    public IPersistenceProvider getPersistenceProvider()
    {
        return persistenceProvider;
    }

    public void setPersistenceProvider(IPersistenceProvider persistenceProvider)
    {
        this.persistenceProvider = persistenceProvider;
    }

    /**
     * Gets the list of warps that can by used by the player
     *
     * @param playerName the name of the player whose warps we're looking up
     * @param requestingPlayer the name of the player requesting this list
     *
     * @return a list of warps that the player in the playerName parameter can use
     */
    public List<Warp> getAvailableWarpsForUser(String playerName, Player requestingPlayer)
    {
        List<Warp> retVal = persistenceProvider.getAllWarps();

        // only private warps should be unavailable (unusable) for a player
        retVal = filterByType(retVal, WarpType.PRIVATE, playerName);

        return retVal;
    }

    /**
     * Gets the list of warps that the player should be able to see when listing warps
     *
     * @param playerName the name of the player whose warps we're looking up
     * @param requestingPlayer the name of the player requesting this list
     *
     * @return a list of warps that the player in the playerName parameter can see
     */
    public List<Warp> getVisibleWarpsForUser(String playerName, Player requestingPlayer)
    {
        List<Warp> retVal = getAvailableWarpsForUser(playerName, requestingPlayer);

        // since the available warps should already have removed private warps that don't belong to this player,
        // all we have to do is remove unlisted ones
        retVal = filterByType(retVal, WarpType.UNLISTED, playerName);

        return retVal;
    }

    /**
     * Creates a list of warps based on the list passed in, that does not not contain warps of the type passed in
     *
     * @param warpList the list of warps that will be filtered down
     * @param type the warp type to filter out
     * @param ignorePlayer ignores filtering warps where this player is the owner.  If null, method will all of the type specified,
     *                     regardless of owner
     *
     * @return a new list of warps based on the first parameter that has been filtered
     */
    private List<Warp> filterByType(List<Warp> warpList, WarpType type, String ignorePlayer)
    {
        List<Warp> retVal = new ArrayList<Warp>(warpList);

        for (Warp warp : warpList)
        {
            if ((ignorePlayer == null) || !warp.getOwner().equalsIgnoreCase(ignorePlayer))
            {
                if(warp.getWarpType() == type)
                    retVal.remove(warp);
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
            // start with the list of available warps.  This allows the available warps method to weed out anything we shouldn't see
            List<Warp> availableWarpsForUser = getAvailableWarpsForUser(requestingPlayer.getDisplayName(),
                                                                        requestingPlayer);

            // if the warpName contains a dot, let's assume they're trying to use a fully qualified name
            if(warpName.contains(AppStrings.FQL_DELIMITER))
            {
                for(Warp warp : availableWarpsForUser)
                {
                    if(warp.getFullyQualifiedName().equalsIgnoreCase(warpName))
                        retVal = warp;
                }
            }

            //let's get all warps that have that name, then decide which to return
            {
                // build a list of matching warps
                List<Warp> matchingWarps = new ArrayList<Warp>();
                for(Warp warp : availableWarpsForUser)
                {
                    if(warp.getName().equalsIgnoreCase(warpName))
                    {
                        matchingWarps.add(warp);
                    }
                }

                // if we have only one match, let's return it
                if(matchingWarps.size() == 1)
                {
                    retVal = matchingWarps.get(0);
                }
                // if we have more than one, we have a naming collision.  In this case it only make sense to return one by just the name
                // if that warp belongs to the requesting player.  Otherwise they need to request it with the fully qualified name
                else if(matchingWarps.size() > 1)
                {
                    for(Warp warp : matchingWarps)
                    {
                        if(warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName()))
                            retVal = warp;
                    }
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
        // deleting then saving for now, since update seems to be failing me on location changes
        Warp retVal = getWarp(warpName, owner);
        if( (retVal != null) && (retVal.getOwner().equalsIgnoreCase(owner.getDisplayName())) )
            persistenceProvider.delete(retVal);

        retVal = new Warp();
        retVal.setName(warpName);
        retVal.setOwner(owner.getDisplayName());
        retVal.setWarpType(warpType);
        retVal.setLocation(location);

        persistenceProvider.save(retVal);

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
                                                              AppStrings.COMMAND_DELETE,
                                                              true,
                                                              false);

                // either they're deleting their own, or they aren't but they have the admin delete priv
                if( isOwner || hasAdminDelete )
                {
                    persistenceProvider.delete(warp);
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
                newWarpName = newWarpName.substring(newWarpName.indexOf(AppStrings.FQL_DELIMITER)+1);

            boolean isOwner = warp.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName());
            boolean hasAdminRename = plugin.hasPermission(requestingPlayer,
                                                          AppStrings.COMMAND_RENAME,
                                                          true,
                                                          false);

            // either they're renaming their own, or they aren't but they have the admin rename priv
            if( isOwner || hasAdminRename )
            {
                // change the name
                warp.setName(newWarpName);

                // update the database
                persistenceProvider.update(warp);

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
                                                               AppStrings.COMMAND_SET,
                                                               true,
                                                               false);

                // either they're setting type on their own, or they aren't but they have the admin set type priv
                if( isOwner || hasAdminSetType )
                {
                    // change the warpType
                    warp.setWarpType(type);
                    // update the database
                    persistenceProvider.update(warp);
                    retVal = true;
                }
                else
                    throw new InternalPermissionsException(AppStrings.WARP_CANNOT_SET_OTHERS);
            }
        }

        return retVal;
    }
}
