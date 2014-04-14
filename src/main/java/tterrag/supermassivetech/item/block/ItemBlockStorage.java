package tterrag.supermassivetech.item.block;

import java.util.ArrayList;
import java.util.List;

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
    public String[] getHiddenLines(ItemStack stack)
    {
        return new String[] { "- Stores 2^40 items and fluids", "- Accepts pipe input/output from any side" };
    }

    @Override
    public String[] getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound != null)
        {
            List<String> strs = new ArrayList<String>();

            if (stack.stackTagCompound.getTag("itemStack") != null)
                strs.add(Utils.formatString("Contains ", "", stack.stackTagCompound.getLong("itemsStored"), true, true) + " "
                        + StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));

            if (stack.stackTagCompound.getTag("fluidStack") != null)
                strs.add(Utils.formatString("Contains ", " mB", stack.stackTagCompound.getLong("fluidStored"), true, true) + " "
                        + StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));

            return strs.size() == 0 ? null : strs.toArray(new String[] {});
        }

        return null;
    }
}
