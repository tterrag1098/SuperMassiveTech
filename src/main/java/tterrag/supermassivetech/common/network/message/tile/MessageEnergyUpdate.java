package tterrag.supermassivetech.common.network.message.tile;

import io.netty.buffer.ByteBuf;
import tterrag.supermassivetech.client.util.ClientUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageEnergyUpdate implements IMessage, IMessageHandler<MessageEnergyUpdate, IMessage>
{
    public int x, y, z;
    public int stored;

    public MessageEnergyUpdate()
    {
    }

    public MessageEnergyUpdate(int x, int y, int z, int stored)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.stored = stored;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.stored = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(stored);
    }

    @Override
    public IMessage onMessage(MessageEnergyUpdate message, MessageContext ctx)
    {
        ClientUtils.updateEnergy(message);
        return null;
    }
}
