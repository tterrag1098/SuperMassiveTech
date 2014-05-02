package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import tterrag.supermassivetech.item.armor.GravityArmorHandler;
import tterrag.supermassivetech.network.SMTPacket;

public class PacketJumpUpdate extends SMTPacket
{
    private boolean isJumpKeyDown;

    public PacketJumpUpdate()
    {
    }

    public PacketJumpUpdate(boolean jumped)
    {
        isJumpKeyDown = jumped;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        buffer.writeBoolean(isJumpKeyDown);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        isJumpKeyDown = buffer.readBoolean();
        GravityArmorHandler.isJumpKeyDown = isJumpKeyDown;
        System.out.println(isJumpKeyDown);
    }
}
