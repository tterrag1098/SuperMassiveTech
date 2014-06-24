package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import tterrag.supermassivetech.util.ClientUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateVenting implements IMessage, IMessageHandler<MessageUpdateVenting, IMessage>
{
    public MessageUpdateVenting()
    {
    }

    private int x, y, z;
    private boolean venting;

    public MessageUpdateVenting(int x, int y, int z, boolean venting)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.venting = venting;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(venting);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        venting = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(MessageUpdateVenting message, MessageContext ctx)
    {
        ClientUtils.setStarHarvesterVenting(message.x, message.y, message.z, message.venting);
        return null;
    }
}
