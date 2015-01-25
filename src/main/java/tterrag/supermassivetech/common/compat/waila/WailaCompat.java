package tterrag.supermassivetech.common.compat.waila;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.block.BlockSMT;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.util.Utils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WailaCompat implements IWailaDataProvider
{
    public static final WailaCompat INSTANCE = new WailaCompat();

    public static void load(IWailaRegistrar registrar)
    {
        registrar.registerHeadProvider(INSTANCE, BlockSMT.class);
        registrar.registerBodyProvider(INSTANCE, BlockSMT.class);
        registrar.registerTailProvider(INSTANCE, BlockSMT.class);
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        TileEntity te = accessor.getTileEntity();
        Block block = accessor.getBlock();
        Item item = Item.getItemFromBlock(block);

        if (te == null)
        {
            return currenttip;
        }

        int key1 = Keyboard.getKeyIndex(ConfigHandler.wailaKey1);
        int key2 = Keyboard.getKeyIndex(ConfigHandler.wailaKey2);

        if (block instanceof IAdvancedTooltip)
        {
            Utils.formAdvancedTooltip(currenttip, accessor.getStack(), (IAdvancedTooltip) block, key1, key2);
        }
        else if (item instanceof IAdvancedTooltip)
        {
            Utils.formAdvancedTooltip(currenttip, accessor.getStack(), (IAdvancedTooltip) item, key1, key2);
        }

        if (block instanceof IWailaAdditionalInfo)
        {
            MovingObjectPosition pos = accessor.getPosition();
            ((IWailaAdditionalInfo) block).getWailaInfo(currenttip, pos.blockX, pos.blockY, pos.blockZ, accessor.getWorld());
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z)
    {
        return tag;
    }
}