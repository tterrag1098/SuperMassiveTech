package tterrag.supermassivetech.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.tile.IBlackHole;
import tterrag.supermassivetech.common.tile.TileBlackHole;

public class BlockBlackHole extends BlockSMT implements ITileEntityProvider
{
    public BlockBlackHole()
    {
        super("blackHole", Material.cloth, soundTypeCloth, 1.0f, SuperMassiveTech.renderIDBlackHole, TileBlackHole.class);
        setBlockBounds(0.4f, 0.4f, 0.4f, 0.6f, 0.6f, 0.6f);
    }

    @Override
    public boolean hasPlacementRotation()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return null;
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
        return super.getSelectedBoundingBoxFromPool(world, x, y, z);//getBoundsFromBlackHole((IBlackHole) world.getTileEntity(x, y, z), x, y, z);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
       AxisAlignedBB bb = getBoundsFromBlackHole((IBlackHole) world.getTileEntity(x, y, z), x, y, z);
       this.setBlockBounds((float) bb.minX - x, (float) bb.minY - y, (float) bb.minZ - z, (float) bb.maxX - x, (float) bb.maxY - y, (float) bb.maxZ - z);
    }
    
    private AxisAlignedBB getBoundsFromBlackHole(IBlackHole bh, int x, int y, int z)
    {
        float xBase = x + 0.5f;
        float yBase = y + 0.5f;
        float zBase = z + 0.5f;
        float scale = bh.getSize() / 2f;
        
        System.out.println(scale);
        
        return AxisAlignedBB.getBoundingBox(xBase - scale, yBase - scale, zBase - scale, xBase + scale, yBase + scale, zBase + scale);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
    {
        if (!world.isRemote)
        {
            TileBlackHole tile = (TileBlackHole) world.getTileEntity(x, y, z);
            tile.incrementEnergy(entity);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        try
        {
            return teClass.newInstance();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
