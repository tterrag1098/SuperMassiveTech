package tterrag.supermassivetech.common.block;

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
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.block.ISaveToItem;
import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.client.util.GuiHelper;
import tterrag.supermassivetech.common.tile.TileWaypoint;
import tterrag.supermassivetech.common.util.Utils;
import tterrag.supermassivetech.common.util.Waypoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWaypoint extends BlockSMT implements ISaveToItem, ITileEntityProvider, IAdvancedTooltip, IWailaAdditionalInfo
{
    public BlockWaypoint()
    {
        super("waypoint", Material.iron, soundTypeMetal, 5f, SuperMassiveTech.renderIDWaypoint, TileWaypoint.class);
        setBlockBounds(.05f, 0f, .05f, .95f, 0.775f, .95f);
    }

    @Override
    public void registerBlockIcons(IIconRegister register)
    {
        this.blockIcon = register.registerIcon(ModProps.MOD_TEXTUREPATH + ":" + "waypoint");
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        super.onBlockPlacedBy(world, x, y, z, player, stack);

        TileEntity te = world.getTileEntity(x, y, z);
        if (!world.isRemote && te != null && te instanceof TileWaypoint)
        {
            TileWaypoint wp = (TileWaypoint) te;

            if (wp.waypoint.isNull())
                wp.init((EntityPlayer) player);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote && (((TileWaypoint) world.getTileEntity(x, y, z)).waypoint.players.contains(player.getCommandSenderName()) || player.capabilities.isCreativeMode))
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
        Waypoint w = new Waypoint().readFromNBT(tag.getCompoundTag("waypoint"));
        w.x = te.xCoord;
        w.y = te.yCoord;
        w.z = te.zCoord;

        ((TileWaypoint) te).waypoint = w;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound() == null ? null : stack.getTagCompound().getCompoundTag("waypoint");

        if (tag != null)
        {
            String name = tag.getString("waypointname");
            int[] color = tag.getIntArray("waypointcolor");
            String ret = String.format("%s: %s~%s: %d~%s: %d~%s: %d", EnumChatFormatting.YELLOW + Utils.lang.localize("tooltip.name"), EnumChatFormatting.WHITE + name,
                    EnumChatFormatting.RED + Utils.lang.localize("tooltip.red"), color[0], EnumChatFormatting.GREEN + Utils.lang.localize("tooltip.green"), color[1],
                    EnumChatFormatting.BLUE + Utils.lang.localize("tooltip.blue"), color[2]);

            return ret;
        }
        else
            return null;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z)
    {
        return 13;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        return true;
    }
}
