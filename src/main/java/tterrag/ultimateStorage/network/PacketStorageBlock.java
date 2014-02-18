package tterrag.ultimateStorage.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import tterrag.ultimateStorage.client.GuiStorageBlock;

public class PacketStorageBlock implements IStoragePacket
{
	private long value;
	public PacketStorageBlock()
	{
		this.value = 0;
	}
	
	public PacketStorageBlock(long value)
	{
		this.value = value;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer)
	{
		buffer.writeLong(value);
	}

	@Override
	public void decodeInto(ByteBuf buffer)
	{
		if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiStorageBlock)
			((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).amountStored = buffer.readLong();
	}
}
