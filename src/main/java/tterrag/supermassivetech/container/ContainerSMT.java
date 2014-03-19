package tterrag.supermassivetech.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import tterrag.supermassivetech.tile.TileSMTInventory;

public abstract class ContainerSMT extends Container
{

	protected TileSMTInventory tileEnt;

	protected ContainerSMT(InventoryPlayer invPlayer, TileSMTInventory tile)
	{
		bindPlayerInventory(invPlayer);
		tileEnt = tile;
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
