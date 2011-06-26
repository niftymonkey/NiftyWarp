package net.niftymonkey.niftywarp;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
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
    public void getAvailableWarpsForUser() throws Exception
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
        
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);
        
        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        List<Warp> warpsForUser = warpManager.getAvailableWarpsForUser(PLAYER_ONE_NAME, mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////
        
        // hidden warps that belong to others should be the only ones not returned
        warpList = filterByType(warpList, WarpType.PRIVATE, PLAYER_ONE_NAME);
        assertEquals(warpList, warpsForUser);
    }

    @Test
    public void getVisibleWarpsForUser() throws Exception
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

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        List<Warp> warpsForUser = warpManager.getVisibleWarpsForUser(PLAYER_ONE_NAME, mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // hidden and unlisted warps that belong to others should be the only ones not returned

        warpList = filterByType(warpList, WarpType.PRIVATE, PLAYER_ONE_NAME);
        warpList = filterByType(warpList, WarpType.UNLISTED, PLAYER_ONE_NAME);
        assertEquals(warpList, warpsForUser);
    }

    @Test
    public void getWarp_byName_playerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.addAll(playerOneWarps);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // setup some expected results
        Warp expectedPrivateWarp = null;
        Warp expectedUnlistedWarp = null;
        Warp expectedListedWarp = null;

        for(Warp tmpWarp : warpList)
        {
            if(tmpWarp.getWarpType().equals(WarpType.PRIVATE))
                expectedPrivateWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.UNLISTED))
                expectedUnlistedWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.LISTED))
                expectedListedWarp = tmpWarp;
        }

        assert expectedPrivateWarp != null;
        assert expectedUnlistedWarp != null;
        assert expectedListedWarp != null;

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        Warp privateWarp = warpManager.getWarp(expectedPrivateWarp.getName(), mockPlayerOne, testPersistenceProvider);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getName(), mockPlayerOne, testPersistenceProvider);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getName(), mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertEquals(expectedPrivateWarp, privateWarp);
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    @Test
    public void getWarp_byFQName_playerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.addAll(playerOneWarps);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // setup some expected results
        Warp expectedPrivateWarp = null;
        Warp expectedUnlistedWarp = null;
        Warp expectedListedWarp = null;

        for(Warp tmpWarp : warpList)
        {
            if(tmpWarp.getWarpType().equals(WarpType.PRIVATE))
                expectedPrivateWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.UNLISTED))
                expectedUnlistedWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.LISTED))
                expectedListedWarp = tmpWarp;
        }

        assert expectedPrivateWarp != null;
        assert expectedUnlistedWarp != null;
        assert expectedListedWarp != null;

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        Warp privateWarp = warpManager.getWarp(expectedPrivateWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertEquals(expectedPrivateWarp, privateWarp);
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    @Test
    public void getWarp_byName_notPlayerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        List<Warp> playerTwoWarps = getDefaultPlayerWarps(PLAYER_TWO_NAME);
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.addAll(playerOneWarps);
        warpList.addAll(playerTwoWarps);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // setup some expected results
        Warp unexpectedPrivateWarp = null;
        Warp expectedUnlistedWarp = null;
        Warp expectedListedWarp = null;

        for(Warp tmpWarp : playerTwoWarps)
        {
            if(tmpWarp.getWarpType().equals(WarpType.PRIVATE))
                unexpectedPrivateWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.UNLISTED))
                expectedUnlistedWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.LISTED))
                expectedListedWarp = tmpWarp;
        }

        assert unexpectedPrivateWarp != null;
        assert expectedUnlistedWarp != null;
        assert expectedListedWarp != null;

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        Warp privateWarp = warpManager.getWarp(unexpectedPrivateWarp.getName(), mockPlayerOne, testPersistenceProvider);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getName(), mockPlayerOne, testPersistenceProvider);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getName(), mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // should get null for a private warp owned by someone else
        assertEquals(null, privateWarp);
        // should get valid warps for unlisted and listed
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    @Test
    public void getWarp_byFQName_notPlayerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        List<Warp> playerTwoWarps = getDefaultPlayerWarps(PLAYER_TWO_NAME);
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.addAll(playerOneWarps);
        warpList.addAll(playerTwoWarps);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // setup some expected results
        Warp unexpectedPrivateWarp = null;
        Warp expectedUnlistedWarp = null;
        Warp expectedListedWarp = null;

        for(Warp tmpWarp : playerTwoWarps)
        {
            if(tmpWarp.getWarpType().equals(WarpType.PRIVATE))
                unexpectedPrivateWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.UNLISTED))
                expectedUnlistedWarp = tmpWarp;
            if(tmpWarp.getWarpType().equals(WarpType.LISTED))
                expectedListedWarp = tmpWarp;
        }

        assert unexpectedPrivateWarp != null;
        assert expectedUnlistedWarp != null;
        assert expectedListedWarp != null;

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        Warp privateWarp = warpManager.getWarp(unexpectedPrivateWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getFullyQualifiedName(), mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // should get null for a private warp owned by someone else
        assertEquals(null, privateWarp);
        // should get valid warps for unlisted and listed
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    @Test
    public void addWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);
        // mock world
        World mockWorld = Mockito.mock(World.class);
        when(mockWorld.getName()).thenReturn("testWorld");
        // mock locations
        Location mockLocationPrivate = Mockito.mock(Location.class);
        when(mockLocationPrivate.getWorld()).thenReturn(mockWorld);
        when(mockLocationPrivate.getX()).thenReturn(10.0);
        when(mockLocationPrivate.getY()).thenReturn(20.0);
        when(mockLocationPrivate.getZ()).thenReturn(30.0);
        when(mockLocationPrivate.getPitch()).thenReturn(1.5f);
        when(mockLocationPrivate.getYaw()).thenReturn(1.75f);
        Location mockLocationUnlisted = Mockito.mock(Location.class);
        when(mockLocationUnlisted.getWorld()).thenReturn(mockWorld);
        when(mockLocationUnlisted.getX()).thenReturn(20.0);
        when(mockLocationUnlisted.getY()).thenReturn(30.0);
        when(mockLocationUnlisted.getZ()).thenReturn(40.0);
        when(mockLocationUnlisted.getPitch()).thenReturn(1.5f);
        when(mockLocationUnlisted.getYaw()).thenReturn(1.75f);
        Location mockLocationListed = Mockito.mock(Location.class);
        when(mockLocationListed.getWorld()).thenReturn(mockWorld);
        when(mockLocationListed.getX()).thenReturn(30.0);
        when(mockLocationListed.getY()).thenReturn(40.0);
        when(mockLocationListed.getZ()).thenReturn(50.0);
        when(mockLocationListed.getPitch()).thenReturn(1.5f);
        when(mockLocationListed.getYaw()).thenReturn(1.75f);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        Warp privateWarp = warpManager.addWarp("testWarp_P", mockPlayerOne, WarpType.PRIVATE, mockLocationPrivate, testPersistenceProvider);
        Warp unlistedWarp = warpManager.addWarp("testWarp_U", mockPlayerOne, WarpType.UNLISTED, mockLocationUnlisted, testPersistenceProvider);
        Warp listedWarp = warpManager.addWarp("testWarp_L", mockPlayerOne, WarpType.LISTED, mockLocationListed, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        List<Warp> testList1 = testPersistenceProvider.getWarpsByName("testWarp_P");
        List<Warp> testList2 = testPersistenceProvider.getWarpsByName("testWarp_U");
        List<Warp> testList3 = testPersistenceProvider.getWarpsByName("testWarp_L");
        assertEquals(testList1.get(0), privateWarp);
        assertEquals(testList2.get(0), unlistedWarp);
        assertEquals(testList3.get(0), listedWarp);
    }

    @Test
    public void deleteWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        Warp warpToDelete = warpList.get(0);
        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.deleteWarp(warpToDelete.getName(), mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertFalse(testPersistenceProvider.getAllWarps().contains(warpToDelete));
    }

    @Test
    public void renameWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = getDefaultPlayerWarps(PLAYER_ONE_NAME);
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        Warp warpToRename = warpList.get(0);
        String warpToRenameName = warpToRename.getName();
        String warpNewName = "renamedWarp";
        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.renameWarp(warpToRenameName, warpNewName, mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        List<Warp> beforeRename = testPersistenceProvider.getWarpsByName(warpToRenameName);
        List<Warp> afterRename = testPersistenceProvider.getWarpsByName(warpNewName);
        assertEquals(0, beforeRename.size());
        assertEquals(1, afterRename.size());
    }

    @Test
    public void setWarpType() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = Mockito.mock(Player.class);
        when(mockPlayerOne.getDisplayName()).thenReturn(PLAYER_ONE_NAME);

        // create and warp for the test persistence provider that will me modified
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.add(new Warp("foo", PLAYER_ONE_NAME, WarpType.LISTED, "testWorld", 0, 0, 0, 1f, 2f));
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        Warp warpToModify = warpList.get(0);
        String warpToModifyName = warpToModify.getName();
        WarpType newType = WarpType.PRIVATE;
        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setWarpType(warpToModifyName, newType, mockPlayerOne, testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        Warp warpAfterMod = testPersistenceProvider.getAllWarps().get(0);
        assertEquals(WarpType.PRIVATE, warpAfterMod.getWarpType());
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
     * @param type the warp type to filter out
     * @param ignorePlayer ignores filtering warps where this player is the owner.  If null, method will all of the type specified,
     *                     regardless of owner
     *
     * @return a new list of warps based on the first parameter that has been filtered
     */
    private List<Warp> filterByType(List<Warp> warpList, WarpType type, String ignorePlayer)
    {
        List<Warp> retVal = new ArrayList<Warp>(warpList);

        for (Warp warp : warpList)
        {
            if ((ignorePlayer == null) || !warp.getOwner().equalsIgnoreCase(ignorePlayer))
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
