package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import tterrag.supermassivetech.client.GuiStorageBlock;
import tterrag.supermassivetech.network.ISMTPacket;

public class PacketBlackHoleStorage implements ISMTPacket
{
	private long value;
	private long fluidValue;
	private int fluidID;

	public PacketBlackHoleStorage()
	{
		this.value = 0;
		this.fluidValue = 0;
		this.fluidID = 0;
	}

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
		System.out.println("storage");
		if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiStorageBlock)
		{
			((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).itemsStored = buffer.readLong();
			((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).fluidStored = buffer.readLong();
			((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).fluidID = buffer.readInt();
		}
	}
}
