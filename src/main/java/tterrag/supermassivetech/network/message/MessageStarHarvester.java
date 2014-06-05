package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import tterrag.supermassivetech.util.ClientUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageStarHarvester implements IMessage, IMessageHandler<MessageStarHarvester, IMessage>
{
    private NBTTagCompound data;
    private int x, y, z;
    private boolean venting;

    public MessageStarHarvester()
    {
    }

    public MessageStarHarvester(NBTTagCompound tag, int x, int y, int z, boolean venting)
    {
        data = tag;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public MessageStarHarvester(int x, int y, int z, boolean venting)
    {
        this(null, x, y, z, venting);
    }

    @Override
    public void toBytes(ByteBuf buffer)
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
        
        buffer.writeBoolean(venting);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
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
        
        venting = buffer.readBoolean();
    }
    
    @Override
    public IMessage onMessage(MessageStarHarvester message, MessageContext ctx)
    {
        ClientUtils.setStarHarvetserSlotContents(message.data, message.x, message.y, message.z, message.venting);
        return null;
    }
}
