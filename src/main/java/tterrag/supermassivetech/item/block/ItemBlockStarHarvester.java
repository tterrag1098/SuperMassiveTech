package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

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
			list.add("Stored: ");

			if (stack.stackTagCompound.getTag("inventory") != null)
				list.add(StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory")).getUnlocalizedName() + ".name"));
		}
	}
}
