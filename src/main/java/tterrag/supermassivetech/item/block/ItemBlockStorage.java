package tterrag.supermassivetech.item.block;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.util.EnumColor;
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
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add(EnumColor.BRIGHT_GREEN + "- Stores 2^40 items and fluids");
			list.add(EnumColor.WHITE + "- Accepts pipe input/output from any side");
		}
		else
		{
			list.add(EnumColor.RED + "Hold" + EnumColor.YELLOW + " -Shift- " + EnumColor.RED + "for more info");
		}
		
		if (stack.stackTagCompound != null)
		{
			list.add("");
			
			if (stack.stackTagCompound.getTag("itemStack") != null)
				list.add(Utils.formatString("Contains ", "", stack.stackTagCompound.getLong("itemsStored"), true, true) + " "
						+ StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));

			if (stack.stackTagCompound.getTag("fluidStack") != null)
				list.add(Utils.formatString("Contains ", " mB", stack.stackTagCompound.getLong("fluidStored"), true, true) + " "
						+ StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));
		}
	}
}
