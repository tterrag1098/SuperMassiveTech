package tterrag.supermassivetech.common.handlers;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class TooltipHandler
{
    @SubscribeEvent
    public void handleTooltip(ItemTooltipEvent event)
    {
        if (event.itemStack == null)
            return;

        if (event.itemStack.getItem() instanceof IAdvancedTooltip)
        {
            IAdvancedTooltip item = (IAdvancedTooltip) event.itemStack.getItem();
            Utils.formAdvancedTooltip(event.toolTip, event.itemStack, item);
        }

        if (Block.getBlockFromItem(event.itemStack.getItem()) instanceof IAdvancedTooltip)
        {
            IAdvancedTooltip block = (IAdvancedTooltip) Block.getBlockFromItem(event.itemStack.getItem());
            Utils.formAdvancedTooltip(event.toolTip, event.itemStack, block);
        }

        if (event.itemStack.getItem() == Items.nether_star && event.itemStack.hasTagCompound() && event.itemStack.getTagCompound().getBoolean("wasRejuvenated"))
        {
            event.toolTip.add("" + EnumChatFormatting.ITALIC + Utils.lang.localize("tooltip.netherStarHot"));
        }
    }
}
