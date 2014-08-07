package tterrag.supermassivetech.common.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.tile.TileBlackHole;

public class BlockBlackHole extends BlockSMT implements ITileEntityProvider
{
    public BlockBlackHole()
    {
        super("blackHole", Material.cloth, soundTypeCloth, -1.0f, SuperMassiveTech.renderIDBlackHole, TileBlackHole.class);
        setBlockBounds(0.4f, 0.4f, 0.4f, 0.6f, 0.6f, 0.6f);
    }

    @Override
    public boolean hasPlacementRotation()
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return null;
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
