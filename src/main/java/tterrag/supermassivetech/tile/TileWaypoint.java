package tterrag.supermassivetech.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import tterrag.supermassivetech.block.waypoint.Waypoint;

public class TileWaypoint extends TileEntity
{
    private Waypoint waypoint;

    public TileWaypoint()
    {
        waypoint = new Waypoint();
    }

    public void init(EntityPlayer... players)
    {
        waypoint = new Waypoint(this.xCoord, this.yCoord, this.zCoord, players);
    }

    @Override
    public void updateEntity()
    {
        if (!worldObj.isRemote)
        {
//            System.out.println("update " + (waypoint == null ? "null" : waypoint.toString()));
//            System.out.println(Waypoint.waypoints.toString());

            if (waypoint == null || waypoint.isNull())
                return;

            Waypoint.waypoints.add(waypoint);
        }
    }

    public void addPlayer(EntityPlayer player)
    {
        Waypoint.waypoints.remove(waypoint);

        waypoint.addPlayer(player);

        Waypoint.waypoints.add(waypoint);

        this.markDirty();
    }

    @Override
    public boolean canUpdate()
    {
        return waypoint == null || waypoint.isNull() || !Waypoint.waypoints.contains(this.waypoint);
    }

    @Override
    public void invalidate()
    {
        Waypoint.waypoints.remove(waypoint);
        super.invalidate();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        waypoint.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        waypoint = waypoint.readFromNBT(tag);
    }
}
