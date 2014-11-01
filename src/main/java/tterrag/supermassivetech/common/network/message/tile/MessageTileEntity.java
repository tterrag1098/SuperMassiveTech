package tterrag.supermassivetech.common.network.message.tile;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;

public abstract class MessageTileEntity implements IMessage
{
    public int x, y, z;

    public MessageTileEntity()
    {}

    public MessageTileEntity(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }
}
