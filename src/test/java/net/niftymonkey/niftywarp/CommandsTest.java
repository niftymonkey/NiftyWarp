package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import net.niftymonkey.niftywarp.commands.DeleteWarpCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: Mark
 * Date: 6/30/11
 * Time: 6:41 PM
 */
@SuppressWarnings({"JavaDoc"})
public class CommandsTest
{
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {

    }

    /**
     * Simple success check.
     *
     * @throws Exception
     */
    @Test
    public void addWarp_success() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        FileConfiguration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

        // create a test persistence provider and set that as our warp manager's provider
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        mockNiftyWarpPlugin.getWarpManager().setPersistenceProvider(testPersistenceProvider);

        AddWarpCommand addWarpCommand = new AddWarpCommand(mockNiftyWarpPlugin);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        String testWarpName = "testWarpName";
        String[] args = new String[1];
        args[0] = testWarpName;

        boolean success = addWarpCommand.onCommand(mockPlayerOne,     // player one is the sender
                                                   null,              // currently my command executors don't use the command object
                                                   null,              // currently my command executors don't use the label object
                                                   args);             // command arguments

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // check simple success first
        assertTrue(success);

        // deeper equality check on the warp created
        assertEquals(testPersistenceProvider.getAllWarps().get(0).getName(), testWarpName);
        assertEquals(testPersistenceProvider.getAllWarps().get(0).getOwner(), mockPlayerOne.getDisplayName());
    }

    /**
     * Failure check for the "no warp name provided" scenario
     *
     * @throws Exception
     */
    @Test
    public void addWarp_failure_noWarpName() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        FileConfiguration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

        // create a test persistence provider and set that as our warp manager's provider
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        mockNiftyWarpPlugin.getWarpManager().setPersistenceProvider(testPersistenceProvider);

        AddWarpCommand addWarpCommand = new AddWarpCommand(mockNiftyWarpPlugin);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        // passing in an empty argument list should fail, since no warp name was provided
        String[] args = new String[]{};

        boolean success = addWarpCommand.onCommand(mockPlayerOne,     // player one is the sender
                                                   null,              // currently my command executors don't use the command object
                                                   null,              // currently my command executors don't use the label object
                                                   args);             // command arguments

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // check simple failure first
        assertFalse(success);

        // make sure there's no warp stored
        assertEquals(testPersistenceProvider.getAllWarps().size(), 0);
    }

    /**
     * Simple success check.
     *
     * @throws Exception
     */
    @Test
    public void deleteWarp_success() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        FileConfiguration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

        List<Warp> warpList = new ArrayList<Warp>();
        String testWarpName = "testWarpName";
        // make a warp for deletion
        Warp warpToDelete = new Warp(testWarpName, mockPlayerOne.getDisplayName(), WarpType.PRIVATE, mockWorld.getName(), 0, 0, 0, 0, 0);
        // add it to the list we're pre-populating the persistence provider with
        warpList.add(warpToDelete);

        // "put it in the database"
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // set this provider as the one to use
        mockNiftyWarpPlugin.getWarpManager().setPersistenceProvider(testPersistenceProvider);

        DeleteWarpCommand deleteWarpCommand = new DeleteWarpCommand(mockNiftyWarpPlugin);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        String[] args = new String[1];
        args[0] = testWarpName;

        boolean success = deleteWarpCommand.onCommand(mockPlayerOne,     // player one is the sender
                                                      null,              // currently my command executors don't use the command object
                                                      null,              // currently my command executors don't use the label object
                                                      args);             // command arguments

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // check simple success first
        assertTrue(success);

        // deeper equality check on the warp created
        assertFalse(testPersistenceProvider.getAllWarps().contains(warpToDelete));
    }

    /**
     * Failure check for the "no warp name provided" scenario
     *
     * @throws Exception
     */
    @Test
    public void deleteWarp_failure_noWarpName() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        FileConfiguration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

        List<Warp> warpList = new ArrayList<Warp>();
        String testWarpName = "testWarpName";
        // make a warp for deletion
        Warp warpToDelete = new Warp(testWarpName, mockPlayerOne.getDisplayName(), WarpType.PRIVATE, mockWorld.getName(), 0, 0, 0, 0, 0);
        // add it to the list we're pre-populating the persistence provider with
        warpList.add(warpToDelete);

        // "put it in the database"
        UnitTestPersistenceProvider testPersistenceProvider = new UnitTestPersistenceProvider();
        testPersistenceProvider.setWarpList(warpList);

        // set this provider as the one to use
        mockNiftyWarpPlugin.getWarpManager().setPersistenceProvider(testPersistenceProvider);

        DeleteWarpCommand deleteWarpCommand = new DeleteWarpCommand(mockNiftyWarpPlugin);

        ////////////////////////////////////////////////////////
        // Run Test(s)
        ////////////////////////////////////////////////////////

        // passing in an empty argument list should fail, since no warp name was provided
        String[] args = new String[]{};

        boolean success = deleteWarpCommand.onCommand(mockPlayerOne,     // player one is the sender
                                                      null,              // currently my command executors don't use the command object
                                                      null,              // currently my command executors don't use the label object
                                                      args);             // command arguments

        ////////////////////////////////////////////////////////
        // Assert/Verify results
        ////////////////////////////////////////////////////////

        // check simple failure first
        assertFalse(success);

        // deeper equality check to ensure our warp wasn't deleted
        assertTrue(testPersistenceProvider.getAllWarps().contains(warpToDelete));
    }
}
