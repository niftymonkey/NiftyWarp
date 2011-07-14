package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.WarpType;
import net.niftymonkey.niftywarp.exceptions.InternalPermissionsException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Mark
 * Date: 6/14/11
 * Time: 9:33 PM
 */
public class SetWarpTypeCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new set warp type command instance
     *
     * @param plugin The base plugin
     */
    public SetWarpTypeCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_SET, false, true))
        {
            // make sure we have two arguments (warp name and new type)
            if (args.length == 2)
            {
                // get the first argument which is the warp name
                String warpName = args[0];
                // get the second argument which is the warp type
                String warpTypeStr = args[1];
                // get a type that matches the param
                WarpType warpType = WarpType.getTypeForString(warpTypeStr);

                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                if(warpType != null)
                {
                    try
                    {
                        boolean typeSetSuccess = plugin.getWarpManager().setWarpType(warpName, warpType, player);
                        if (typeSetSuccess)
                        {
                            // let them know that we successfully renamed the warp
                            player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                               ChatColor.GREEN + AppStrings.WARP_SET_PREFIX +
                                               ChatColor.WHITE + warpName + warpType.getTypeColor() + " [" + warpTypeStr + "]");
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
                else
                {
                    // let them know that we couldn't find a warp type that matches their param
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.RED + warpTypeStr + AppStrings.WARP_TYPE_NOT_FOUND_SUFFIX);
                }
            }
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}