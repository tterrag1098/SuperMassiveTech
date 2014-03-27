package tterrag.supermassivetech.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStarHarvester extends ItemBlockSMT implements IAdvancedTooltip
{
	public ItemBlockStarHarvester(Block block)
	{
		super(block);
	}

	@Override
	public String[] getHiddenLines(ItemStack stack) 
	{
		return new String[]{
				"- Harvests the power of stars!",
				"- Outputs at the rate of the star inside.",
				"- Once the star is out of power\n   it will go critical. Better run!"
		};
	}

	@Override
	public String[] getStaticLines(ItemStack stack) 
	{
		if (stack.stackTagCompound != null)
		{
			if (stack.stackTagCompound.getTag("inventory") != null)
			{
				ItemStack star = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory"));
				
				return new String[]{
						String.format("Stored: %s", Utils.getType(star).toString()),
						String.format("Power Remaining: %s", Utils.formatString("", " RF", Utils.getStarPowerRemaining(star), false))
				};
			}
		}
		
		return null;
	}
}
