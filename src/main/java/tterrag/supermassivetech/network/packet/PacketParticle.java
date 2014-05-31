package tterrag.supermassivetech.network.packet;

import tterrag.supermassivetech.network.SMTPacket;
import io.netty.buffer.ByteBuf;

public abstract class PacketParticle extends SMTPacket
{
    protected int[] info = new int[6];

    public PacketParticle()
    {
    }

    public PacketParticle(int... information)
    {
        for (int i = 0; i < this.info.length; i++)
        {
            this.info[i] = information[i];
        }
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        for (int i = 0; i < info.length; i++)
        {
            buffer.writeInt(info[i]);
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        int[] data = new int[6];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = buffer.readInt();
        }
        spawnParticle(data);
    }
    
    protected void spawnParticle(int[] data)
    {
        
    }
}
