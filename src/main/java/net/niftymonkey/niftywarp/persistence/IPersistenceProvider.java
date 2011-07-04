package net.niftymonkey.niftywarp.persistence;

import net.niftymonkey.niftywarp.Warp;

import java.util.List;

/**
 * Interface used to abstract away the specific database calls (queries) to get warps out of the persistence mechanism.  
 * 
 * This may prove useful for providing an alternative persistence model (like direct JPA, or the Persistence plugin).  
 * In addition, this provides a simple programmatic way to setup test data for unit testing.
 * 
 * User: Mark Lozano
 * Date: 6/22/11
 * Time: 11:07 PM
 */
public interface IPersistenceProvider
{
    /**
     * Gets all warps out of persistence
     * 
     * @return a list of all the warps we have persisted
     */
    List<Warp> getAllWarps();

    /**
     * Gets the warps that name specified
     *
     * @param name the name of the warp
     * @return a list of warps that have that name
     */
    List<Warp> getWarpsByName(String name);

    /**
     * Stores a warp into persistence
     *
     * @param warp the warp object to persist
     */
    void save(Warp warp);

    /**
     * Updates an existing warp object
     *
     * @param warp the warp object to update
     */
    void update(Warp warp);

    /**
     * Deletes a warp from persistence
     *
     * @param warp the warp object to remove
     */
    void delete(Warp warp);

}