package tterrag.supermassivetech.common.network.message;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class MessageParticle implements IMessage
{
    protected int[] info = new int[6];

    public MessageParticle()
    {}

    public MessageParticle(int... information)
    {
        for (int i = 0; i < this.info.length; i++)
        {
            this.info[i] = information[i];
        }
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        for (int i = 0; i < info.length; i++)
        {
            buffer.writeInt(info[i]);
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        int[] data = new int[6];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = buffer.readInt();
        }
        this.info = data;
    }
}
