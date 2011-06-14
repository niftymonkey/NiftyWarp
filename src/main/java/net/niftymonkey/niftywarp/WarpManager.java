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
     * Adds a warp to the list using the supplied parameters
     *
     * @param warpName the name of the warp
     * @param owner the player creating this warp
     * @param type the type of warp this is
     * @param location the {@link org.bukkit.Location} object that represents this warp
     */
    public void addWarp(String warpName, String owner, Warp.Type type, Location location)
    {
        // create a new warp
        Warp newWarp = new Warp();
        newWarp.setName(warpName);
        newWarp.setOwner(owner);
        newWarp.setType(type);
        newWarp.setLocation(location);

        // add it to our list
        warpMap.put(Warp.buildId(owner, warpName), newWarp);
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
     * @param player the player who is requesting to warp to this warp
     *
     * @return a warp object
     */
    public Warp getWarp(String warpName, Player player)
    {
        Warp retVal = null;

        if(warpName != null && player != null)
        {
            String warpId = Warp.buildId(player.getDisplayName(), warpName);
            retVal = warpMap.get(warpId);
        }

        return retVal;
    }
}
