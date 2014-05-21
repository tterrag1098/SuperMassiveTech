package tterrag.supermassivetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;

public class BlockSMT extends Block
{
    private int renderID;
    protected String unlocName;

    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness, int renderID)
    {
        super(mat);
        setStepSound(type);
        setHardness(hardness);
        setBlockName(unlocName);
        setCreativeTab(SuperMassiveTech.tabSMT);
        this.renderID = renderID;
        this.unlocName = unlocName;
    }
    
    protected BlockSMT(String unlocName, Material mat, SoundType type, float hardness)
    {
        this(unlocName, mat, type, hardness, 0);
    }
    
    public boolean hasPlacementRotation()
    {
        return true;
    }

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
}
