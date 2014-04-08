package tterrag.supermassivetech.enchant;

import java.util.Map;

import tterrag.supermassivetech.SuperMassiveTech;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantTooltipHandler
{
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public void handleTooltip(ItemTooltipEvent event)
	{
		Map<Integer, Integer> enchantments;
		enchantments = EnchantmentHelper.getEnchantments(event.itemStack);
		for (Integer integer : enchantments.keySet())
		{
			if (SuperMassiveTech.enchantRegistry.getEnchantByID(integer) instanceof IAdvancedEnchant)
			{
				Enchantment e = SuperMassiveTech.enchantRegistry.getEnchantByID(integer);
				for (int i = 0; i < event.toolTip.size(); i++)
				{
					if (event.toolTip.get(i).contains(e.getName()))
					{
						for (String s : ((IAdvancedEnchant)e).getTooltipDetails(event.itemStack))
							event.toolTip.add(i + 1, EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + "  - " + s);
					}
				}
			}
		}
	}
}
