package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.network.SMTPacket;
import tterrag.supermassivetech.tile.TileStarHarvester;

public class PacketStarHarvester extends SMTPacket
{
    private NBTTagCompound data;
    private int x, y, z;

    public PacketStarHarvester()
    {
    }

    public PacketStarHarvester(NBTTagCompound tag, int x, int y, int z)
    {
        data = tag;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PacketStarHarvester(int x, int y, int z)
    {
        this(null, x, y, z);
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);

        if (data != null)
        {
            PacketBuffer pb = new PacketBuffer(buffer);
            try
            {
                pb.writeNBTTagCompoundToBuffer(data);
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();

        if (buffer.isReadable())
        {
            try
            {
                data = new PacketBuffer(buffer).readNBTTagCompoundFromBuffer();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
            }
        }

        setSlotContents();
    }

    private void setSlotContents()
    {
        World world = Minecraft.getMinecraft().theWorld;

        if (world != null)
        {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null && t instanceof TileStarHarvester)
            {
                ((TileStarHarvester) t).setInventorySlotContents(0, data == null ? null : ItemStack.loadItemStackFromNBT(data));
            }
        }
    }
}
