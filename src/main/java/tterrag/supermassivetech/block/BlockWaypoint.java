package tterrag.supermassivetech.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileWaypoint;
import tterrag.supermassivetech.util.GuiHelper;
import tterrag.supermassivetech.util.Utils;
import tterrag.supermassivetech.util.Waypoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWaypoint extends BlockSMT implements ISaveToItem, ITileEntityProvider, IAdvancedTooltip
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
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound() == null ? null : stack.getTagCompound().getCompoundTag("waypoint");
        
        if (tag != null)
        {
            String name = tag.getString("waypointname");
            int[] color = tag.getIntArray("waypointcolor");
            String ret = String.format("%s: %s~%s: %d~%s: %d~%s: %d",
                    EnumChatFormatting.YELLOW + Utils.localize("tooltip.name", true), EnumChatFormatting.WHITE + name,
                    EnumChatFormatting.RED + Utils.localize("tooltip.red", true), color[0],
                    EnumChatFormatting.GREEN + Utils.localize("tooltip.green", true), color[1],
                    EnumChatFormatting.BLUE + Utils.localize("tooltip.blue", true), color[2]);
            
            return ret;
        }
        else return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return null;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileWaypoint();
    }
    
    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return 13;
    }
}
