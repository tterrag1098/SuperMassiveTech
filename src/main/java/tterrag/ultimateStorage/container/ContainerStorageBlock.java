package tterrag.ultimateStorage.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import tterrag.ultimateStorage.UltimateStorage;
import tterrag.ultimateStorage.network.PacketStorageBlock;
import tterrag.ultimateStorage.tile.TileStorageBlock;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;

public class ContainerStorageBlock extends Container
{
	private TileStorageBlock tileEnt;
	
	public ContainerStorageBlock(InventoryPlayer par1InventoryPlayer, TileStorageBlock tile)
	{
		bindPlayerInventory(par1InventoryPlayer);
		
		this.tileEnt = tile;
		
		this.addSlotToContainer(new Slot(tile, 0, 48, 94));
		this.addSlotToContainer(new Slot(tile, 1, 184, 20));
		this.addSlotToContainer(new Slot(tile, 2, 184, 81));
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
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(par2);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (par2 == 36 || par2 == 37)
			{
				if (!this.mergeItemStack(itemstack1, 27, 36, false))
				{
					if (!this.mergeItemStack(itemstack1, 0, 27, false))
						return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			if (par2 < 36)
			{
				if (!this.mergeItemStack(itemstack1, 37, 38, false))
					return null;
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
			if (itemstack1.stackSize == 0)
			{
				slot.putStack(null);
				return null;
			}
		}
		return itemstack;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void detectAndSendChanges()
	{
		for (ICrafting c : (List<ICrafting>) crafters)
		{
			PacketStorageBlock packet = new PacketStorageBlock(tileEnt.stored);
			UltimateStorage.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
			UltimateStorage.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(c);
			UltimateStorage.channels.get(Side.SERVER).writeOutbound(packet);
		}
		super.detectAndSendChanges();
	}
}
