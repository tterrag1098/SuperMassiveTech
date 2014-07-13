package tterrag.supermassivetech.container;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.network.PacketHandler;
import tterrag.supermassivetech.network.message.MessageBlackHoleStorage;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.util.Utils;

public class ContainerBlackHoleStorage extends ContainerSMT
{
    public ContainerBlackHoleStorage(InventoryPlayer par1InventoryPlayer, TileBlackHoleStorage tile)
    {
        super(par1InventoryPlayer, tile);

        this.addSlotToContainer(tile.new SlotInput(tile, 0, 184, 20));
        this.addSlotToContainer(new Slot(tile, 1, 184, 81));
        
        this.addSlotToContainer(tile.new SlotFluidContainer(tile, 2, 32, 91, true));
        this.addSlotToContainer(tile.new SlotFluidContainer(tile, 3, 63, 91, false));
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

            if (par2 >= 36 && par2 <= 39)
            {
                if (!this.mergeItemStack(itemstack1, 27, 36, false))
                {
                    if (!this.mergeItemStack(itemstack1, 0, 27, false))
                        return null;
                }
            }
            
            boolean wasFluidContainer = false;
            if (par2 < 36 && ((Slot) this.inventorySlots.get(38)).isItemValid(itemstack1))
            {
                if (!this.mergeItemStack(itemstack1, 38, 39, false))
                    return null;
                else
                    wasFluidContainer = true;
            }
            if (!wasFluidContainer && par2 < 36 && (Utils.stacksEqual(((TileBlackHoleStorage) tileEnt).getStoredItem(), itemstack1) || ((TileBlackHoleStorage) tileEnt).getStoredItem() == null))
            {
                if (!this.mergeItemStack(itemstack1, 36, 37, false))
                    return null;
            }
            slot.onSlotChange(itemstack1, itemstack);

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
        TileBlackHoleStorage te = (TileBlackHoleStorage) tileEnt;
        for (ICrafting c : (List<ICrafting>) crafters)
        {
            FluidStack fluid = te.getTank().getFluid();
            MessageBlackHoleStorage packet = new MessageBlackHoleStorage(te.storedAmount, te.getTank().amountStored, fluid == null ? -1 : fluid.fluidID);
            PacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) c);
        }
        super.detectAndSendChanges();
    }
}
