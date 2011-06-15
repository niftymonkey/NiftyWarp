package net.niftymonkey.niftywarp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;

/**
 * Warp object
 *
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 12:30 AM
 */
public class Warp implements Serializable, Comparable<Warp>
{
    public enum Type
    {
        // only the owner of this warp can see it in their list, and only the owner can use this warp
        PRIVATE,

        // Anyone can see this warp in their list
        PUBLIC_LISTED,

        // Only the owner of this warp will see it in their list, however others can still use this warp
        PUBLIC_UNLISTED;

        public static Type getTypeForString(String typeStr)
        {
            // default to private
            Type retVal = PRIVATE;

            if(typeStr.equals(AppStrings.WARP_TYPE_LISTED))
                retVal = PUBLIC_LISTED;
            if(typeStr.equals(AppStrings.WARP_TYPE_UNLISTED))
                retVal = PUBLIC_UNLISTED;

            return retVal;
        }

        /**
         * Gets the ChatColor for the type of Warp this is
         *
         * @return the relevant ChatColor
         */
        public ChatColor getTypeColor()
        {
            ChatColor retVal = ChatColor.WHITE;

            if(this.equals(PRIVATE))
                retVal = ChatColor.DARK_GRAY;
            if(this.equals(PUBLIC_UNLISTED))
                retVal = ChatColor.DARK_PURPLE;

            return retVal;
        }
    }

    // warp creation parameters
    private String   name;
    private String   owner;
    private Warp.Type type;

    // LOCATION ATTRIBUTES - in here separately for serialization purposes

    private String worldName;
    private double x;
    private double y;
    private double z;
    private float  pitch;
    private float  yaw;


    public static String buildId(String owner, String warpName)
    {
        return owner + "." + warpName;
    }

    ///////////////////////
    // Getters and Setters
    ///////////////////////

    public String getId()
    {
        return buildId(getOwner(), getName());
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public Warp.Type getType()
    {
        return type;
    }

    public void setType(Warp.Type type)
    {
        this.type = type;
    }

    public void setLocation(Location location)
    {
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.pitch = location.getPitch();
        this.yaw = location.getYaw();
    }

    public Location getLocation()
    {
        World world = Bukkit.getServer().getWorld(worldName);
        return new Location(world, x, y, z, yaw, pitch);
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setWorldName(String worldName)
    {
        this.worldName = worldName;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public double getZ()
    {
        return z;
    }

    public void setZ(double z)
    {
        this.z = z;
    }

    public float getPitch()
    {
        return pitch;
    }

    public void setPitch(float pitch)
    {
        this.pitch = pitch;
    }

    public float getYaw()
    {
        return yaw;
    }

    public void setYaw(float yaw)
    {
        this.yaw = yaw;
    }

    public int compareTo(Warp o)
    {
        return getId().compareTo(o.getId());
    }
}
