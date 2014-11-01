package tterrag.supermassivetech.api.common.compat;

import java.util.List;

import net.minecraft.world.World;
import tterrag.supermassivetech.common.block.BlockSMT;

/**
 * Used to add WAILA compatability without using and abusing <code>{@literal @}Optional</code>
 * <p>
 * Must be implemented on a block, {@link BlockSMT} will pass it to the TileEntity by default
 */
public interface IWailaAdditionalInfo
{
    /**
     * Add to the main WAILA tooltip
     * 
     * @param tooltip - Current list of Strings in the tooltip
     * @param x
     * @param y
     * @param z
     * @param world
     */
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world);
}
