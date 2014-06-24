package tterrag.supermassivetech.block.container;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.block.ISaveToItem;
import tterrag.supermassivetech.tile.TileStarHarvester;
import tterrag.supermassivetech.util.Utils;

public class BlockStarHarvester extends BlockContainerSMT implements ISaveToItem
{
    public BlockStarHarvester()
    {
        super("starHarvester", Material.iron, soundTypeMetal, 5.0f, TileStarHarvester.class, SuperMassiveTech.renderIDStarHarvester);
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

        stack.stackTagCompound = new NBTTagCompound();

        stack.stackTagCompound.setInteger("energy", te.getEnergyStored(ForgeDirection.UNKNOWN));

        return stack;
    }

    @Override
    public void processBlockPlace(NBTTagCompound tag, TileEntity te)
    {
        if (te instanceof TileStarHarvester)
        {
            TileStarHarvester harvester = (TileStarHarvester) te;

            harvester.setEnergyStored(tag.getInteger("energy"));
        }
    }

    @Override
    public void dropItem(World world, ItemStack item, int x, int y, int z)
    {
        super.dropItem(world, item, x, y, z);
        if (world.getBlockMetadata(x, y, z) > 5)
            Utils.spawnItemInWorldWithRandomMotion(world, new ItemStack(SuperMassiveTech.itemRegistry.starContainer), x, y, z);
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player)
    {
        super.onBlockHarvested(world, x, y, z, meta, player);
        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);
        if (te != null && te.isGravityWell() && !player.capabilities.isCreativeMode)
        {
            Utils.spawnItemInWorldWithRandomMotion(world, te.getStackInSlot(0), x, y, z);
        }
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);
        return te == null || te.getStackInSlot(0) == null ? 0 : 15;
    }

    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        super.getWailaInfo(tooltip, x, y, z, world);

        TileStarHarvester te = (TileStarHarvester) world.getTileEntity(x, y, z);

        if (te.getBlockMetadata() < 5)
        {
            tooltip.add("" + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + Utils.localize("tooltip.noContainerInPlace", true));
        }
        else if (!te.isGravityWell())
        {
            tooltip.add("" + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + Utils.localize("tooltip.noStarInPlace", true));
        }

        int energyStored = te.getEnergyStored(ForgeDirection.UNKNOWN), maxEnergyStored = te.getMaxEnergyStored(ForgeDirection.UNKNOWN);
        int output = te.getCurrentOutputMax(), maxOutput = te.maxPerTick;

        tooltip.add(EnumChatFormatting.WHITE + Utils.localize("tooltip.bufferStorage", true) + ": "
                + Utils.getColorForPowerLeft(energyStored, maxEnergyStored) + energyStored + " RF");
        tooltip.add(EnumChatFormatting.WHITE + Utils.localize("tooltip.currentOutputMax", true) + ": "
                + Utils.getColorForPowerLeft(output, maxOutput) + output + " RF");

    }
}
