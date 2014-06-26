package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import tterrag.supermassivetech.registry.Achievements;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageAchievementToServer implements IMessage, IMessageHandler<MessageAchievementToServer, IMessage>
{
    public MessageAchievementToServer(){}

    @Override
    public void toBytes(ByteBuf buf)
    {
        ;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        ;
    }

    @Override
    public IMessage onMessage(MessageAchievementToServer message, MessageContext ctx)
    {
        Achievements.doFireworkDisplay(ctx.getServerHandler().playerEntity);
        return null;
    }
}
