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
 * User: Mark
 * Date: 6/30/11
 * Time: 12:42 AM
 */
public class HomeSetCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new homeset command instance
     *
     * @param plugin The base plugin
     */
    public HomeSetCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_HOMESET, false, true))
        {
            // add this warp to the warp map
            Warp warp = plugin.getWarpManager().addWarp(AppStrings.HOME_WARP_NAME,
                                                        player,
                                                        WarpType.PRIVATE,
                                                        player.getLocation());

            // get the addon message prefix
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

            // let them know it worked
            String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.HOME_SET);
            player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                               ChatColor.GREEN + msgFromBundle);

            retVal = true;
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}

