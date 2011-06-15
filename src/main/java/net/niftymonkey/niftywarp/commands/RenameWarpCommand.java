package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
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
public class RenameWarpCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new rename warp command instance
     *
     * @param plugin The base plugin
     */
    public RenameWarpCommand(NiftyWarp plugin)
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
        if (command.getName().equalsIgnoreCase(AppStrings.COMMAND_RENAME))
        {
            // make sure we have two arguments (name and new name)
            if (args.length == 2)
            {
                // Cast to the player object
                Player player = (Player) sender;

                // get the first argument which is the warp name
                String warpName = args[0];
                // get the second argument which is the new warp name
                String newWarpName = args[1];

                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                // rename this warp from the warp map
                boolean renamed = plugin.getWarpManager().renameWarp(warpName, newWarpName, player);
                if (renamed)
                {
                    // let them know that we successfully renamed the warp
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.GREEN + AppStrings.WARP_RENAMED_PREFIX +
                                       ChatColor.WHITE + warpName + " --> " + newWarpName);
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

        return retVal;
    }
}