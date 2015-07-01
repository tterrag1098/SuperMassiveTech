package tterrag.supermassivetech.common.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.block.ISaveToItem;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;

import com.enderio.core.common.util.ItemUtil;

public class BlockStarHarvester extends BlockContainerSMT implements ISaveToItem
{
    public BlockStarHarvester()
    {
        super("starHarvester", Material.iron, soundTypeMetal, 5.0f, TileStarHarvester.class, SuperMassiveTech.renderIDStarHarvester);
        setResistance(100f);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        boolean returnVal = false;
        if (te instanceof TileStarHarvester)
        {
            returnVal = ((TileStarHarvester) te).handleRightClick(player, ForgeDirection.getOrientation(side));
        }
        return returnVal;
    }

    @Override
    public ItemStack getNBTItem(World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(this);
        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);

        if (te == null)
            return stack;

        int energy = te.getEnergyStored();
        if (energy > 0 || te.getBlockMetadata() > 5)
        {
            stack.stackTagCompound = new NBTTagCompound();

            if (energy > 0)
            {
                stack.stackTagCompound.setInteger("energy", te.getEnergyStored());
            }

            if (te.getBlockMetadata() > 5)
            {
                stack.stackTagCompound.setInteger("storedMetaData", te.getBlockMetadata());
            }
        }

        return stack;
    }

    @Override
    public void processBlockPlace(NBTTagCompound tag, TileEntity te)
    {
        if (te instanceof TileStarHarvester)
        {
            TileStarHarvester harvester = (TileStarHarvester) te;

            harvester.setEnergyStored(tag.getInteger("energy"));

            if (tag.getInteger("storedMetaData") > 5)
            {
                te.getWorldObj().setBlockMetadataWithNotify(te.xCoord, te.yCoord, te.zCoord, te.getBlockMetadata() + 6, 3);
                te.getWorldObj().markBlockForUpdate(te.xCoord, te.yCoord, te.zCoord);
            }
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
        super.onBlockHarvested(world, x, y, z, meta, player);
        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);
        if (te != null && te.isGravityWell() && !player.capabilities.isCreativeMode)
        {
            ItemUtil.spawnItemInWorldWithRandomMotion(world, te.getStackInSlot(0), x, y, z);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);
        return te == null || te.getStackInSlot(0) == null ? 0 : 15;
    }
}
