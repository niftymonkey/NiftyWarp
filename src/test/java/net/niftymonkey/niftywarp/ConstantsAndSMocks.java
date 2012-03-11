package net.niftymonkey.niftywarp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.configuration.Configuration;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Helper class that is used for constants and for retrieving stubbed mock objects
 *
 * User: Mark
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
    public static final int CONFIG_DEFAULT_MAXWARPS  = 5;

    public static NiftyWarp getStubbedNWPluginMock(FileConfiguration configuration)
    {
        NiftyWarp retVal = Mockito.mock(NiftyWarp.class);
        WarpManager warpManager = new WarpManager(retVal);

        // set the plugin to give our mock config and an instance of warp manager
        when(retVal.getConfig()).thenReturn(configuration);
        when(retVal.getWarpManager()).thenReturn(warpManager);

        // also stub out permission checks to return true
        when(retVal.hasPermission(Matchers.<Player>any(), anyString(), anyBoolean(), anyBoolean())).thenReturn(true);

        // get localization values from config and initialize the locale/bundle
        String language = "en";
        String country = "US";
        Locale currentLocale = new Locale(language, country);
        ResourceBundle messageBundle = ResourceBundle.getBundle("MessageBundle", currentLocale);
        when(retVal.getMessageBundle()).thenReturn(messageBundle);

        return retVal;
    }

    public static FileConfiguration getStubbedConfigurationMock()
    {
        FileConfiguration retVal = Mockito.mock(FileConfiguration.class);

        // stub a default warp type config option
        when(retVal.getString(AppStrings.PROPERTY_WARP_DEFAULT_WARPTYPE, AppStrings.WARP_TYPE_UNLISTED))
             .thenReturn(CONFIG_DEFAULT_WARP_TYPE);

        // stub a default max-warps config option value
        when(retVal.getInt(AppStrings.PROPERTY_WARP_MAXWARPS, 20))
             .thenReturn(CONFIG_DEFAULT_MAXWARPS);

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
