package tterrag.supermassivetech.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.network.packet.PacketBlackHoleStorage;
import tterrag.supermassivetech.network.packet.PacketHopperParticle;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;

public class ChannelHandler extends FMLIndexedMessageToMessageCodec<ISMTPacket>
{
	public static EnumMap<Side, FMLEmbeddedChannel> channels;

	public ChannelHandler()
	{
		addDiscriminator(0, PacketBlackHoleStorage.class);
		addDiscriminator(1, PacketHopperParticle.class);
	}

	public static void init()
	{
		channels = SuperMassiveTech.channels;
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

	public void sendToPlayer(EntityPlayer player, ISMTPacket packet)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channels.get(Side.SERVER).writeOutbound(packet);
	}

	public void sendToAll(ISMTPacket packet)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channels.get(Side.SERVER).writeOutbound(packet);
	}

	public void sendToAllInRange(double range, int x, int y, int z, int dimension, ISMTPacket packet)
	{
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new TargetPoint(dimension, x, y, z, range));
		channels.get(Side.SERVER).writeOutbound(packet);
	}
}
