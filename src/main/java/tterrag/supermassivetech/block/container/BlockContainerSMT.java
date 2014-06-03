package tterrag.supermassivetech.block.container;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.block.BlockSMT;
import tterrag.supermassivetech.util.Utils;

public abstract class BlockContainerSMT extends BlockSMT implements ITileEntityProvider
{
    protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te)
    {
        this(unlocName, mat, type, hardness, te, 0);
    }

    protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te, int renderID)
    {
        super(unlocName, mat, type, hardness, renderID, te);
        String toolLevel = Utils.getToolClassFromMaterial(mat);

        if (!toolLevel.equals("none"))
            setHarvestLevel("pickaxe", Utils.getToolLevelFromMaterial(mat));

        setBlockName(unlocName);
        this.isBlockContainer = true;
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon("supermassivetech:" + unlocName.substring(unlocName.indexOf(".") + 1, unlocName.length()));
    }
    
    @Override
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

    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        super.breakBlock(world, x, y, z, block, meta);
        world.removeTileEntity(x, y, z);
    }
    
    public boolean onBlockEventReceived(World world, int x, int y, int z, int side, int meta)
    {
        super.onBlockEventReceived(world, x, y, z, side, meta);
        TileEntity tileentity = world.getTileEntity(x, y, z);
        return tileentity != null ? tileentity.receiveClientEvent(side, meta) : false;
    }
    
    @Override
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
        return true;
    }
}
