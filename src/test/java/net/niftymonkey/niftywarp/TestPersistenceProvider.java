package net.niftymonkey.niftywarp;

import java.util.ArrayList;
import java.util.List;

/**
 * Persistence implementation that provides a set of known test data to be used in unit testing.
 * 
 * User: Mark Lozano
 * Date: 6/22/11
 * Time: 11:12 PM
 */
public class TestPersistenceProvider implements IPersistenceProvider
{
    // some reused constants
    public static final String PLAYER_ONE_NAME   = "playerOne";
    public static final String PLAYER_TWO_NAME   = "playerTwo";
    public static final String PLAYER_THREE_NAME = "playerThree";
    public static final String WORLD_NAME        = "world";

    /**
     * Gets all warps out of persistence
     *
     * @return a list of all the warps we have persisted
     */
    @Override
    public List<Warp> getAllWarps()
    {
        List<Warp> retVal = new ArrayList<Warp>();

        retVal.addAll(getPlayerOneWarps());
        retVal.addAll(getPlayerTwoWarps());
        retVal.addAll(getPlayerThreeWarps());

        return retVal;
    }

    public List<Warp> getPlayerOneWarps()
    {
        List<Warp> retVal = new ArrayList<Warp>();

        Warp warp = new Warp();
        warp.setName("one1");
        warp.setOwner(PLAYER_ONE_NAME);
        warp.setWarpType(WarpType.PRIVATE);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("two1");
        warp.setOwner(PLAYER_ONE_NAME);
        warp.setWarpType(WarpType.PUBLIC_UNLISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("three1");
        warp.setOwner(PLAYER_ONE_NAME);
        warp.setWarpType(WarpType.PUBLIC_LISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        return retVal;
    }
    
    public List<Warp> getPlayerTwoWarps()
    {
        List<Warp> retVal = new ArrayList<Warp>();

        Warp warp = new Warp();
        warp.setName("one2");
        warp.setOwner(PLAYER_TWO_NAME);
        warp.setWarpType(WarpType.PRIVATE);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("two2");
        warp.setOwner(PLAYER_TWO_NAME);
        warp.setWarpType(WarpType.PUBLIC_UNLISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("three2");
        warp.setOwner(PLAYER_TWO_NAME);
        warp.setWarpType(WarpType.PUBLIC_LISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        return retVal;
    }
    
    public List<Warp> getPlayerThreeWarps()
    {
        List<Warp> retVal = new ArrayList<Warp>();

        Warp warp = new Warp();
        warp.setName("one3");
        warp.setOwner(PLAYER_THREE_NAME);
        warp.setWarpType(WarpType.PRIVATE);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("two3");
        warp.setOwner(PLAYER_THREE_NAME);
        warp.setWarpType(WarpType.PUBLIC_UNLISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        warp = new Warp();
        warp.setName("three3");
        warp.setOwner(PLAYER_THREE_NAME);
        warp.setWarpType(WarpType.PUBLIC_LISTED);
        warp.setWorldName(WORLD_NAME);
        warp.setX(10.0);
        warp.setY(20.0);
        warp.setZ(30);
        warp.setYaw(1.5f);
        warp.setPitch(1.5f);
        retVal.add(warp);

        return retVal;
    }
}
