package tterrag.supermassivetech.network.packet;

import io.netty.buffer.ByteBuf;
import tterrag.supermassivetech.item.armor.GravityArmorHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageJumpUpdate implements IMessage, IMessageHandler<MessageJumpUpdate, IMessage>
{
    private boolean isJumpKeyDown;

    public MessageJumpUpdate()
    {
    }

    public MessageJumpUpdate(boolean jumped)
    {
        isJumpKeyDown = jumped;
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeBoolean(isJumpKeyDown);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        isJumpKeyDown = buffer.readBoolean();
        GravityArmorHandler.isJumpKeyDown = isJumpKeyDown;
    }
    
    @Override
    public IMessage onMessage(MessageJumpUpdate message, MessageContext ctx)
    {
        GravityArmorHandler.isJumpKeyDown = isJumpKeyDown;
        return null;
    }
}
