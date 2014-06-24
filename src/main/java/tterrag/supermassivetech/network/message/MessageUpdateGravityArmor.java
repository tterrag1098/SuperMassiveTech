package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.handlers.ClientKeyHandler.ArmorPowerState;
import tterrag.supermassivetech.item.ItemGravityArmor;
import cpw.mods.fml.common.network.ByteBufUtils;
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
    private ArmorPowerState state;
    private byte[] indeces;

    public MessageUpdateGravityArmor()
    {
    }

    public MessageUpdateGravityArmor(PowerUps powerup, ArmorPowerState state, byte... indeces)
    {
        this.powerup = powerup;
        this.state = state;
        this.indeces = indeces;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(powerup.ordinal());
        ByteBufUtils.writeUTF8String(buf, state.name);
        buf.writeInt(state.color.ordinal());
        buf.writeByte(indeces.length);
        buf.writeBytes(indeces);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.powerup = PowerUps.values()[buf.readInt()];
        this.state = new ArmorPowerState(ByteBufUtils.readUTF8String(buf), buf.readInt());
        byte num = buf.readByte();
        indeces = new byte[num];
        for (int i = 0; i < num; i++)
        {
            indeces[i] = buf.readByte();
        }
    }

    @Override
    public IMessage onMessage(MessageUpdateGravityArmor message, MessageContext ctx)
    {
        ItemStack[] armor = ctx.getServerHandler().playerEntity.inventory.armorInventory;

        for (int i : message.indeces)
        {
            if (i <= 3 && i >= 0 && armor[i] != null && armor[i].getItem() instanceof ItemGravityArmor)
            {
                armor[i].stackTagCompound.setString(message.powerup.toString(), message.state.name);
            }
        }
        return null;
    }
}
