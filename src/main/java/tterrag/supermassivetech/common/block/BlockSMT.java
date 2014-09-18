package tterrag.supermassivetech.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.block.ISaveToItem;
import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.common.util.Utils;

public abstract class BlockSMT extends Block implements IWailaAdditionalInfo
{
    private int renderID;
    protected String unlocName;
    protected Class<? extends TileEntity> teClass;

    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness, int renderID, Class<? extends TileEntity> teClass)
    {
        super(mat);
        setStepSound(type);
        setHardness(hardness);
        setBlockName(ModProps.LOCALIZING + "." + unlocName);
        setCreativeTab(SuperMassiveTech.tabSMT);
        this.renderID = renderID;
        this.unlocName = unlocName;
        this.teClass = teClass;
    }

    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness, int renderID)
    {
        this(unlocName, mat, type, hardness, renderID, null);
    }
    
    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> teClass)
    {
        this(unlocName, mat, type, hardness, 0, teClass);
    }

    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness)
    {
        this(unlocName, mat, type, hardness, 0, null);
    }

    /**
     * Default: true
     */
    public boolean hasPlacementRotation()
    {
        return true;
    }

    /**
     * Default: true
     */
    public boolean hasCustomModel()
    {
        return true;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
    {
        int opp = Facing.oppositeSide[side];

        return hasPlacementRotation() ? opp : 0;
    }

    @Override
    public int getRenderType()
    {
        return renderID;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return !hasCustomModel();
    }

    @Override
    public boolean isOpaqueCube()
    {
        return !hasCustomModel();
    }

    @Override
    public boolean isNormalCube()
    {
        return !hasCustomModel();
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return !hasCustomModel();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        if (this.saveToItem())
        {
            TileEntity te = world.getTileEntity(x, y, z);

            if (te != null && te.getClass() == this.teClass && stack.stackTagCompound != null)
            {
                ((ISaveToItem) this).processBlockPlace(stack.stackTagCompound, te);
            }
        }
        else
        {
            super.onBlockPlacedBy(world, x, y, z, player, stack);
        }
    }

    @Override
    public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_, EntityPlayer player)
    {
        if (this.saveToItem() && canHarvestBlock(player, world.getBlockMetadata(x, y, z)) && !player.capabilities.isCreativeMode && !world.isRemote)
        {
            ((ISaveToItem) this).dropItem(world, ((ISaveToItem) this).getNBTItem(world, x, y, z), x, y, z);
        }
        else
        {
            super.onBlockHarvested(world, x, y, z, p_149681_5_, player);
        }
    }

    @Override
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return this.saveToItem() ? null : super.getItemDropped(p_149650_1_, p_149650_2_, p_149650_3_);
    }
    
    // overrides ITEP in subclasses
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        try
        {
            return teClass.newInstance();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Whether this block keeps its inventory in item form. If this ever returns true the block MUST implement IKeepInventoryAsItem
     */
    public boolean saveToItem()
    {
        return this instanceof ISaveToItem;
    }

    /**
     * Base implementation of {@link ISaveToItem} method
     */
    public void dropItem(World world, ItemStack item, int x, int y, int z)
    {
        Utils.spawnItemInWorldWithRandomMotion(world, item, x, y, z);
    }

    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IWailaAdditionalInfo)
        {
            ((IWailaAdditionalInfo) te).getWailaInfo(tooltip, x, y, z, world);
        }
    }
}
