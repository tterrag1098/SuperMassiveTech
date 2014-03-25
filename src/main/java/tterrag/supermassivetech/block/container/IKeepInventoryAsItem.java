package tterrag.supermassivetech.block.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface IKeepInventoryAsItem
{
	public ItemStack getNBTItem(World world, int x, int y, int z);
	
	public void processBlockPlace(NBTTagCompound tag, TileEntity te);
	
	public void dropItem(World world, ItemStack item, int x, int y, int z);
}
