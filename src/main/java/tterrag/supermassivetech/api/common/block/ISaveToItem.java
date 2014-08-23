package tterrag.supermassivetech.api.common.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public interface ISaveToItem
{
    /**
     * Gets an itemstack with the relevant NBT data that represents all the data of the now destroyed TE
     */
    public ItemStack getNBTItem(World world, int x, int y, int z);

    /**
     * Processes turning NBT into TE data on block place
     * 
     * @param tag - tag of the itemstack
     * @param te - TE to set data in
     */
    public void processBlockPlace(NBTTagCompound tag, TileEntity te);

    /**
     * Drops the passed stack into the world
     * 
     * @param world
     * @param item - The item returned from getNBTItem
     * @param x
     * @param y
     * @param z
     */
    public void dropItem(World world, ItemStack item, int x, int y, int z);
}
