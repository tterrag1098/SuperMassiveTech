package tterrag.supermassivetech.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;
import tterrag.supermassivetech.common.util.Utils;

import com.enderio.core.common.util.EnderStringUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockStarHarvester extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockStarHarvester(Block block)
    {
        super(block);
    }

    @Override
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.lang.localize("tooltip.starHarvester");
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
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound != null)
        {
            if (!stack.stackTagCompound.hasKey("energy"))
                return null;

            String toReturn = "";
            NBTTagCompound tag = stack.stackTagCompound;

            int energy = tag.getInteger("energy");
            if (energy != 0)
            {
                toReturn += String.format("%s: %s", Utils.lang.localize("tooltip.bufferStorage"),
                        EnderStringUtils.getColorFor(energy, TileStarHarvester.MAX_ENERGY) + EnderStringUtils.formatString("", " RF", energy, true, true));
            }
            else
            {
                return null;
            }

            return toReturn;
        }

        return null;
    }
}
