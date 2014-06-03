package tterrag.supermassivetech.block.waypoint;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.block.BlockSMT;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.GuiHandler;
import tterrag.supermassivetech.tile.TileWaypoint;

public class BlockWaypoint extends BlockSMT
{
    public BlockWaypoint()
    {
        super("waypoint", Material.iron, soundTypeMetal, 5f, SuperMassiveTech.renderIDWaypoint);
        setBlockBounds(.05f, 0f, .05f, .95f, 0.775f, .95f);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "waypoint");
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
        if (!world.isRemote && te != null && te instanceof TileWaypoint)
        {
            TileWaypoint wp = (TileWaypoint) te;
            wp.init((EntityPlayer) player);
        }
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            GuiHandler.openWaypointGui(world, x, y, z);
        }
        return true;
    }
}
