/**
 * StorageBlock
 * 
 * @author Garrett Spicer-Davis
 */
package tterrag.supermassivetech.common.block.container;

import static tterrag.supermassivetech.common.tile.TileBlackHoleStorage.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.block.ISaveToItem;
import tterrag.supermassivetech.common.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.common.util.Utils;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class BlockBlackHoleStorage extends BlockContainerSMT implements ISaveToItem
{
    public BlockBlackHoleStorage()
    {
        super("blackHoleStorage", Material.iron, soundTypeMetal, 30.0f, TileBlackHoleStorage.class, SuperMassiveTech.renderIDStorage);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileBlackHoleStorage && !player.isSneaking())
        {
            player.openGui(SuperMassiveTech.instance, 0, world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void processBlockPlace(NBTTagCompound tag, TileEntity te)
    {
        long itemsStored = tag.getLong("itemsStored");
        long fluidStored = tag.getLong("fluidStored");

        ItemStack stackStored = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("itemStack"));
        FluidStack fluidStackStored = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluidStack"));

        TileBlackHoleStorage tile = (TileBlackHoleStorage) te;

        tile.storedAmount = itemsStored;
        tile.getTank().amountStored = fluidStored;
        tile.setStoredItemOnPlace(stackStored);
        tile.getTank().setStoredFluidOnPlace(fluidStackStored);
    }

    @Override
    public ItemStack getNBTItem(World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(this);
        TileBlackHoleStorage te = (TileBlackHoleStorage) world.getTileEntity(x, y, z);

        if (te == null)
            return stack;

        NBTTagCompound tag = new NBTTagCompound();

        if (te.getStackInSlot(output) != null && !Utils.stacksEqual(te.getStackInSlot(output), te.getStoredItem()))
        {
            Utils.spawnItemInWorldWithRandomMotion(world, te.getStackInSlot(output), x, y, z);
            te.setInventorySlotContents(output, null);
        }

        long itemAmount = te.storedAmount + (te.getStackInSlot(output) == null ? 0 : te.getStackInSlot(output).stackSize);
        tag.setLong("itemsStored", itemAmount);
        tag.setLong("fluidStored", te.getTank().amountStored);

        if (te.getStoredItem() != null)
        {
            NBTTagCompound itemTag = new NBTTagCompound();
            te.getStoredItem().writeToNBT(itemTag);
            tag.setTag("itemStack", itemTag);
        }

        if (te.getStoredItem() == null && te.getStackInSlot(output) != null)
        {
            NBTTagCompound itemTag = new NBTTagCompound();
            te.getStackInSlot(2).writeToNBT(itemTag);
            tag.setTag("itemStack", itemTag);
        }

        if (te.getTank().getFluidStored() != null)
        {
            NBTTagCompound fluidTag = new NBTTagCompound();
            te.getTank().getFluidStored().writeToNBT(fluidTag);
            tag.setTag("fluidStack", fluidTag);
        }

        stack.stackTagCompound = tag;
        return stack;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
    {
        return new ItemStack(this); // Temp code why doesn't this work D:
    }

    @Override
    public boolean hasPlacementRotation()
    {
        return true;
    }

    @Override
    public boolean hasCustomModel()
    {
        return true;
    }
}
