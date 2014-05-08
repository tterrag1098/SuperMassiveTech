package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.util.Utils;

public class BlockBlackHoleHopper extends BlockContainerSMT implements IKeepInventoryAsItem
{
    public BlockBlackHoleHopper()
    {
        super("blackHoleHopper", Material.iron, soundTypeMetal, 30.0f, TileBlackHoleHopper.class, SuperMassiveTech.renderIDHopper);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileBlackHoleHopper te = (TileBlackHoleHopper) world.getTileEntity(x, y, z);

        if (te == null || world.isRemote)
            return true;

        ItemStack current = player.getCurrentEquippedItem();

        if (current == null)
        {
            if (player.isSneaking())
                te.clearConfig(player);
            else
                player.addChatMessage(new ChatComponentText(Utils.localize("tooltip.currentConfig", true) + ": " + te.getConfig()));
        }
        else
        {
            te.setConfig(current, player);
        }

        return true;
    }

    @Override
    public ItemStack getNBTItem(World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(this);
        TileBlackHoleHopper te = (TileBlackHoleHopper) world.getTileEntity(x, y, z);

        if (te == null)
            return stack;

        stack.stackTagCompound = new NBTTagCompound();

        if (te.getStackInSlot(0) != null)
        {
            NBTTagCompound tag0 = new NBTTagCompound();
            te.getStackInSlot(0).writeToNBT(tag0);
            stack.stackTagCompound.setTag("inventory0", tag0);
        }
        if (te.getStackInSlot(1) != null)
        {
            NBTTagCompound tag1 = new NBTTagCompound();
            te.getStackInSlot(1).writeToNBT(tag1);
            stack.stackTagCompound.setTag("inventory1", tag1);
        }

        return stack;
    }

    @Override
    public void processBlockPlace(NBTTagCompound tag, TileEntity te)
    {
        if (te instanceof TileBlackHoleHopper)
        {
            TileBlackHoleHopper hopper = (TileBlackHoleHopper) te;
            hopper.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("inventory0")));
            hopper.setInventorySlotContents(1, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("inventory1")));
        }
    }
}
