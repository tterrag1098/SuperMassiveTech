package tterrag.supermassivetech.network.message;

import tterrag.supermassivetech.util.ClientUtils;
import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChargerUpdate implements IMessage, IMessageHandler<MessageChargerUpdate, IMessage>
{
    public MessageChargerUpdate() {}
    
    private int x, y, z;
    private int stored;
    private boolean hasInventory;
    
    public MessageChargerUpdate(int x, int y, int z, int stored, boolean hasInventory)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.stored = stored;
        this.hasInventory = hasInventory;
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.stored = buf.readInt();
        this.hasInventory = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(stored);
        buf.writeBoolean(hasInventory);
    }
    
    @Override
    public IMessage onMessage(MessageChargerUpdate message, MessageContext ctx)
    {
        ClientUtils.updateCharger(message.x, message.y, message.z, message.stored, message.hasInventory);
        return null;
    }
}
