package tterrag.supermassivetech.item.block;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBlockHopper extends ItemBlockGravity
{

	public ItemBlockHopper(Block block)
	{
		super(block);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		list.add("Right click with an item to configure");
		list.add("Right click with empty hand to check configuration");
		list.add("Shift right click with an empty hand to clear");
	}
}
