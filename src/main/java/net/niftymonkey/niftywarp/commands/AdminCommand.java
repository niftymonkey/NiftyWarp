package net.niftymonkey.niftywarp.commands;

import net.niftymonkey.niftywarp.AppStrings;
import net.niftymonkey.niftywarp.NiftyWarp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 9:32 PM
 */
public class AdminCommand implements CommandExecutor
{
    /**
     * The plugin
     */
    private final NiftyWarp plugin;

    /**
     * Creates a new admin command instance
     *
     * @param plugin The base plugin
     */
    public AdminCommand(NiftyWarp plugin)
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
        if (command.getName().equalsIgnoreCase(AppStrings.COMMAND_ADMIN))
        {
            // Cast to the player object
            Player player = (Player) sender;

            if(this.plugin.hasPermission(player, AppStrings.COMMAND_ADMIN_PERMISSION, AppStrings.COMMAND_ADMIN))
            {
                // TODO: Implement me!
                retVal = true;
            }
        }

        return retVal;
    }
}