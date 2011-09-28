package net.niftymonkey.niftywarp.persistence;

import com.avaje.ebean.EbeanServer;
import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the persistence provider that uses EbeanServer
 * 
 * User: Mark
 * Date: 6/22/11
 * Time: 11:08 PM
 */
public class EbeanServerPersistenceProvider implements IPersistenceProvider
{
    private static Logger log = Logger.getLogger("Minecraft");

    private JavaPlugin plugin;
    private EbeanServer database;

    public EbeanServerPersistenceProvider(JavaPlugin plugin, EbeanServer database)
    {
        this.plugin = plugin;
        this.database = database;
    }

    /**
     * Gets the number of warps that are owned by the player specified
     *
     * @param player the player we're getting a warp count for
     * @return the number of warps owned by this player
     */
    @Override
    public int getWarpsForPlayerCount(Player player)
    {
        return database.find(Warp.class).where().ieq("owner", player.getDisplayName()).findRowCount();
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
        try
        {
            database.update(warp);
        }
        catch (OptimisticLockException e)
        {
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);
            log.warning(addonMsgPrefix + "Error attempting to modify warp database.  Retrying.");

            // if this happens, let's requery, reapply changes, and try again
            database.beginTransaction();

            Warp tmp = database.find(Warp.class, warp.getId());
            tmp = Warp.copy(warp);

            database.update(tmp);

            database.endTransaction();
        }
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
