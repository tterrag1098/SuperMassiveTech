package tterrag.supermassivetech.common.network.message.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import tterrag.supermassivetech.client.util.ClientUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageStarHarvester implements IMessage, IMessageHandler<MessageStarHarvester, IMessage>
{
    private NBTTagCompound data;
    private int x, y, z;
    private double spinSpeed;

    public MessageStarHarvester()
    {
    }

    public MessageStarHarvester(NBTTagCompound tag, int x, int y, int z, double spinSpeed)
    {
        data = tag;
        this.x = x;
        this.y = y;
        this.z = z;
        this.spinSpeed = spinSpeed;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeDouble(spinSpeed);

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
    public void fromBytes(ByteBuf buffer)
    {
        x = buffer.readInt();
        y = buffer.readInt();
        z = buffer.readInt();
        spinSpeed = buffer.readDouble();

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
    }

    @Override
    public IMessage onMessage(MessageStarHarvester message, MessageContext ctx)
    {
        ClientUtils.updateStarHarvester(message.data, message.x, message.y, message.z, message.spinSpeed);
        return null;
    }
}
