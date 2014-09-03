package tterrag.supermassivetech.common.handlers;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.enchant.IAdvancedEnchant;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(types = HandlerType.FORGE)
public class TooltipHandler
{
    @SubscribeEvent
    public void handleTooltip(ItemTooltipEvent event)
    {
        if (event.itemStack == null)
            return;

        if (event.itemStack.stackTagCompound != null)
        {
            handleEnchantTooltip(event);
        }

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

    @SuppressWarnings("unchecked")
    private void handleEnchantTooltip(ItemTooltipEvent event)
    {
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(event.itemStack);

        for (Integer integer : enchantments.keySet())
        {
            if (SuperMassiveTech.enchantRegistry.getEnchantByID(integer) instanceof IAdvancedEnchant)
            {
                Enchantment e = SuperMassiveTech.enchantRegistry.getEnchantByID(integer);
                for (int i = 0; i < event.toolTip.size(); i++)
                {
                    if (event.toolTip.get(i).contains(StatCollector.translateToLocal(e.getName())))
                    {
                        for (String s : ((IAdvancedEnchant) e).getTooltipDetails(event.itemStack))
                            event.toolTip.add(i + 1, EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + "  - " + s);
                    }
                }
            }
        }
    }
}
