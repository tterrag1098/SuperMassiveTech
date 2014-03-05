package tterrag.supermassivetech.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<IStoragePacket>
{
	public ChannelHandler()
	{
		addDiscriminator(0, PacketStorageBlock.class);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, IStoragePacket msg, ByteBuf target) throws Exception
	{
		msg.encodeInto(target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, IStoragePacket msg)
	{
		msg.decodeInto(source);
	}
}
