package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Mark Lozano
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

        // check for the correct command name
        if (command.getName().equalsIgnoreCase(AppStrings.COMMAND_SET))
        {
            // make sure we have two arguments (warp name and new type)
            if (args.length == 2)
            {
                // Cast to the player object
                Player player = (Player) sender;
                // Check permission
                if(this.plugin.hasPermission(player, AppStrings.COMMAND_SET_PERMISSION, AppStrings.COMMAND_SET))
                {
                    // get the first argument which is the warp name
                    String warpName = args[0];
                    // get the second argument which is the warp type
                    String warpTypeStr = args[1];
                    Warp.Type type = Warp.Type.getTypeForString(warpTypeStr);

                    // get the addon message prefix
                    String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                    // rename this warp from the warp map
                    boolean renamed = plugin.getWarpManager().setWarpType(warpName, type, player);
                    if (renamed)
                    {
                        // let them know that we successfully renamed the warp
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.GREEN + AppStrings.WARP_SET_PREFIX +
                                           ChatColor.WHITE + warpName + type.getTypeColor() + " [" + warpTypeStr + "]");
                    }
                    else
                    {
                        // let them know that we couldn't find that warp
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.RED + AppStrings.WARP_NOT_FOUND_PREFIX +
                                           ChatColor.WHITE + warpName);
                    }

                    retVal = true;
                }
            }
        }

        return retVal;
    }
}