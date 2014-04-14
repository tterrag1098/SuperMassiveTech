package tterrag.supermassivetech.item;

import net.minecraft.item.ItemStack;

public interface IAdvancedTooltip
{

    /**
     * Lines that show when shift is held. <br>
     * <br>
     * Color formatting is automatic, always appended to the beginning of the
     * line, so it can be overriden with custom colorations.
     * 
     * @param stack - {@link ItemStack} the tooltip is being applied to
     */
    public String[] getHiddenLines(ItemStack stack);

    /**
     * Lines that are shown constantly, must be manually colored
     * 
     * @param stack - {@link ItemStack} the tooltip is being applied to
     */
    public String[] getStaticLines(ItemStack stack);
}
