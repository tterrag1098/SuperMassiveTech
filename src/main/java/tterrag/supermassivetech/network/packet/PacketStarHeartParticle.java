package tterrag.supermassivetech.network.packet;

import tterrag.supermassivetech.util.ClientUtils;

public class PacketStarHeartParticle extends PacketParticle
{
    public PacketStarHeartParticle()
    {
    }

    public PacketStarHeartParticle(int... information)
    {
        super(information);
    }

    protected void spawnParticle(int[] data)
    {
        ClientUtils.spawnStarHeartParticles(data[3], data[4], data[5], data[0], data[1], data[2]);
    }
}
