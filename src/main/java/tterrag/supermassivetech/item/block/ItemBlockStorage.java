package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStorage extends ItemBlockGravity
{
	public ItemBlockStorage(Block block)
	{
		super(block);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (stack.stackTagCompound != null)
		{
			list.add("Stored: ");

			if (stack.stackTagCompound.getTag("itemStack") != null)
				list.add(Utils.formatString("", stack.stackTagCompound.getLong("itemsStored"), false, true) + " "
						+ StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));

			if (stack.stackTagCompound.getTag("fluidStack") != null)
				list.add(Utils.formatString("", stack.stackTagCompound.getLong("fluidStored"), true, true) + " "
						+ StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));
		}
	}
}
