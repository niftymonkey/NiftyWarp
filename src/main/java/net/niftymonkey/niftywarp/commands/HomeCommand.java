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
 * Date: 6/30/11
 * Time: 12:42 AM
 */
public class HomeCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new home command instance
     *
     * @param plugin The base plugin
     */
    public HomeCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_HOME, false, true))
        {
            // find that in the warp map
            Warp warp = plugin.getWarpManager().getWarp(AppStrings.HOME_WARP_NAME, player);

            // get the addon message prefix
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

            if (warp != null)
            {
                // send the player there
                player.teleport(warp.getLocation());

                // let them know it worked
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.GREEN + AppStrings.WARPED_TO_PREFIX +
                                   warp.getWarpType().getTypeColor() + AppStrings.HOME_WARP_NAME);
            }
            else
            {
                // let them know we couldn't find a warp with that name
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.RED + AppStrings.WARP_NOT_FOUND_PREFIX +
                                   ChatColor.WHITE + AppStrings.HOME_WARP_NAME);
            }

            retVal = true;
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}