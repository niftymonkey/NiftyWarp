package net.niftymonkey.niftywarp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.mockito.Matchers;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Helper class that is used for constants and for retrieving stubbed mock objects
 *
 * User: Mark Lozano
 * Date: 6/30/11
 * Time: 6:42 PM
 */
public class ConstantsAndSMocks
{
    public static final String PLAYER_ONE_NAME   = "playerOne";
    public static final String PLAYER_TWO_NAME   = "playerTwo";
    public static final String PLAYER_THREE_NAME = "playerThree";
    public static final String WORLD_NAME        = "testWorld";

    public static final String CONFIG_DEFAULT_WARP_TYPE = AppStrings.WARP_TYPE_UNLISTED;

    public static NiftyWarp getStubbedNWPluginMock(Configuration configuration)
    {
        NiftyWarp retVal = Mockito.mock(NiftyWarp.class);
        // create our test persistence provider and set that as the persistence provider for
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        // set the plugin to give our mock config and an instance of warp manager
        when(retVal.getConfiguration()).thenReturn(configuration);
        WarpManager warpManager = new WarpManager(retVal);
        warpManager.setPersistenceProvider(testPersistenceProvider);
        when(retVal.getWarpManager()).thenReturn(warpManager);
        // also stub out permission checks to return true
        when(retVal.hasPermission(Matchers.<Player>any(), anyString(), anyString())).thenReturn(true);

        return retVal;
    }

    public static Configuration getStubbedConfigurationMock()
    {
        Configuration retVal = Mockito.mock(Configuration.class);

        // stub a default warp type config option
        when(retVal.getString(AppStrings.PROPERTY_WARP_DEFAULT_WARPTYPE, AppStrings.WARP_TYPE_UNLISTED))
             .thenReturn(CONFIG_DEFAULT_WARP_TYPE);

        return retVal;
    }

    public static World getStubbedWorldMock(String worldName)
    {
        World retVal = Mockito.mock(World.class);
        when(retVal.getName()).thenReturn(worldName);

        return retVal;
    }

    public static Location getStubbedLocationMock(World world, double x, double y, double z, float pitch, float yaw)
    {
        Location retVal = Mockito.mock(Location.class);
        when(retVal.getWorld()).thenReturn(world);
        when(retVal.getX()).thenReturn(x);
        when(retVal.getY()).thenReturn(y);
        when(retVal.getZ()).thenReturn(z);
        when(retVal.getPitch()).thenReturn(pitch);
        when(retVal.getYaw()).thenReturn(yaw);

        return retVal;
    }

    public static Player getStubbedPlayerMock(String playerName, Location location)
    {
        Player retVal = Mockito.mock(Player.class);
        when(retVal.getDisplayName()).thenReturn(playerName);
        when(retVal.getLocation()).thenReturn(location);

        return retVal;
    }


}
