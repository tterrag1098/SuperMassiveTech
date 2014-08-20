package tterrag.supermassivetech.common.network.message.tile;

import tterrag.supermassivetech.client.util.ClientUtils;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateBlackHole extends MessageTileEntity implements IMessage, IMessageHandler<MessageUpdateBlackHole, IMessage>
{
    public long energy;

    public MessageUpdateBlackHole() {}
    
    public MessageUpdateBlackHole(int x, int y, int z, long storedEnergy)
    {
        super(x, y, z);
        this.energy = storedEnergy;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        energy = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        buf.writeLong(energy);
    }

    @Override
    public IMessage onMessage(MessageUpdateBlackHole message, MessageContext ctx)
    {
        ClientUtils.updateBlackHole(message);
        return null;
    }
}
