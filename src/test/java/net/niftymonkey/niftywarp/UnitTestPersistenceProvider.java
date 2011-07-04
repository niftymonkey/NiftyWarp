package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.persistence.IPersistenceProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence implementation that provides a mechanism for creating/using specific data to be used in unit testing.
 *
 * User: Mark Lozano
 * Date: 6/22/11
 * Time: 11:12 PM
 */
class UnitTestPersistenceProvider implements IPersistenceProvider
{
    private List<Warp> warpList = new ArrayList<Warp>();

    /**
     * Used to setup initial test warp data.  The list passed in should represent the entire list of warps "in the database"
     *
     * @param warpList the new list of warps
     */
    protected void setWarpList(List<Warp> warpList)
    {
        this.warpList = warpList;
    }
    /**
     * Gets all warps out of persistence
     *
     * @return a list of all the warps we have persisted
     */
    @Override
    public List<Warp> getAllWarps()
    {
        return warpList;
    }

    /**
     * Gets the warps that name specified
     *
     * @param name the name of the warp
     * @return a list of warps that have that name
     */
    @Override
    public List<Warp> getWarpsByName(String name)
    {
        List<Warp> retVal = new ArrayList<Warp>();

        List<Warp> allWarps = getAllWarps();
        for(Warp warp : allWarps)
        {
            if(warp.getName().equalsIgnoreCase(name))
                retVal.add(warp);
        }

        return retVal;
    }

    /**
     * Stores a warp into persistence
     *
     * @param warp the warp object to persist
     */
    @Override
    public void save(Warp warp)
    {
        // if this one is already in the list, remove it so we can replace it with the new version
        delete(warp);

        // "store" it
        warpList.add(warp);
    }

    /**
     * Updates an existing warp object
     *
     * @param warp the warp object to update
     */
    @Override
    public void update(Warp warp)
    {
        save(warp);
    }

    /**
     * Deletes a warp from persistence
     *
     * @param warp the warp object to remove
     */
    @Override
    public void delete(Warp warp)
    {
        Warp warpToDelete = null;
        for(Warp tmpWarp : warpList)
        {
            if(tmpWarp.getFullyQualifiedName().equalsIgnoreCase(warp.getFullyQualifiedName()))
                warpToDelete = tmpWarp;
        }

        if(warpToDelete != null)
            warpList.remove(warpToDelete);
    }
}
