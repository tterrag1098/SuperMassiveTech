package tterrag.supermassivetech.network.message;

import tterrag.supermassivetech.registry.Achievements;
import tterrag.supermassivetech.util.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class MessageAchievement implements IMessage, IMessageHandler<MessageAchievement, IMessage>
{
    private ItemStack stack;
    
    public MessageAchievement(){}
    
    public MessageAchievement(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, stack);   
    }
    
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.stack = ByteBufUtils.readItemStack(buf);
    }
    
    @Override
    public IMessage onMessage(MessageAchievement message, MessageContext ctx)
    {
        ClientUtils.unlockClientAchievement(Achievements.getValidItemStack(message.stack));
        return null;
    }
}
