package tterrag.supermassivetech.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import tterrag.supermassivetech.block.waypoint.Waypoint;
import tterrag.supermassivetech.util.Utils;

public class TileWaypoint extends TileEntity
{
    public Waypoint waypoint;
    public List<UUID> players;

    public TileWaypoint()
    {
        waypoint = new Waypoint();
        players = new ArrayList<UUID>();
    }

    public void init(EntityPlayer... players)
    {
        if (!worldObj.isRemote)
            waypoint = new Waypoint(this.xCoord, this.yCoord, this.zCoord, players);
        
        for (EntityPlayer p : players)
        {
            this.players.add(p.getUniqueID());
        }
    }

    @Override
    public void updateEntity()
    {
        if (worldObj.isRemote)
        {
            if (waypoint == null || waypoint.isNull() || Waypoint.waypoints.contains(waypoint))
                return;

            Waypoint.waypoints.add(waypoint);
        }
    }

    public void addPlayer(EntityPlayer player)
    {
        Waypoint.waypoints.remove(waypoint);

        waypoint.addPlayer(player);

        Waypoint.waypoints.add(waypoint);
        
        players.add(player.getUniqueID());

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
    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }
    
    public int[] getColorArr()
    {
        return waypoint.isNull() ? new int[]{255, 255, 255} : new int[]{waypoint.getColor().getRed(), waypoint.getColor().getGreen(), waypoint.getColor().getBlue()};
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass > 0;
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        waypoint.writeToNBT(tag);
        Utils.writeUUIDsToNBT(players.toArray(new UUID[]{}), tag, "tileuuids");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        waypoint = waypoint.readFromNBT(tag);
        players = Arrays.asList(Utils.readUUIDsFromNBT("tileuuids", tag));
    }
    
    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }
}
