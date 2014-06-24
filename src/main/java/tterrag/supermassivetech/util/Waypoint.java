package tterrag.supermassivetech.util;

import java.awt.Color;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import com.google.common.collect.Sets;

public class Waypoint
{
    public static Set<Waypoint> waypoints = Sets.newConcurrentHashSet();

    public int x, y, z;
    public LinkedList<String> players;
    private boolean isempty = true;
    private Color color;
    private String name;

    public Waypoint()
    {
        name = "Unnamed";
    }

    public Waypoint(String name, int x, int y, int z, EntityPlayer... players)
    {
        this.name = name;

        this.x = x;
        this.y = y;
        this.z = z;

        this.players = new LinkedList<String>();

        for (EntityPlayer e : players)
        {
            this.players.add(e.getCommandSenderName());
        }

        Random rand = new Random();

        color = new Color(rand.nextInt(100), rand.nextInt(100), rand.nextInt(100));

        this.isempty = false;
    }

    public Waypoint addPlayer(EntityPlayer player)
    {
        this.players.add(player.getCommandSenderName());
        return this;
    }

    public Waypoint removePlayer(EntityPlayer player)
    {
        this.players.remove(player.getCommandSenderName());
        return this;
    }

    public boolean viewableBy(EntityPlayer player)
    {
        return players.contains(player.getCommandSenderName());
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Waypoint)
        {
            Waypoint wp = (Waypoint) obj;

            return this.x == wp.x && this.y == wp.y && this.z == wp.z;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return (x + "," + y + "," + z + " " + (isNull() ? "" : players.toString())).hashCode();
    }

    public Color getColor()
    {
        return color;
    }

    public void setColor(Color c)
    {
        this.color = c;
    }

    public void setColor(int r, int g, int b)
    {
        this.color = new Color(r, g, b);
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        if (isNull())
            return;

        tag.setInteger("waypointx", x);
        tag.setInteger("waypointy", y);
        tag.setInteger("waypointz", z);

        NBTTagList list = new NBTTagList();
        for (String s : players)
        {
            list.appendTag(new NBTTagString(s));
        }
        tag.setTag("waypointplayers", list);

        tag.setIntArray("waypointcolor", new int[] {
                color.getRed(), color.getGreen(), color.getBlue()
        });

        tag.setString("waypointname", name);
    }

    public Waypoint readFromNBT(NBTTagCompound tag)
    {
        this.x = tag.getInteger("waypointx");
        this.y = tag.getInteger("waypointy");
        this.z = tag.getInteger("waypointz");

        players = new LinkedList<String>();
        NBTTagList list = tag.getTagList("waypointplayers", Constants.NBT.TAG_STRING);
        for (int i = 0; i < list.tagCount(); i++)
        {
            players.add(list.getStringTagAt(i));
        }

        int[] arr = tag.getIntArray("waypointcolor");

        if (arr.length < 2)
            return this;

        this.color = new Color(arr[0], arr[1], arr[2]);

        this.name = tag.getString("waypointname");

        this.isempty = false;

        return this;
    }

    public boolean isNull()
    {
        return isempty;
    }

    @Override
    public String toString()
    {
        return String.format("x: %d, y: %d, z: %d   %s", x, y, z, !isNull() ? Arrays.deepToString(players.toArray(new String[] {})) : "");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String newName)
    {
        this.name = newName;
    }
}
