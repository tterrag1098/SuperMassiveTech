package tterrag.supermassivetech.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import tterrag.supermassivetech.util.ClientUtils;

public class MessageStarHeartParticle extends MessageParticle implements IMessageHandler<MessageStarHeartParticle, IMessage>
{
    public MessageStarHeartParticle() {}

    public MessageStarHeartParticle(int... information)
    {
        super(information);
    }
    
    @Override
    public IMessage onMessage(MessageStarHeartParticle message, MessageContext ctx)
    {
        spawnParticle(message.info);
        return null;
    }

    protected void spawnParticle(int[] data)
    {
        ClientUtils.spawnStarHeartParticles(data[3], data[4], data[5], data[0], data[1], data[2]);
    }
}
