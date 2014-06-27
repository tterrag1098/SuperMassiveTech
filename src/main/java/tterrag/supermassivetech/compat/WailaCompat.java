package tterrag.supermassivetech.compat;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.block.BlockSMT;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.util.Utils;

public class WailaCompat implements IWailaDataProvider
{
    public static final WailaCompat INSTANCE = new WailaCompat();

    public static void load()
    {
        ModuleRegistrar.instance().registerHeadProvider(INSTANCE, BlockSMT.class);
        ModuleRegistrar.instance().registerBodyProvider(INSTANCE, BlockSMT.class);
        ModuleRegistrar.instance().registerTailProvider(INSTANCE, BlockSMT.class);
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

        if (block instanceof IAdvancedTooltip)
        {
            Utils.formAdvancedTooltip(currenttip, accessor.getStack(), (IAdvancedTooltip) block, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL);
        }
        else if (item instanceof IAdvancedTooltip)
        {
            Utils.formAdvancedTooltip(currenttip, accessor.getStack(), (IAdvancedTooltip) item, Keyboard.KEY_LCONTROL, Keyboard.KEY_RCONTROL);
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
}
