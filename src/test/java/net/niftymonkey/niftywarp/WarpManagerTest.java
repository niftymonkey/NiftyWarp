package net.niftymonkey.niftywarp;

import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Author: Mark Lozano
 */
public class WarpManagerTest
{
    private NiftyWarp mockNiftyWarpPlugin = null;
    private Player mockPlayerOne = null;
    private IPersistenceProvider testWarpProvider = null;

    @Before
    public void setUp() throws Exception
    {
        mockNiftyWarpPlugin = Mockito.mock(NiftyWarp.class);
        mockPlayerOne = Mockito.mock(Player.class);
        testWarpProvider = new TestPersistenceProvider();

        when(mockPlayerOne.getDisplayName()).thenReturn(TestPersistenceProvider.PLAYER_ONE_NAME);
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testGetWarpsForUser() throws Exception
    {
        WarpManager warpManager = new WarpManager(mockNiftyWarpPlugin);
        List<Warp> warpsForUser = warpManager.getWarpsForUser(TestPersistenceProvider.PLAYER_ONE_NAME, mockPlayerOne, testWarpProvider);

        // hidden warps that belong to others should be the only ones not returned
        assertEquals(7, warpsForUser.size());
    }
}
