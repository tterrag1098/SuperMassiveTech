package tterrag.supermassivetech.common.network.message.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.client.util.ClientUtils;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageChargerUpdate extends MessageEnergyUpdate
{
    public MessageChargerUpdate()
    {
    }

    public ItemStack item;

    public MessageChargerUpdate(int x, int y, int z, int stored, ItemStack item)
    {
        super(x, y, z, stored);
        this.item = item;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);
        this.item = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, item);
    }

    public static final class Handler implements IMessageHandler<MessageChargerUpdate, IMessage>
    {
        public Handler()
        {
        }

        @Override
        public IMessage onMessage(MessageChargerUpdate message, MessageContext ctx)
        {
            ClientUtils.updateEnergy(message);
            ClientUtils.updateCharger(message);
            return null;
        }
    }
}
