package tterrag.supermassivetech.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<ISMTPacket>
{
	public ChannelHandler()
	{
		addDiscriminator(0, PacketBlackHoleStorage.class);
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ISMTPacket msg, ByteBuf target) throws Exception
	{
		msg.encodeInto(target);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, ISMTPacket msg)
	{
		msg.decodeInto(source);
	}
}
