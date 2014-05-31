package tterrag.supermassivetech.network.packet;

import net.minecraft.client.Minecraft;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;

public class PacketHopperParticle extends PacketParticle
{
    public PacketHopperParticle(){}

    public PacketHopperParticle(int... information)
    {
        super(information);
    }

    @Override
    public void spawnParticle(int[] data)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, data[3] + 0.5, data[4] + 0.5, data[5] + 0.5, data[0] + 0.5,
                data[1] + 0.5, data[2] + 0.5, 0.1d));
    }
}
