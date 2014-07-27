package tterrag.supermassivetech.block.container;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.energy.TileCharger;

public class BlockCharger extends BlockContainerSMT
{
    public BlockCharger()
    {
        super("charger", Material.iron, soundTypeMetal, 5.0f, TileCharger.class, SuperMassiveTech.renderIDCharger);
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(Reference.MOD_TEXTUREPATH + ":chargerBlob");
    }

    // Emits redstone if inv is empty, or if energy container is full
    @Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
        TileCharger te = (TileCharger) world.getTileEntity(x, y, z);
        ItemStack stack = te.getStackInSlot(0);
        if (stack != null && stack.getItem() instanceof IEnergyContainerItem)
        {
            IEnergyContainerItem item = (IEnergyContainerItem) stack.getItem();
            if (item.getEnergyStored(stack) < item.getMaxEnergyStored(stack))
            {
                return 0;
            }
        }
        return 15;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
        return isProvidingStrongPower(world, x, y, z, side);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            ItemStack stack = player.getCurrentEquippedItem();
            TileCharger tile = (TileCharger) world.getTileEntity(x, y, z);
            if (stack != null)
            {
                ItemStack newStack = stack.copy();
                newStack.stackSize = 1;
                tile.setInventorySlotContents(0, newStack);
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
            else if (player.isSneaking())
            {
                player.inventory.addItemStackToInventory(tile.getStackInSlot(0));
                tile.setInventorySlotContents(0, null);
            }
        }
        return true;
    }
}
