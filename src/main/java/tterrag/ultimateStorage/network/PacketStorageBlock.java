package tterrag.ultimateStorage.network;

import tterrag.ultimateStorage.client.GuiStorageBlock;
import net.minecraft.client.Minecraft;
import io.netty.buffer.ByteBuf;

public class PacketStorageBlock implements IStoragePacket
{
	private long value;
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
		((GuiStorageBlock) Minecraft.getMinecraft().currentScreen).amountStored = buffer.readLong();
	}
}
