package tterrag.supermassivetech.common.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.tile.energy.TileCharger;
import tterrag.supermassivetech.common.util.Utils;

public class BlockCharger extends BlockContainerSMT implements IAdvancedTooltip
{
    public BlockCharger()
    {
        super("charger", Material.iron, soundTypeMetal, 5.0f, TileCharger.class, SuperMassiveTech.renderIDCharger);
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(ModProps.MOD_TEXTUREPATH + ":charger");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return true;
        }

        ItemStack stack = player.getCurrentEquippedItem();
        TileCharger tile = (TileCharger) world.getTileEntity(x, y, z);
        if (stack != null)
        {
            if (stack.getItem() != Items.redstone && tile.getStackInSlot(0) == null)
            {
                ItemStack newStack = stack.copy();
                newStack.stackSize = 1;
                tile.setInventorySlotContents(0, newStack);
                player.inventory.decrStackSize(player.inventory.currentItem, 1);
            }
        }
        else if (player.isSneaking() && tile.getStackInSlot(0) != null && player instanceof EntityPlayerMP /* prevent crashes with poorly implemented fake players */)
        {
            player.inventory.addItemStackToInventory(tile.getStackInSlot(0));
            ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
            tile.setInventorySlotContents(0, null);
        }
        return true;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileCharger)
        {
            return ((TileCharger) te).isCharging() ? 10 : 0;
        }
        return 0;
    }

    @Override
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.lang.localize("tooltip.charger");
    }

    @Override
    public String getStaticLines(ItemStack stack)
    {
        return null;
    }
}
