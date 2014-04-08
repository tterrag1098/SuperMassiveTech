package tterrag.supermassivetech.item.armor;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class EnchantTooltipHandler
{
	@SubscribeEvent
	public void handleTooltip(ItemTooltipEvent event)
	{
		if (EnchantmentHelper.getEnchantmentLevel(42, event.itemStack) != 0)
		{
			for (int i = 0; i < event.toolTip.size(); i++)
			{
				if (event.toolTip.get(i).contains("Gravity Resist"))
				{
					event.toolTip.add(i + 1, EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + "  - Reduces all gravity effects");
					break;
				}
			}
		}
	}
}
