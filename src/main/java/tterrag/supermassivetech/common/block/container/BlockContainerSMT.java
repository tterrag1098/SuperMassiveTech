package tterrag.supermassivetech.common.block.container;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.common.block.BlockSMT;
import tterrag.supermassivetech.common.tile.abstracts.TileSMTInventory;
import tterrag.supermassivetech.common.util.Utils;

public abstract class BlockContainerSMT extends BlockSMT
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
    public void breakBlock(World world, int x, int y, int z, Block block, int meta)
    {
        if (!saveToItem())
        {
            TileSMTInventory te = (TileSMTInventory) world.getTileEntity(x, y, z);
            for (int i = 0; i < te.getSizeInventory(); i++)
            {
                Utils.spawnItemInWorldWithRandomMotion(world, te.getStackInSlot(i), x, y, z);
            }
        }

        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
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
