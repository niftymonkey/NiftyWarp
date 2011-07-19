package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NWUtils;
import net.niftymonkey.niftywarp.NiftyWarp;
import net.niftymonkey.niftywarp.Warp;
import net.niftymonkey.niftywarp.WarpType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Mark
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
        if(this.plugin.hasPermission(player, AppStrings.COMMAND_LIST, false, true))
        {
            // get the addon message prefix
            String addonMsgPrefix = AppStrings.getAddonMsgPrefix(plugin);

            String worldStrFilter = null;
            WarpType warpTypeFilter = null;

            // perhaps they supplied a world filter
            if (args.length >= 1)
            {
                worldStrFilter = args[0];
                // if worldstr is asterisk, they want all worlds, so no need to filter on that
                if(!worldStrFilter.equals("*"))
                {
                    if(!NWUtils.isValidWorld(worldStrFilter, plugin.getServer()))
                    {
                        String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_INVALID_WORLD);
                        Object[] formatValues = new Object[] { worldStrFilter };
                        String message = MessageFormat.format(msgFromBundle, formatValues);

                        // let them know that's an invalid world
                        player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                           ChatColor.RED + message);

                        return false;
                    }
                }
                else
                {
                    worldStrFilter = null;
                }
            }

            // perhaps they supplied a type filter
            if (args.length == 2)
            {
                String typeStr = args[1];
                warpTypeFilter = WarpType.getTypeForString(typeStr);
                if(warpTypeFilter == null)
                {
                    String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.ERR_INVALID_WARP_TYPE);
                    Object[] formatValues = new Object[] { typeStr };
                    String message = MessageFormat.format(msgFromBundle, formatValues);

                    // let them know that's an invalid warp type
                    player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                       ChatColor.RED + message);

                    return false;
                }
            }

            // get the list of warps this user is allowed to see
            // TODO: perhaps implement getting warps for a different user (using the first param of the getAvailableWarpsForUser() method)?
            List<Warp> warpsList = plugin.getWarpManager().getVisibleWarpsForUser(player.getDisplayName(), player);

            // NOTE: temporary fix
            // remove "filtered" warps from the warp list
            // TODO:  this should be done as part of the initial "query" that the WarpManager is doing above
            List<Warp> tmpList = new ArrayList<Warp>(warpsList.size());
            tmpList.addAll(warpsList);
            for(Warp tmpWarp: tmpList)
            {
                if(worldStrFilter != null)
                {
                    if(!tmpWarp.getWorldName().equalsIgnoreCase(worldStrFilter))
                        warpsList.remove(tmpWarp);
                }
                if(warpTypeFilter != null)
                {
                    if(tmpWarp.getWarpType() != warpTypeFilter)
                        warpsList.remove(tmpWarp);
                }
            }

            if(warpsList.size() > 0)
            {
                // Make a list of warps that are owned by this user so we can display owned and non-owned warps separately.
                // These will be displayed first and always with just the name
                List<Warp> playerOwnedWarps = new ArrayList<Warp>();
                for(Warp warp: warpsList)
                {

                    if(warp.getOwner().equalsIgnoreCase(player.getDisplayName()))
                    {
                        playerOwnedWarps.add(warp);
                    }
                }

                // sort the lists
                Collections.sort(playerOwnedWarps);
                Collections.sort(warpsList);

                String playerOwnedWarpsStr = "";
                String otherOwnedWarpStr = "";

                if (playerOwnedWarps.size() > 0)
                {
                    // Iterate over player owned warps
                    int i = 0;
                    while (i < playerOwnedWarps.size())
                    {
                        // get the warp
                        Warp warp = playerOwnedWarps.get(i);
                        // append it to the string
                        playerOwnedWarpsStr += warp.getWarpType().getTypeColor() + warp.getName();
                        // increment counter
                        i++;

                        // if this was not the last one, append a comma and space
                        if(i != playerOwnedWarps.size())
                            playerOwnedWarpsStr += ", ";
                    }
                }

                // make a map that is keyed off of the name of the warp.  This allows me to easily identify naming collisions when
                // deciding how to display the warp name in the loop below
                Map<String, List<Warp>> nameToWarpListMapping = new HashMap<String, List<Warp>>();

                List<Warp> tmpWarpList = null;
                for(Warp warp: warpsList)
                {
                    // either get the existing list of warps with this name from the map, or make a new list with this warp
                    if(nameToWarpListMapping.containsKey(warp.getName()))
                        tmpWarpList = nameToWarpListMapping.get(warp.getName());
                    else
                        tmpWarpList = new ArrayList<Warp>();

                    // add to the list
                    tmpWarpList.add(warp);

                    // put the list back into the map
                    nameToWarpListMapping.put(warp.getName(), tmpWarpList);
                }

                String warpName = null;
                Set<Map.Entry<String,List<Warp>>> nameToWarpListEntries = nameToWarpListMapping.entrySet();
                for (Map.Entry<String, List<Warp>> next : nameToWarpListEntries)
                {
                    List<Warp> warpsWithSameName = next.getValue();
                    boolean useFullyQualifiedNames = (warpsWithSameName.size() > 1);

                    for (Warp warp : warpsWithSameName)
                    {
                        // only process this if it is not owned by player, since we already made a list for the player owned ones
                        if (warp.getOwner().equalsIgnoreCase(player.getDisplayName()))
                            continue;

                        warpName = useFullyQualifiedNames ? warp.getFullyQualifiedName() : warp.getName();

                        // append it to the string
                        otherOwnedWarpStr += warp.getWarpType().getTypeColor() + warpName;

                        otherOwnedWarpStr += ", ";
                    }
                }

                // remove the last comma and whitespace
                if(otherOwnedWarpStr.endsWith(", "))
                    otherOwnedWarpStr = otherOwnedWarpStr.trim().substring(0, otherOwnedWarpStr.length()-2);

                // create a line that shows the warp type names in their color for clarity
                String warpTypesInfo = ChatColor.WHITE + "(" +
                                       WarpType.LISTED.getTypeColor() + AppStrings.WARP_TYPE_LISTED + ", " +
                                       WarpType.UNLISTED.getTypeColor() + AppStrings.WARP_TYPE_UNLISTED + ", " +
                                       WarpType.PRIVATE.getTypeColor() + AppStrings.WARP_TYPE_PRIVATE +
                                       ChatColor.WHITE + ")";

                // output warps
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix + warpTypesInfo);

                String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.WARPS_YOURS);
                Object[] formatValues = new Object[] { playerOwnedWarpsStr };
                String message = MessageFormat.format(msgFromBundle, formatValues);

                player.sendMessage(ChatColor.GREEN + message);

                msgFromBundle = plugin.getMessageBundle().getString(AppStrings.WARPS_OTHERS);
                formatValues = new Object[] { otherOwnedWarpStr };
                message = MessageFormat.format(msgFromBundle, formatValues);

                player.sendMessage(ChatColor.GREEN + message);

            }
            else
            {
                // inform of no warps
                String msgFromBundle = plugin.getMessageBundle().getString(AppStrings.NO_AVAILABLE_WARPS);
                player.sendMessage(ChatColor.AQUA + addonMsgPrefix +
                                   ChatColor.GREEN + msgFromBundle);
            }

            retVal = true;
        }
        else
            retVal = true; // in the case of permissions failure, we still need to return true so that no usage is printed

        return retVal;
    }
}
