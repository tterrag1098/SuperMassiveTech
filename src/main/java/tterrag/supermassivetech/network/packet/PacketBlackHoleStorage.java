package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import tterrag.supermassivetech.client.gui.GuiStorageBlock;
import tterrag.supermassivetech.network.SMTPacket;

public class PacketBlackHoleStorage extends SMTPacket
{
    private long value;
    private long fluidValue;
    private int fluidID;

    public PacketBlackHoleStorage(){}
    
    public PacketBlackHoleStorage(long value, long fluidValue, int fluidID)
    {
        this.value = value;
        this.fluidValue = fluidValue;
        this.fluidID = fluidID;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeLong(value);
        buffer.writeLong(fluidValue);
        buffer.writeInt(fluidID);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiStorageBlock)
        {
            ((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).itemsStored = buffer.readLong();
            ((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).fluidStored = buffer.readLong();
            ((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).fluidID = buffer.readInt();
        }
    }
}
