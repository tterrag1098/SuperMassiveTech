/**
 * TileStorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class TileStorageBlock extends TileEntity implements ISidedInventory
{
	public ItemStack[] inventory = new ItemStack[3];
	public long stored;
	public final long max = Long.MAX_VALUE;
	
	public TileStorageBlock()
	{
		
	}

	@Override
	public int getSizeInventory()
	{
		return 2;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return inventory[var1];
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inventory[i] != null)
		{
			ItemStack itemstack;

			if (this.inventory[i].stackSize <= j)
			{
				itemstack = this.inventory[i];
				this.inventory[i] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inventory[i].splitStack(j);

				if (this.inventory[i].stackSize == 0)
				{
					this.inventory[i] = null;
				}

				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (this.inventory[i] != null)
		{
			ItemStack itemstack = this.inventory[i];
			this.inventory[i] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventory[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInventoryName()
	{
		return "tterrag.inv.storageBlock";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
		
	}

	@Override
	public void closeInventory()
	{
		
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2)
	{
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return null;
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3)
	{
		return false;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3)
	{
		return true;
	}

}
