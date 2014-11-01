package tterrag.supermassivetech.common.network.message.tile;

import tterrag.supermassivetech.client.util.ClientUtils;
import tterrag.supermassivetech.common.network.message.MessageParticle;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageHopperParticle extends MessageParticle implements IMessageHandler<MessageHopperParticle, IMessage>
{
    public MessageHopperParticle()
    {}

    public MessageHopperParticle(int... information)
    {
        super(information);
    }

    @Override
    public IMessage onMessage(MessageHopperParticle message, MessageContext ctx)
    {
        spawnParticle(message.info);
        return null;
    }

    public void spawnParticle(int[] data)
    {
        ClientUtils.spawnHopperParticle(data);
    }
}
