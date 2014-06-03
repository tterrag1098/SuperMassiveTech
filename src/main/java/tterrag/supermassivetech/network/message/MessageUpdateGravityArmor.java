package tterrag.supermassivetech.network.message;

import io.netty.buffer.ByteBuf;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageUpdateGravityArmor implements IMessage, IMessageHandler<MessageUpdateGravityArmor, IMessage>
{
    public enum PowerUps
    {
        HUD, TOOLPICKER
    }
    
    private PowerUps powerup;
    private boolean value;
    private byte index;
    
    public MessageUpdateGravityArmor(){}
    
    public MessageUpdateGravityArmor(PowerUps powerup, boolean value, byte armorIndex)
    {
        this.powerup = powerup;
        this.value = value;
        this.index = armorIndex;
    }
    
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(powerup.ordinal());
        buf.writeBoolean(value);
        buf.writeByte(index);
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.powerup = PowerUps.values()[buf.readInt()];
        this.value = buf.readBoolean();
        this.index = buf.readByte();
    }
    
    public IMessage onMessage(MessageUpdateGravityArmor message, MessageContext ctx)
    {
        ctx.getServerHandler().playerEntity.inventory.armorInventory[message.index].stackTagCompound.setBoolean(message.powerup.toString(), message.value);
        return null;
    }
}
