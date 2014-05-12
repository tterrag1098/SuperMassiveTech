package tterrag.supermassivetech.block.waypoint;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.block.BlockSMT;
import tterrag.supermassivetech.tile.TileWaypoint;

public class BlockWaypoint extends BlockSMT
{
    public BlockWaypoint()
    {
        super("waypoint", Material.iron, soundTypeMetal, 5f);
    }
    
    @Override
    public boolean hasTileEntity(int meta)
    {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileWaypoint();
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof TileWaypoint)
        {
            TileWaypoint wp = (TileWaypoint) te;
            wp.init((EntityPlayer) player);
        }
    }
}
