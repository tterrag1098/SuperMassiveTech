package tterrag.supermassivetech.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.network.PacketStorageBlock;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.relauncher.Side;

public class ContainerStorageBlock extends ContainerSMT
{
	public ContainerStorageBlock(InventoryPlayer par1InventoryPlayer, TileBlackHoleStorage tile)
	{
		super(par1InventoryPlayer, tile);

		this.addSlotToContainer(tile.new SlotFluidContainer(tile, 0, 48, 94));
		this.addSlotToContainer(tile.new SlotInput(tile, 1, 184, 20));
		this.addSlotToContainer(new Slot(tile, 2, 184, 81));
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

			if (par2 >= 36 && par2 <= 38)
			{
				if (!this.mergeItemStack(itemstack1, 27, 36, false))
				{
					if (!this.mergeItemStack(itemstack1, 0, 27, false))
						return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			if (par2 < 36 && (TileBlackHoleStorage.stacksEqual(((TileBlackHoleStorage) tileEnt).getStoredItem(), itemstack1) || ((TileBlackHoleStorage) tileEnt).getStoredItem() == null))
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
			if (itemstack1.stackSize == itemstack.stackSize) { return null; }
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
		TileBlackHoleStorage te = (TileBlackHoleStorage) tileEnt;
		for (ICrafting c : (List<ICrafting>) crafters)
		{
			FluidStack fluid = te.getTank().getFluid();
			PacketStorageBlock packet = new PacketStorageBlock(te.storedAmount, te.getTank().amountStored, fluid == null ? 0 : fluid.fluidID);
			SuperMassiveTech.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
			SuperMassiveTech.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(c);
			SuperMassiveTech.channels.get(Side.SERVER).writeOutbound(packet);
		}
		super.detectAndSendChanges();
	}
}
