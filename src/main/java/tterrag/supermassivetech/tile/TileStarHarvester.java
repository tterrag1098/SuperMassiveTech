package tterrag.supermassivetech.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.registry.IStar;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileStarHarvester extends TileSMTInventory implements IInventory, IEnergyHandler, IEnergyInfo
{
	private ItemStack[] inventory = new ItemStack[1];
	private int slot = 0;
	private EnergyStorage storage;
	private final int STORAGE_CAP = 100000;
	
	public TileStarHarvester()
	{
		storage = new EnergyStorage(STORAGE_CAP);
	}
	
	@Override
	public boolean isGravityWell() 
	{
		return false;
	}

	@Override
	public boolean showParticles() 
	{
		return false;
	}

	@Override
	public String getInventoryName() 
	{
		return "tterrag.inventory.starHarvester";
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) 
	{
		return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) 
	{
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from) 
	{
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) 
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) 
	{
		return STORAGE_CAP;
	}

	@Override
	public int getEnergyPerTick() 
	{
		return getMaxEnergyPerTick();
	}

	@Override
	public int getMaxEnergyPerTick() 
	{
		return ((IStar) inventory[slot].getItem()).getMaxPower();
	}

	@Override
	public int getEnergy() 
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergy() {
		return STORAGE_CAP;
	}
}
