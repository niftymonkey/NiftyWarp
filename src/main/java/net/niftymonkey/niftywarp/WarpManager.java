package net.niftymonkey.niftywarp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class used to handle the loading, saving, and retrieval of warps
 *
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 1:18 AM
 */
public class WarpManager
{
    // warp id to warp object map
    // using a map instead of a list for named lookup efficiency
    private Map<String, Warp> warpMap;

    public WarpManager()
    {
        reloadWarps();
    }

    /**
     * Loads the warp map from serialization (file, database, etc.)
     */
    public void reloadWarps()
    {
        warpMap = new HashMap<String, Warp>();
        // TODO:  Implement me!
    }

    /**
     * Serializes the warp map (to file, database, etc.)
     */
    public void serializeWarps()
    {
        // TODO:  Implement me!
    }

    /**
     * Gets the list of warps for the user
     *
     * @param playerName the name of the player whose warps should be listed
     *
     * @return a list of warps that the player in the playerName parameter can use
     */
    public List<Warp> getWarpsForUser(String playerName)
    {
        List<Warp> retVal = new ArrayList<Warp>();

        Set<String> warpIds = warpMap.keySet();
        for (String warpId : warpIds)
        {
            Warp warp = warpMap.get(warpId);
            // if this is a public listed warp, it goes in everyone's list
            if(warp.getType() == Warp.Type.PUBLIC_LISTED)
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
     * Gets a named warp object from the list
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
            String warpId;
            // if the warp name contains a dot, then this is an id, so we can use it as-is
            if(warpName.contains("."))
            {
                warpId = warpName;
            }
            else // if it doesn't have a dot, the user didn't specify an owner, so default to self and build the id
                warpId = Warp.buildId(requestingPlayer.getDisplayName(), warpName);

            retVal = warpMap.get(warpId);

            // we need to null this back out if this player doesn't have access to this warp
            if(!retVal.getOwner().equalsIgnoreCase(requestingPlayer.getDisplayName()) &&
               retVal.getType() == Warp.Type.PRIVATE)
            {
                retVal = null;
            }
        }

        return retVal;
    }

    /**
     * Adds a warp to the list using the supplied parameters
     *
     * @param warpName the name of the warp
     * @param owner the player creating this warp
     * @param type the type of warp this is
     * @param location the {@link org.bukkit.Location} object that represents this warp
     *
     * @return the Warp that was created
     */
    public Warp addWarp(String warpName, String owner, Warp.Type type, Location location)
    {
        Warp retVal = new Warp();
        retVal.setName(warpName);
        retVal.setOwner(owner);
        retVal.setType(type);
        retVal.setLocation(location);

        // add it to our list
        addWarp(retVal);

        return retVal;
    }

    /**
     * Overloaded method for use in adding warps that already have been created by some other means
     *
     * @param warp the warp object
     */
    private void addWarp(Warp warp)
    {
        if (warp != null)
            warpMap.put(warp.getId(), warp);
    }

    /**
     * Deletes a warp from the list
     *
     * @param warpName the name of the warp
     * @param requestingPlayer the player requesting this action
     *
     * @return true if deleted, false if not
     */
    public boolean deleteWarp(String warpName, Player requestingPlayer)
    {
        boolean retVal = false;

        if(warpName != null && requestingPlayer != null)
        {
            String warpId;
            // if the warp name contains a dot, then this is an id, so we can use it as-is
            if(warpName.contains("."))
                warpId = warpName;
            else // if it doesn't have a dot, the user didn't specify warp owner, so default to self and build the id
                warpId = Warp.buildId(requestingPlayer.getDisplayName(), warpName);

            if(warpMap.containsKey(warpId))
            {
                warpMap.remove(warpId);
                retVal = true;
            }
        }

        return retVal;
    }

    /**
     * Renames a warp in the system.  Currently this is done with an add and a remove.
     *
     * @param warpName the name of the warp to rename
     * @param newWarpName the new name of the warp
     * @param requestingPlayer the player requesting this action
     *
     * @return true if renamed, false if not
     */
    public boolean renameWarp(String warpName, String newWarpName, Player requestingPlayer)
    {
        boolean retVal = false;

        if(warpName != null && newWarpName != null && requestingPlayer != null)
        {
            String warpId;
            boolean renamingNonOwned = false;
            // if the warp name contains a dot, then this is an id, so we can use it as-is
            if(warpName.contains("."))
            {
                warpId = warpName;
                renamingNonOwned = true;
            }
            else // if it doesn't have a dot, the user didn't specify warp owner, so default to self and build the id
                warpId = Warp.buildId(requestingPlayer.getDisplayName(), warpName);

            // let's see if that warp exists
            if(warpMap.containsKey(warpId))
            {
                // since it exists, let's get it out so we can modify it
                Warp warp = warpMap.get(warpId);

                // if we're changing one we don't own, we only need to change the name to the part after the dot
                if(renamingNonOwned)
                    newWarpName = newWarpName.substring(newWarpName.indexOf(".")+1);

                // change the name
                warp.setName(newWarpName);

                // add it to the map
                addWarp(warp);

                // remove the old entry
                warpMap.remove(warpId);

                retVal = true;
            }
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
     */
    public boolean setWarpType(String warpName, Warp.Type type, Player requestingPlayer)
    {
        boolean retVal = false;

        // make sure we have valid params
        if(warpName != null && type != null && requestingPlayer != null)
        {
            // get our warp
            Warp warp = getWarp(warpName, requestingPlayer);

            if (warp != null)
            {
                // change the type
                warp.setType(type);

                retVal = true;
            }
        }

        return retVal;
    }
}
