package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NWUtils;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;

/**
 * User: Mark
 * Date: 7/6/11
 * Time: 1:04 AM
 */
public class WarpToCoordCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new warp to coordinate command instance
     *
     * @param plugin The base plugin
     */
    public WarpToCoordCommand(NiftyWarp plugin)
    {
        this.plugin = plugin;
    }

    /**
     * Called when a command is sent
     *
     * @param sender  The sender (aka player)
     * @param command The command
     * @param label   The label
     * @param args    The arguments used
     * @return Whether the command succeeded or not
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        boolean retVal = false;

        // Cast to the player object
        Player player = (Player) sender;

        // Check permission
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_WARPTOCOORD, false, true))
        {
            // get the addon message prefix
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

            // make sure we have at lease three arguments: x, y, and z
            if (args.length >= 3)
            {
                // get the first three arguments, as they are necessary regardless of world
                String x = args[0];
                String y = args[1];
                String z = args[2];

                // make sure these are valid before we do anything else
                if(NWUtils.isValidDouble(x) || NWUtils.isValidDouble(y) || NWUtils.isValidDouble(z))
                {
                    World world = null;
                    if(args.length == 4)
                    {
                        String worldString = args[3];

                        // check for world ... if none specified, use the player's current world
                        if(NWUtils.isValidWorld(worldString, plugin.getServer()))
                            world = plugin.getServer().getWorld(worldString);
                        else
                        {
                            String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_INVALID_WORLD);
                            Object[] formatValues = new Object[] { worldString };
                            String message = MessageFormat.format(msgFromBundle, formatValues);

                            // let them know that's an invalid world
                            player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                               ChatColor.RED + message);
                        }

                    }
                    else
                        world = player.getWorld();

                    if(world != null)
                    {
                        // send the player there
                        Location location = new Location(world, Double.valueOf(x), Double.valueOf(y), Double.valueOf(z));
                        plugin.getWarpManager().sendPlayerToWarp(player, location, player);

                        String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.WARPED_TO_COORD);
                        Object[] formatValues = new Object[] { x, y, z, world.getName() };
                        String message = MessageFormat.format(msgFromBundle, formatValues);

                        // let them know it worked
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.GREEN + message);
                    }

                    retVal = true;
                }
                else
                {
                    String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_INVALID_COORDINATE);
                    Object[] formatValues = new Object[] { x, y, z };
                    String message = MessageFormat.format(msgFromBundle, formatValues);

                    // let them know we couldn't warp to that spot
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.RED + message);
                }
            }
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}