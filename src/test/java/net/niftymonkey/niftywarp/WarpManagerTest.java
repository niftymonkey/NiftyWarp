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

/**
 * Author: Mark Lozano
 */
@SuppressWarnings({"JavaDoc"})
public class WarpManagerTest
{
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

    /**
     * Tests getting all the warps a player can use.
     *
     * @throws Exception
     */
    @Test
    public void getAvailableWarpsForUser() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider 
        List<Warp> warpList = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        warpList.addAll(getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_TWO_NAME));
        warpList.addAll(getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_THREE_NAME));
        
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);
        
        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        List<Warp> warpsForUser = warpManager.getAvailableWarpsForUser(ConstantsAndSMocks.PLAYER_ONE_NAME, mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////
        
        // hidden warps that belong to others should be the only ones not returned
        warpList = filterByType(warpList, WarpType.PRIVATE, ConstantsAndSMocks.PLAYER_ONE_NAME);
        assertEquals(warpList, warpsForUser);
    }

    /**
     * This tests getting all the warps that a player can see (useful in listing warps)
     *
     * @throws Exception
     */
    @Test
    public void getVisibleWarpsForUser() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        warpList.addAll(getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_TWO_NAME));
        warpList.addAll(getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_THREE_NAME));

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        List<Warp> warpsForUser = warpManager.getVisibleWarpsForUser(ConstantsAndSMocks.PLAYER_ONE_NAME, mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // hidden and unlisted warps that belong to others should be the only ones not returned
        warpList = filterByType(warpList, WarpType.PRIVATE, ConstantsAndSMocks.PLAYER_ONE_NAME);
        warpList = filterByType(warpList, WarpType.UNLISTED, ConstantsAndSMocks.PLAYER_ONE_NAME);
        assertEquals(warpList, warpsForUser);
    }

    /**
     * Tests getting a warp by name, where the warp is owned by the player requesting the warp
     *
     * @throws Exception
     */
    @Test
    public void getWarp_byName_playerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
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

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp privateWarp = warpManager.getWarp(expectedPrivateWarp.getName(), mockPlayerOne);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getName(), mockPlayerOne);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertEquals(expectedPrivateWarp, privateWarp);
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    /**
     * Tests getting a warp by fully qualified name, where the warp is owned by the player requesting the warp
     *
     * FQL = [owner].[warpName]
     *
     * @throws Exception
     */
    @Test
    public void getWarp_byFQName_playerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
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

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp privateWarp = warpManager.getWarp(expectedPrivateWarp.getFullyQualifiedName(), mockPlayerOne);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getFullyQualifiedName(), mockPlayerOne);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getFullyQualifiedName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertEquals(expectedPrivateWarp, privateWarp);
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    /**
     * Tests getting a warp by name, where the warp is NOT owned by the player requesting the warp
     *
     * @throws Exception
     */
    @Test
    public void getWarp_byName_notPlayerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        List<Warp> playerTwoWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_TWO_NAME);
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

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp privateWarp = warpManager.getWarp(unexpectedPrivateWarp.getName(), mockPlayerOne);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getName(), mockPlayerOne);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // should get null for a private warp owned by someone else
        assertEquals(null, privateWarp);
        // should get valid warps for unlisted and listed
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    /**
     * Tests getting a warp by fully qualified name, where the warp is NOT owned by the player requesting the warp
     *
     * FQL = [owner].[warpName]
     *
     * @throws Exception
     */
    @Test
    public void getWarp_byFQName_notPlayerOwned() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> playerOneWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        List<Warp> playerTwoWarps = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_TWO_NAME);
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

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp privateWarp = warpManager.getWarp(unexpectedPrivateWarp.getFullyQualifiedName(), mockPlayerOne);
        Warp unlistedWarp = warpManager.getWarp(expectedUnlistedWarp.getFullyQualifiedName(), mockPlayerOne);
        Warp listedWarp = warpManager.getWarp(expectedListedWarp.getFullyQualifiedName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // should get null for a private warp owned by someone else
        assertEquals(null, privateWarp);
        // should get valid warps for unlisted and listed
        assertEquals(expectedUnlistedWarp, unlistedWarp);
        assertEquals(expectedListedWarp, listedWarp);
    }

    /**
     * Tests getting a warp whose name contains the delimiter usually reserved for fully qualified names.  This ensures the ability to
     * be able to get the warp even when it contains the fql delimiter.  This test also verifies ability to look up by the name and the
     * fully qualified name.  Lastly this test also verifies ability to lookup when there are naming collisions between an owned and a
     * non-owned warp
     *
     * @throws Exception
     */
    @Test
    public void getWarp_containsDelimiter() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = new ArrayList<Warp>();

        String warpName = "foo" + AppStrings.FQL_DELIMITER + "bar";
        Warp playerOwned = new Warp(warpName,
                                    ConstantsAndSMocks.PLAYER_ONE_NAME,
                                    WarpType.PRIVATE,
                                    ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 1f, 1f);
        Warp nonPlayerOwned = new Warp(warpName,
                                       ConstantsAndSMocks.PLAYER_TWO_NAME,
                                       WarpType.UNLISTED,
                                       ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 1f, 1f);
        warpList.add(playerOwned);
        warpList.add(nonPlayerOwned);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp poNameResult = warpManager.getWarp(playerOwned.getName(), mockPlayerOne);
        Warp poFQLResult = warpManager.getWarp(playerOwned.getFullyQualifiedName(), mockPlayerOne);
        Warp npoNameResult = warpManager.getWarp(nonPlayerOwned.getName(), mockPlayerOne);
        Warp npoFQLResult = warpManager.getWarp(nonPlayerOwned.getFullyQualifiedName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertEquals(playerOwned, poNameResult);
        assertEquals(playerOwned, poFQLResult);
        // this actually expects the player owned warp since the name passed in is not "fully qualified"
        assertEquals(playerOwned, npoNameResult);
        assertEquals(nonPlayerOwned, npoFQLResult);
    }

    /**
     * Tests adding a warp
     *
     * @throws Exception
     */
    @Test
    public void addWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);
        // mock world
        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        // mock locations
        Location mockLocationPrivate = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 10.0, 20.0, 30.0, 1.5f, 1.75f);
        Location mockLocationUnlisted = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 20.0, 30.0, 40.0, 1.5f, 1.75f);
        Location mockLocationListed = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 30.0, 40.0, 50.0, 1.5f, 1.75f);

        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        Warp privateWarp = warpManager.addWarp("testWarp_P", mockPlayerOne, WarpType.PRIVATE, mockLocationPrivate);
        Warp unlistedWarp = warpManager.addWarp("testWarp_U", mockPlayerOne, WarpType.UNLISTED, mockLocationUnlisted);
        Warp listedWarp = warpManager.addWarp("testWarp_L", mockPlayerOne, WarpType.LISTED, mockLocationListed);

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

    /**
     * Tests deleting a warp
     *
     * @throws Exception
     */
    @Test
    public void deleteWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        Warp warpToDelete = warpList.get(0);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        warpManager.deleteWarp(warpToDelete.getName(), mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        assertFalse(testPersistenceProvider.getAllWarps().contains(warpToDelete));
    }

    /**
     * Tests renaming a warp
     *
     * @throws Exception
     */
    @Test
    public void renameWarp() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and set a list of warps that will be returned from the persistence provider
        List<Warp> warpList = getDefaultPlayerWarps(ConstantsAndSMocks.PLAYER_ONE_NAME);
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        Warp warpToRename = warpList.get(0);
        String warpToRenameName = warpToRename.getName();
        String warpNewName = "renamedWarp";

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        warpManager.renameWarp(warpToRenameName, warpNewName, mockPlayerOne);

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        List<Warp> beforeRename = testPersistenceProvider.getWarpsByName(warpToRenameName);
        List<Warp> afterRename = testPersistenceProvider.getWarpsByName(warpNewName);
        assertEquals(0, beforeRename.size());
        assertEquals(1, afterRename.size());
    }

    /**
     * Tests changing the warp type on an existing warp
     *
     * @throws Exception
     */
    @Test
    public void setWarpType() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        // mock player
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, null);

        // create and warp for the test persistence provider that will me modified
        List<Warp> warpList = new ArrayList<Warp>();
        warpList.add(new Warp("foo", ConstantsAndSMocks.PLAYER_ONE_NAME, WarpType.LISTED, ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 1f, 2f));
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        warpManager.setPersistenceProvider(testPersistenceProvider);

        Warp warpToModify = warpList.get(0);
        String warpToModifyName = warpToModify.getName();
        WarpType newType = WarpType.PRIVATE;

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        warpManager.setWarpType(warpToModifyName, newType, mockPlayerOne);

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
        retVal.add(new Warp("pW_"+reversedName, playerName, WarpType.PRIVATE, ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 0, 0));
        retVal.add(new Warp("uW_"+reversedName, playerName, WarpType.UNLISTED, ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 0, 0));
        retVal.add(new Warp("lW_"+reversedName, playerName, WarpType.LISTED, ConstantsAndSMocks.WORLD_NAME, 0, 0, 0, 0, 0));

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