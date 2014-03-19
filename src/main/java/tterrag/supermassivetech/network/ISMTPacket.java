package tterrag.supermassivetech.network;

import io.netty.buffer.ByteBuf;

/**
 * AbstractPacket class. Should be the parent of all packets wishing to use the
 * PacketPipeline.
 * 
 * @author sirgingalot
 */
public interface ISMTPacket
{
	/**
	 * Encode the packet data into the ByteBuf stream. Complex data sets may
	 * need specific data handlers (See
	 * 
	 * @link{cpw.mods.fml.common.network.ByteBuffUtils )
	 * 
	 * @param buffer the buffer to encode into
	 */
	public abstract void encodeInto(ByteBuf buffer);

	/**
	 * Decode the packet data from the ByteBuf stream. Complex data sets may
	 * need specific data handlers (See
	 * 
	 * @link{cpw.mods.fml.common.network.ByteBuffUtils )
	 * 
	 * @param buffer the buffer to decode from
	 */
	public abstract void decodeInto(ByteBuf buffer);
}