package tterrag.supermassivetech.api.common.enchant;

import net.minecraft.item.ItemStack;

public interface IAdvancedEnchant
{
    public String[] getTooltipDetails(ItemStack stack);
}
