package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.ItemGravityArmor;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateGravityArmor implements IMessage, IMessageHandler<MessageUpdateGravityArmor, IMessage>
{
    public enum PowerUps
    {
        HUD, TOOLPICKER, GRAV_RESIST, FIELD
    }

    private PowerUps powerup;
    private boolean value;
    private byte[] indeces;

    public MessageUpdateGravityArmor()
    {
    }

    public MessageUpdateGravityArmor(PowerUps powerup, boolean value, byte... indeces)
    {
        this.powerup = powerup;
        this.value = value;
        this.indeces = indeces;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(powerup.ordinal());
        buf.writeBoolean(value);
        buf.writeByte(indeces.length);
        buf.writeBytes(indeces);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.powerup = PowerUps.values()[buf.readInt()];
        this.value = buf.readBoolean();
        byte num = buf.readByte();
        indeces = new byte[num];
        for (int i = 0; i < num; i++)
        {
            indeces[i] = buf.readByte();
        }
    }

    public IMessage onMessage(MessageUpdateGravityArmor message, MessageContext ctx)
    {
        ItemStack[] armor = ctx.getServerHandler().playerEntity.inventory.armorInventory;

        for (int i : message.indeces)
        {
            if (i <= 3 && i >= 0 && armor[i] != null && armor[i].getItem() instanceof ItemGravityArmor)
            {
                armor[i].stackTagCompound.setBoolean(message.powerup.toString(), message.value);
            }
        }
        return null;
    }
}
