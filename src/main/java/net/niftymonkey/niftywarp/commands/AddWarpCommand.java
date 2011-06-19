package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import net.niftymonkey.niftywarp.WarpType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Mark Lozano
 * Date: 6/13/11
 * Time: 2:34 AM
 */
public class AddWarpCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new addwarp command instance
     *
     * @param plugin The base plugin
     */
    public AddWarpCommand(NiftyWarp plugin)
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
        if (command.getName().equalsIgnoreCase(AppStrings.COMMAND_ADD))
        {
            // make sure we have one argument, which is the name for the warp we're about to add
            if (args.length == 1)
            {
                // Cast to the player object
                Player player = (Player) sender;

                // get the first argument which is the warp name
                String warpName = args[0];
                // add this warp to the warp map
                // TODO: handle warp type argument
                Warp warp = plugin.getWarpManager().addWarp(warpName,
                                                            player,
                                                            WarpType.PUBLIC_LISTED,
                                                            player.getLocation());

                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                // let them know that we successfully created the warp
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.GREEN + AppStrings.WARP_ADDED_PREFIX +
                                   warp.getWarpType().getTypeColor() + warpName);

                retVal = true;
            }
        }

        return retVal;
    }
}

