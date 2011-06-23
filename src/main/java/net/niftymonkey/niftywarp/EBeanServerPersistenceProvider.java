package net.niftymonkey.niftywarp;

import com.avaje.ebean.EbeanServer;

import java.util.List;

/**
 * Implementation of the persistence provider that uses EbeanServer
 * 
 * User: Mark Lozano
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
}
