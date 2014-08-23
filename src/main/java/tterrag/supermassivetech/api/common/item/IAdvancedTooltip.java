package tterrag.supermassivetech.api.common.item;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Automatic formatting of advanced tooltips.
 * 
 * Can be implemented on items or blocks (the Block will be found automatically from the ItemBlock)
 */
public interface IAdvancedTooltip
{
    /**
     * Lines that show when shift is held. <br>
     * <p>
     * Color formatting is automatic, always appended to the beginning of the line, so it can be overriden with custom colorations.<br>
     * <p>
     * 
     * @param stack - {@link ItemStack} the tooltip is being applied to
     */
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack);

    /**
     * Lines that are shown constantly, must be manually colored
     * 
     * @param stack - {@link ItemStack} the tooltip is being applied to
     */
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack);
}
