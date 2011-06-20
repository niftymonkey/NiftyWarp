package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.exceptions.InternalPermissionsException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 9:32 PM
 */
public class DeleteWarpCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new remove warp command instance
     *
     * @param plugin The base plugin
     */
    public DeleteWarpCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_DELETE_PERMISSION, AppStrings.COMMAND_DELETE))
        {
            // make sure we have one argument, which is the name for the warp we're about to add
            if (args.length == 1)
            {
                // get the first argument which is the warp name
                String warpName = args[0];

                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                try
                {
                    boolean removed = plugin.getWarpManager().deleteWarp(warpName, player);
                    if (removed)
                    {
                        // let them know that we successfully removed the warp
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.GREEN + AppStrings.WARP_REMOVED_PREFIX +
                                           ChatColor.WHITE + warpName);
                    }
                    else
                    {
                        // let them know that we couldn't find that warp
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.RED + AppStrings.WARP_NOT_FOUND_PREFIX +
                                           ChatColor.WHITE + warpName);
                    }
                }
                catch (InternalPermissionsException e)
                {
                    // let them know they don't have permission to do that
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.RED + e.getMessage());
                }

                retVal = true;
            }
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}