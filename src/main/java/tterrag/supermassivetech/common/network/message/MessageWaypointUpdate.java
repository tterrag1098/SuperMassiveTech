package tterrag.supermassivetech.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.common.tile.TileWaypoint;
import tterrag.supermassivetech.common.util.Waypoint;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageWaypointUpdate implements IMessage, IMessageHandler<MessageWaypointUpdate, IMessage>
{
    public MessageWaypointUpdate()
    {
    }

    private int r, g, b;
    private String name;
    private int x, y, z;

    public MessageWaypointUpdate(int r, int g, int b, String name, int x, int y, int z)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(r);
        buf.writeInt(g);
        buf.writeInt(b);

        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        ByteBufUtils.writeUTF8String(buf, name);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        r = buf.readInt();
        g = buf.readInt();
        b = buf.readInt();

        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();

        name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public IMessage onMessage(MessageWaypointUpdate message, MessageContext ctx)
    {
        World world = ctx.getServerHandler().playerEntity.worldObj;

        TileEntity te = world.getTileEntity(message.x, message.y, message.z);

        if (te != null && te instanceof TileWaypoint)
        {
            TileWaypoint tewp = (TileWaypoint) te;
            Waypoint.waypoints.remove(tewp.waypoint);
            tewp.waypoint.setColor(message.r, message.g, message.b);
            tewp.waypoint.setName(message.name);
            Waypoint.waypoints.add(tewp.waypoint);
        }

        return null;
    }
}
