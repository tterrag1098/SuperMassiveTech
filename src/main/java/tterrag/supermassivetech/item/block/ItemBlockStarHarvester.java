package tterrag.supermassivetech.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStarHarvester extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockStarHarvester(Block block)
    {
        super(block);
    }

    @Override
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.starHarvester", true);
    }

    @Override
    public boolean isGravityWell(ItemStack stack)
    {
        return stack.stackTagCompound != null && stack.stackTagCompound.getTag("inventory") != null;
    }

    @Override
    public int getGravStrength(ItemStack stack)
    {
        if (isGravityWell(stack))
        {
            ItemStack star = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory"));
            return Utils.getType(star).getTier().ordinal();
        }
        return 0;
    }

    @Override
    public String getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound != null)
        {
            if (stack.stackTagCompound.getTag("inventory") != null)
            {
                ItemStack star = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory"));

                IStar type = Utils.getType(star);
                return String.format("%s: %s", Utils.localize("tooltip.stored", true), type.getTextColor() + type.toString()) + "~" + 
                       String.format("%s: %s", Utils.localize("tooltip.powerRemaining", true), Utils.getColorForPowerLeft(type.getPowerStored(star), type.getPowerStoredMax()) + Utils.formatString("", " RF", Utils.getStarPowerRemaining(star), false));
            }
        }

        return null;
    }
}
