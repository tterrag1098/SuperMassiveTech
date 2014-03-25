package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStarHarvester extends ItemBlockGravity
{

	public ItemBlockStarHarvester(Block block)
	{
		super(block);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (stack.stackTagCompound != null)
		{
			if (stack.stackTagCompound.getTag("inventory") != null)
			{
				ItemStack star = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory"));
				list.add(String.format("Stored: %s", Utils.getType(star).toString()));
				list.add(String.format("Power Remaining: %s", Utils.formatString("", " RF", Utils.getStarPowerRemaining(star), false)));
			}
		}
	}
}
