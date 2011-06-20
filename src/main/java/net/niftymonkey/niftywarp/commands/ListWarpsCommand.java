package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 12:01 AM
 */
public class ListWarpsCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new list warps command instance
     *
     * @param plugin The base plugin
     */
    public ListWarpsCommand(NiftyWarp plugin)
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_LIST_PERMISSION, AppStrings.COMMAND_LIST))
        {
            if (args.length == 0)
            {
                // get the addon message prefix
                String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

                // get the list of warps
                // TODO: implement getting warps for a different user (using the first param of the getWarpsForUser() method)
                List<Warp> warpsList = plugin.getWarpManager().getWarpsForUser(player.getDisplayName(), player);
                Collections.sort(warpsList);

                if (warpsList.size() > 0)
                {
                    String availableWarpStr = "";

                    int i = 0;
                    while (i < warpsList.size())
                    {
                        // get the warp
                        Warp warp = warpsList.get(i);
                        // get the name
                        String warpName;
                        // if this is owned by someone else, we need to show the fully qualified name instead so that it can be
                        // looked up correctly on use
                        if(!warp.getOwner().equalsIgnoreCase(player.getDisplayName()))
                            warpName = warp.getFullyQualifiedName();
                        else
                            warpName = warp.getName();


                        // append it to the string
                        availableWarpStr += warp.getWarpType().getTypeColor() + warpName;
                        // increment counter
                        i++;

                        // if this was not the last one, append a comma and space
                        if(i != warpsList.size())
                            availableWarpStr += ", ";
                    }

                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.GREEN + AppStrings.AVAILABLE_WARPS_PREFIX +
                                       ChatColor.WHITE + availableWarpStr);
                }
                else
                {
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.GREEN + AppStrings.AVAILABLE_WARPS_PREFIX +
                                       ChatColor.WHITE + AppStrings.NO_AVAILABLE_WARPS);
                }

                retVal = true;
            }
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}
