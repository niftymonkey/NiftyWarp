package net.niftymonkey.niftywarp.persistence;

import com.avaje.ebean.EbeanServer;
import net.niftymonkey.niftywarp.Warp;

import java.util.List;

/**
 * Implementation of the persistence provider that uses EbeanServer
 * 
 * User: Mark
 * Date: 6/22/11
 * Time: 11:08 PM
 */
public class EbeanServerPersistenceProvider implements IPersistenceProvider
{
    private EbeanServer database;

    public EbeanServerPersistenceProvider(EbeanServer database)
    {
        this.database = database;
    }

    /**
     * Gets all warps out of persistence
     *
     * @return a list of all the warps we have persisted
     */
    @Override
    public List<Warp> getAllWarps()
    {
        return database.find(Warp.class).findList();
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
        return database.find(Warp.class).where().ieq("name", name).findList();
    }

    /**
     * Stores a warp into persistence
     *
     * @param warp the warp object to persist
     */
    @Override
    public void save(Warp warp)
    {
        // store this warp
        database.save(warp);
    }

    /**
     * Updates an existing warp object
     *
     * @param warp the warp object to update
     */
    @Override
    public void update(Warp warp)
    {
        database.update(warp);
    }

    /**
     * Deletes a warp from persistence
     *
     * @param warp the warp object to remove
     */
    @Override
    public void delete(Warp warp)
    {
        database.delete(warp);
    }
}
