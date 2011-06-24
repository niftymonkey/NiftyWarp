package net.niftymonkey.niftywarp;

import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Author: Mark Lozano
 */
public class WarpManagerTest
{
    // some testing constants
    public static final String PLAYER_ONE_NAME   = "playerOne";
    public static final String PLAYER_TWO_NAME   = "playerTwo";
    public static final String PLAYER_THREE_NAME = "playerThree";
    public static final String WORLD_NAME        = "world";

    private NiftyWarp mockNiftyWarpPlugin = null;

    @Before
    public void setUp() throws Exception
    {
        mockNiftyWarpPlugin = Mockito.mock(NiftyWarp.class);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testGetWarpsForUser() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider 
        List<Warp> warpList = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        warpList.addAll(getDefaultPlayerWarps(PLAYER_TWO_NAME));
        warpList.addAll(getDefaultPlayerWarps(PLAYER_THREE_NAME));
        
        TestPersistenceProvider testPersistenceProvider = new TestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);
        
        ////////////////////////////////////////////////////////
        // Run Test
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        List<Warp> warpsForUser = warpManager.getWarpsForUser(PLAYER_ONE_NAME, mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////
        
        // hidden warps that belong to others should be the only ones not returned
        warpList = filterByType(warpList, WarpType.PRIVATE, PLAYER_ONE_NAME);
        assertEquals(warpList, warpsForUser);
    }

    
    ////////////////////////////////////////////////////////
    // Helper Methods
    ////////////////////////////////////////////////////////


    /**
     * Gets a default set of test warps.  This should contain at least 1 warp of each type.
     * 
     * @param playerName the player name
     * 
     * @return a list of warps for this user
     */
    private List<Warp> getDefaultPlayerWarps(String playerName)
    {
        List<Warp> retVal = new ArrayList<Warp>();

        String reversedName = new StringBuffer(playerName).reverse().toString();
        retVal.add(new Warp("pW_"+reversedName, playerName, WarpType.PRIVATE, WORLD_NAME, 0, 0, 0, 0, 0));
        retVal.add(new Warp("uW_"+reversedName, playerName, WarpType.UNLISTED, WORLD_NAME, 0, 0, 0, 0, 0));
        retVal.add(new Warp("lW_"+reversedName, playerName, WarpType.LISTED, WORLD_NAME, 0, 0, 0, 0, 0));

        return retVal;
    }

    /**
     * Creates a list of warps based on the list passed in, that does not not contain warps of the type passed in
     * 
     * @param warpList the list of warps that will be filtered down
     * @param type the type to filter out
     * @param ignorePlayer ignores filtering on warps where this player is the owner
     * 
     * @return a new list of warps based on the first parameter that has been filtered
     */
    private List<Warp> filterByType(List<Warp> warpList, WarpType type, String ignorePlayer)
    {
        List<Warp> retVal = new ArrayList<Warp>(warpList);
        
        for (Warp warp : warpList)
        {
            if (!warp.getOwner().equalsIgnoreCase(ignorePlayer))
            {
                if(warp.getWarpType() == type)
                    retVal.remove(warp);
            }
        }
        
        return retVal;
    }
}

/**
 * Persistence implementation that provides a mechanism for creating/using specific data to be used in unit testing.
 * 
 * User: Mark Lozano
 * Date: 6/22/11
 * Time: 11:12 PM
 */
class TestPersistenceProvider implements IPersistenceProvider
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

}
