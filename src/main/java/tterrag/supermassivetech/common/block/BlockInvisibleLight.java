package tterrag.supermassivetech.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockInvisibleLight extends BlockSMT
{
    public BlockInvisibleLight()
    {
        super("invisibleLight", Material.air, soundTypeCloth, 0.0f, TileInvisibleLight.class);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean canCollideCheck(int par1, boolean par2)
    {
        return this.isCollidable();
    }

    @Override
    public boolean isCollidable()
    {
        return false;
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public boolean canProvidePower()
    {
        return true;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean isAir(IBlockAccess world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public int getLightValue()
    {
        return 15;
    }

    public static class TileInvisibleLight extends TileEntity
    {
        @SuppressWarnings("unchecked")
        @Override
        public void updateEntity()
        {
            if (worldObj.getTotalWorldTime() % 15 == 0)
            {
                List<EntityItem> entitiesInside = worldObj.getEntitiesWithinAABB(EntityItem.class,
                        AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
                if (entitiesInside.isEmpty())
                {
                    worldObj.setBlockToAir(xCoord, yCoord, zCoord);
                }
            }
        }
    }
}
