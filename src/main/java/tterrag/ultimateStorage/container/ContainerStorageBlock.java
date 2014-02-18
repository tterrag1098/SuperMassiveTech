package tterrag.ultimateStorage.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
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
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}
}
