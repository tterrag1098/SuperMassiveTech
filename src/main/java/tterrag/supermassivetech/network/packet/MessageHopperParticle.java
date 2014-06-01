package tterrag.supermassivetech.network.packet;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;

public class MessageHopperParticle extends MessageParticle implements IMessageHandler<MessageHopperParticle, IMessage>
{
    public MessageHopperParticle(){}

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
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, data[3] + 0.5, data[4] + 0.5, data[5] + 0.5, data[0] + 0.5,
                data[1] + 0.5, data[2] + 0.5, 0.1d));
    }
}
