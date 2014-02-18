package tterrag.ultimateStorage.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tterrag.ultimateStorage.tile.TileStorageBlock;

public class ContainerStorageBlock extends Container
{
	private EntityPlayer player;
	private TileStorageBlock tileEnt;
	
	public ContainerStorageBlock(InventoryPlayer par1InventoryPlayer, TileStorageBlock tile)
	{
		bindPlayerInventory(par1InventoryPlayer);

		this.player = par1InventoryPlayer.player;
		this.tileEnt = tile;
		
		this.addSlotToContainer(new Slot(tile, 0, 48, 94));
		this.addSlotToContainer(new Slot(tile, 1, 184, 81));
		this.addSlotToContainer(new Slot(tile, 2, 184, 20));
	}
	
	private void bindPlayerInventory(InventoryPlayer inv)
	{
		int i;

		for (i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(inv, j + i * 9 + 9, j * 18 + 44, 120 + i * 18));
			}
		}

		for (i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(inv, i, i * 18 + 44, 178));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slot)
	{
		ItemStack itemstack1 = null;
		Slot slotObj = (Slot) this.inventorySlots.get(slot);
		if (slotObj != null && slotObj.getHasStack())
		{
			itemstack1 = slotObj.getStack();
			if (slot < 3)
			{
				if (!this.mergeItemStack(itemstack1, 30, 39, false))
				{
					if (!this.mergeItemStack(itemstack1, 3, 27, false)) return null;
				}
				slotObj.onSlotChange(itemstack1, itemstack1);
			}
			else if (slot > 2)
			{
				if (slot < 30)
				{
					if (!this.mergeItemStack(itemstack1, 0, 2, false)) if (!this.mergeItemStack(itemstack1, 30, 39, false)) return null;
					slotObj.onSlotChange(itemstack1, itemstack1);
				}
				else if (slot > 29)
				{
					if (!this.mergeItemStack(itemstack1, 0, 2, false)) if (!this.mergeItemStack(itemstack1, 3, 29, false)) return null;
					slotObj.onSlotChange(itemstack1, itemstack1);
				}
			}
			if (itemstack1.stackSize == 0)
			{
				slotObj.putStack((ItemStack) null);
			}
			else
			{
				slotObj.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack1.stackSize)
			{
				return null;
			}
			slotObj.onPickupFromSlot(player, itemstack1);
			if (itemstack1.stackSize == 0)
			{
				slotObj.putStack(null);
				return null;
			}
		}
		return itemstack1;
	}
}
