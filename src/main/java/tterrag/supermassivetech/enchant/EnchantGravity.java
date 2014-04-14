package tterrag.supermassivetech.enchant;

import tterrag.supermassivetech.config.ConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;

public class EnchantGravity extends Enchantment implements IAdvancedEnchant
{
    public EnchantGravity()
    {
        super(ConfigHandler.gravEnchantID, 2, EnumEnchantmentType.armor);
    }

    @Override
    public int getMaxEnchantability(int par1)
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMinEnchantability(int par1)
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "enchantment.gravityResist";
    }

    @Override
    public String[] getTooltipDetails(ItemStack stack)
    {
        return new String[] { "Reduces all gravity effects" };
    }
}
