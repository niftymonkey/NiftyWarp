package net.niftymonkey.niftywarp.permissions;

import com.nijikokun.bukkit.Permissions.Permissions;
import net.niftymonkey.niftywarp.AppStrings;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Mark Lozano
 * Date: 7/3/11
 * Time: 3:17 PM
 */
public class PermissionNodeMapper
{
    private static Map<String, String> userCommandToPermissionNodeMap;
    private static Map<String, String> adminCommandToPermissionNodeMap;

    static
    {
        userCommandToPermissionNodeMap = new HashMap<String, String>();
        adminCommandToPermissionNodeMap = new HashMap<String, String>();

        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_ADD, AppStrings.COMMAND_ADD_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_DELETE, AppStrings.COMMAND_DELETE_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_HOME, AppStrings.COMMAND_HOME_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_HOMESET, AppStrings.COMMAND_HOMESET_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_LIST, AppStrings.COMMAND_LIST_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_RENAME, AppStrings.COMMAND_RENAME_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_SET, AppStrings.COMMAND_SET_PERMISSION);
        userCommandToPermissionNodeMap.put(AppStrings.COMMAND_WARP, AppStrings.COMMAND_WARP_PERMISSION);

        adminCommandToPermissionNodeMap.put(AppStrings.COMMAND_DELETE, AppStrings.COMMAND_ADMIN_DELETE_PERMISSION);
        adminCommandToPermissionNodeMap.put(AppStrings.COMMAND_RENAME, AppStrings.COMMAND_ADMIN_RENAME_PERMISSION);
        adminCommandToPermissionNodeMap.put(AppStrings.COMMAND_SET, AppStrings.COMMAND_ADMIN_SET_PERMISSION);
    }

    /**
     * Gets the permission node that governs this command
     *
     * @param command the command string
     *
     * @return the permission node string
     */
    public static String getPermissionNode(String command)
    {
        return userCommandToPermissionNodeMap.get(command);
    }

    /**
     * Gets the admin permission node (if any) that governs this command
     *
     * @param command the command string
     *
     * @return the permission node string
     */
    public static String getAdminPermissionNode(String command)
    {
        return adminCommandToPermissionNodeMap.get(command);
    }

}
