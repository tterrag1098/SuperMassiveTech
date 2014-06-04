package tterrag.supermassivetech.item.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStorage extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockStorage(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.blackHoleStorage", true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound != null)
        {
            List<String> strs = new ArrayList<String>();

            if (stack.stackTagCompound.getTag("itemStack") != null)
                strs.add(Utils.formatString(Utils.localize("tooltip.stored", true) + ": ", "", stack.stackTagCompound.getLong("itemsStored"), true, true) + " "
                        + StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));

            if (stack.stackTagCompound.getTag("fluidStack") != null)
                strs.add(Utils.formatString(Utils.localize("tooltip.stored", true) + ": ", " mB", stack.stackTagCompound.getLong("fluidStored"), true, true) + " "
                        + StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));

            return Utils.makeTooltipString(strs);
        }

        return null;
    }
}
