package tterrag.supermassivetech.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileStarHarvester extends TileSMTInventory implements ISidedInventory, IEnergyHandler, IEnergyInfo
{
	private int slot = 0, currentPerTick = 0;
	private EnergyStorage storage;
	private final int STORAGE_CAP = 100000;
	public double spinSpeed = 0;
	public float spinRot = 0;

	public TileStarHarvester()
	{
		storage = new EnergyStorage(STORAGE_CAP);
		inventory = new ItemStack[1];
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if (inventory[slot] != null && inventory[slot].getItem() instanceof ItemStar)
		{
			IStar type = Utils.getType(inventory[slot]);
			int energy = inventory[slot].getTagCompound().getInteger("energy");
			currentPerTick = type.getPowerPerTick() * 2;
			if (energy > 0)
			{
				inventory[slot].getTagCompound().setInteger("energy", energy - storage.receiveEnergy(energy > currentPerTick ? currentPerTick : energy, false));
			}
			attemptOutputEnergy();
		}
	}

	private void attemptOutputEnergy()
	{
		ForgeDirection f = ForgeDirection.getOrientation(getBlockMetadata());
		TileEntity te = worldObj.getTileEntity(xCoord + f.offsetX, yCoord + f.offsetY, zCoord + f.offsetZ);
		if (te instanceof IEnergyHandler && !(te instanceof TileStarHarvester))
		{
			IEnergyHandler ieh = (IEnergyHandler) te;
			storage.extractEnergy(ieh.receiveEnergy(f.getOpposite(), currentPerTick, false), false);
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}

	@Override
	public boolean isGravityWell()
	{
		return inventory[slot] != null && inventory[slot].getItem() instanceof ItemStar;
	}

	@Override
	public boolean showParticles()
	{
		return true;
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

	public void setEnergyStored(int energy)
	{
		storage.setEnergyStored(energy);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return from.ordinal() == getBlockMetadata();
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
		return ((IStar) inventory[slot].getItem()).getPowerPerTick();
	}

	@Override
	public int getEnergy()
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergy()
	{
		return STORAGE_CAP;
	}

	public boolean handleRightClick(EntityPlayer player)
	{
		ItemStack stack = player.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemStar && inventory[slot] == null)
		{
			ItemStack insert = stack.copy();
			insert.stackSize = 1;
			inventory[slot] = insert;
			player.getCurrentEquippedItem().stackSize--;
			return true;
		}
		else if (!player.isSneaking() && inventory[slot] != null)
		{
			if (!player.inventory.addItemStackToInventory(inventory[slot]))
				player.worldObj.spawnEntityInWorld(new EntityItemIndestructible(player.worldObj, player.posX, player.posY, player.posZ, inventory[slot], 0, 0, 0, 0));

			inventory[slot] = null;
			return true;
		}
		else if (!player.worldObj.isRemote)
		{
			if (inventory[slot] == null)
				player.addChatMessage(new ChatComponentText("No star in place."));
			else
			{
				player.addChatMessage(new ChatComponentText("Current star is: " + Utils.getType(inventory[slot]).toString()));
				player.addChatMessage(new ChatComponentText("Energy remaining: " + Utils.formatString("", " RF", inventory[slot].getTagCompound().getInteger("energy"), true)));
			}
			return false;
		}
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return var1 == 1 ? new int[] { 0 } : new int[] {};
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3)
	{
		return var1 == 0 && var3 == 1 && var2 != null ? var2.getItem() instanceof ItemStar : false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3)
	{
		return var1 == 0 && var3 == 1;
	}
}
