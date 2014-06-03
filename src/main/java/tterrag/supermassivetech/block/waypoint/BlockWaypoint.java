package tterrag.supermassivetech.block.waypoint;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.block.BlockSMT;
import tterrag.supermassivetech.block.ISaveToItem;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileWaypoint;
import tterrag.supermassivetech.util.GuiHelper;

public class BlockWaypoint extends BlockSMT implements ISaveToItem, ITileEntityProvider
{
    public BlockWaypoint()
    {
        super("waypoint", Material.iron, soundTypeMetal, 5f, SuperMassiveTech.renderIDWaypoint, TileWaypoint.class);
        setBlockBounds(.05f, 0f, .05f, .95f, 0.775f, .95f);
    }
    
    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(Reference.MOD_TEXTUREPATH + ":" + "waypoint");
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
        super.onBlockPlacedBy(world, x, y, z, player, stack);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            GuiHelper.openWaypointGui(world, x, y, z);
        }
        return true;
    }

    @Override
    public ItemStack getNBTItem(World world, int x, int y, int z)
    {
        ItemStack stack = new ItemStack(this);
        
        stack.stackTagCompound = new NBTTagCompound();
        
        NBTTagCompound waypointTag = new NBTTagCompound();
        ((TileWaypoint) world.getTileEntity(x, y, z)).waypoint.writeToNBT(waypointTag);
        
        stack.stackTagCompound.setTag("waypoint", waypointTag);
        
        return stack;
    }

    @Override
    public void processBlockPlace(NBTTagCompound tag, TileEntity te)
    {
        ((TileWaypoint)te).waypoint = new Waypoint().readFromNBT(tag.getCompoundTag("waypoint"));
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileWaypoint();
    }
}
