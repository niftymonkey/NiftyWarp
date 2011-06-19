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

    ///////////////////////
    // Helper Methods
    ///////////////////////

    public static String buildFullyQualifiedName(String owner, String warpName)
    {
        return owner + "." + warpName;
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
        return getFullyQualifiedName().compareTo(o.getFullyQualifiedName());
    }
}
