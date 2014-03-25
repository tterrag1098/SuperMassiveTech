package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileStarHarvester;

public class BlockStarHarvester extends BlockContainerSMT implements IKeepInventoryAsItem
{
	public BlockStarHarvester()
	{
		super("tterrag.starHarvester", Material.iron, soundTypeMetal, 15.0f, TileStarHarvester.class, SuperMassiveTech.renderIDStarHarvester);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		boolean returnVal = false;
		if (!world.isRemote && te instanceof TileStarHarvester)
		{
			returnVal = ((TileStarHarvester) te).handleRightClick(player);
		}
		return returnVal;
	}

	@Override
	public ItemStack getNBTItem(World world, int x, int y, int z) 
	{
		ItemStack stack = new ItemStack(this);
		TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);
		
		if (te == null)
			return stack;
		
		stack.stackTagCompound = new NBTTagCompound();
		
		if (te.getStackInSlot(0) != null)
		{
			NBTTagCompound invTag = new NBTTagCompound();
			te.getStackInSlot(0).writeToNBT(invTag);
			stack.stackTagCompound.setTag("inventory", invTag);
		}
		
		stack.stackTagCompound.setInteger("energy", te.getEnergyStored(ForgeDirection.UNKNOWN));
		
		return stack;
	}

	@Override
	public void processBlockPlace(NBTTagCompound tag, TileEntity te) 
	{
		if (te instanceof TileStarHarvester)
		{
			TileStarHarvester harvester = (TileStarHarvester) te;
			
			harvester.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("inventory")));
			harvester.setEnergyStored(tag.getInteger("energy"));
		}
	}

	@Override
	public void dropItem(World world, ItemStack item, int x, int y, int z) 
	{
		
	}
}
