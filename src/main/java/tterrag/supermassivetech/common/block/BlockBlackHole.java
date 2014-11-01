package tterrag.supermassivetech.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.tile.TileBlackHole;

public class BlockBlackHole extends BlockSMT implements ITileEntityProvider
{
    public BlockBlackHole()
    {
        super("blackHole", Material.cloth, soundTypeCloth, 1.0f, SuperMassiveTech.renderIDBlackHole, TileBlackHole.class);
        setResistance(100f);
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
