package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tterrag.supermassivetech.client.gui.GuiStorageBlock;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageBlackHoleStorage implements IMessage, IMessageHandler<MessageBlackHoleStorage, IMessage>
{
    private long value;
    private long fluidValue;
    private int fluidID;

    public MessageBlackHoleStorage()
    {
    }

    public MessageBlackHoleStorage(long value, long fluidValue, int fluidID)
    {
        this.value = value;
        this.fluidValue = fluidValue;
        this.fluidID = fluidID;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeLong(value);
        buffer.writeLong(fluidValue);
        buffer.writeInt(fluidID);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        this.value = buffer.readLong();
        this.fluidValue = buffer.readLong();
        this.fluidID = buffer.readInt();
    }

    @Override
    public IMessage onMessage(MessageBlackHoleStorage message, MessageContext ctx)
    {
        GuiScreen gui = Minecraft.getMinecraft().currentScreen;
        
        if (gui != null && gui instanceof GuiStorageBlock)
        {
            GuiStorageBlock storage = (GuiStorageBlock) gui;
            
            storage.itemsStored = message.value;
            storage.fluidStored = message.fluidValue;
            storage.fluidID = message.fluidID;
        }
        
        return null;
    }
}
