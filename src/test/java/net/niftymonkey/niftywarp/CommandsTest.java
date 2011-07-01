package net.niftymonkey.niftywarp;

import net.niftymonkey.niftywarp.commands.AddWarpCommand;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: Mark Lozano
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

    @Test
    public void addWarp_success() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        Configuration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

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
        IPersistenceProvider persistenceProvider = mockNiftyWarpPlugin.getWarpManager().getPersistenceProvider();
        assertEquals(persistenceProvider.getAllWarps().get(0).getName(), testWarpName);
        assertEquals(persistenceProvider.getAllWarps().get(0).getOwner(), mockPlayerOne.getDisplayName());
    }

    @Test
    public void addWarp_failure_noWarpName() throws Exception
    {
        ////////////////////////////////////////////////////////
        // Setup test-specific mocks, stubs, and data
        ////////////////////////////////////////////////////////

        World mockWorld = ConstantsAndSMocks.getStubbedWorldMock(ConstantsAndSMocks.WORLD_NAME);
        Location mockLocation = ConstantsAndSMocks.getStubbedLocationMock(mockWorld, 0, 0, 0, 0, 0);
        Player mockPlayerOne = ConstantsAndSMocks.getStubbedPlayerMock(ConstantsAndSMocks.PLAYER_ONE_NAME, mockLocation);
        Configuration mockConfiguration = ConstantsAndSMocks.getStubbedConfigurationMock();
        NiftyWarp mockNiftyWarpPlugin = ConstantsAndSMocks.getStubbedNWPluginMock(mockConfiguration);

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
        IPersistenceProvider persistenceProvider = mockNiftyWarpPlugin.getWarpManager().getPersistenceProvider();
        assertEquals(persistenceProvider.getAllWarps().size(), 0);
    }
}
