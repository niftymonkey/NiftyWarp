package net.niftymonkey.niftywarp;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.EnumType.STRING;

/**
 * Warp object
 *
 * User: Mark Lozano
 * Date: 6/14/11
 * Time: 12:30 AM
 */
@Entity()
@Table(name="nw_warp")
public class Warp implements Serializable, Comparable<Warp>
{
    @Id
    private int id;
    @NotNull
    private String   fullyQualifiedName;

    // warp creation parameters

    @Length(max=30)
    @NotEmpty
    private String   name;
    @NotNull
    private String   owner;
    @NotNull
    @Enumerated(STRING)
    private WarpType warpType;

    // LOCATION ATTRIBUTES - in here separately for serialization purposes

    @NotEmpty
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float  pitch;
    private float  yaw;

    public Warp()
    {
    }

    public Warp(String name, String owner, WarpType warpType, String worldName, double x, double y, double z, float pitch, float yaw)
    {
        setName(name);
        setOwner(owner);
        setWarpType(warpType);
        setWorldName(worldName);
        setX(x);
        setY(y);
        setZ(z);
        setPitch(pitch);
        setYaw(yaw);
    }

    public Warp(String name, String owner, WarpType warpType, Location location)
    {
        setName(name);
        setOwner(owner);
        setWarpType(warpType);
        setLocation(location);
    }

    ///////////////////////
    // Helper Methods
    ///////////////////////

    public static String buildFullyQualifiedName(String owner, String warpName)
    {
        return owner + AppStrings.FQL_DELIMITER + warpName;
    }

    ///////////////////////
    // Getters and Setters
    ///////////////////////

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getFullyQualifiedName()
    {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName)
    {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        setFullyQualifiedName(buildFullyQualifiedName(getOwner(), getName()));
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
        setFullyQualifiedName(buildFullyQualifiedName(getOwner(), getName()));
    }

    public WarpType getWarpType()
    {
        return warpType;
    }

    public void setWarpType(WarpType warpType)
    {
        this.warpType = warpType;
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
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString()
    {
        return this.name + ":" + this.owner + ":" + this.getWarpType();
    }

    /** 
     * (non-Javadoc)
     * @see java.lang.Object#equals(Object) 
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warp warp = (Warp) o;

        if (Float.compare(warp.pitch, pitch) != 0) return false;
        if (Double.compare(warp.x, x) != 0) return false;
        if (Double.compare(warp.y, y) != 0) return false;
        if (Float.compare(warp.yaw, yaw) != 0) return false;
        if (Double.compare(warp.z, z) != 0) return false;
        if (!name.equals(warp.name)) return false;
        if (!owner.equals(warp.owner)) return false;
        if (warpType != warp.warpType) return false;
        if (!worldName.equals(warp.worldName)) return false;

        return true;
    }

    /** 
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + warpType.hashCode();
        result = 31 * result + worldName.hashCode();
        temp = x != +0.0d ? Double.doubleToLongBits(x) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = y != +0.0d ? Double.doubleToLongBits(y) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = z != +0.0d ? Double.doubleToLongBits(z) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }
}
