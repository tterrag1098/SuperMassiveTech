package tterrag.supermassivetech.common.enchant;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.item.ItemStack;
import tterrag.core.api.common.enchant.IAdvancedEnchant;
import tterrag.supermassivetech.common.config.ConfigHandler;

public class EnchantGravity extends Enchantment implements IAdvancedEnchant
{
    public EnchantGravity()
    {
        super(ConfigHandler.gravEnchantID, 5, EnumEnchantmentType.armor);
    }

    @Override
    public int getMaxEnchantability(int level)
    {
        return super.getMaxEnchantability(level) + 30;
    }

    @Override
    public int getMinEnchantability(int level)
    {
        return super.getMinEnchantability(level);
    }

    @Override
    public int getMaxLevel()
    {
        return 3;
    }

    @Override
    public String getName()
    {
        return "enchantment.gravityResist";
    }

    public double getReduction(double base, int level)
    {
        return (base / this.getMaxLevel()) * level;
    }

    @Override
    public String[] getTooltipDetails(ItemStack stack)
    {
        return new String[] { "Reduces all gravity effects" };
    }
}
