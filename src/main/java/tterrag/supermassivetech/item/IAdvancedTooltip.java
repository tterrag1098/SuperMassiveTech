package tterrag.supermassivetech.item;

import net.minecraft.item.ItemStack;

public interface IAdvancedTooltip {
	
	/**
	 * Lines that show when shift is held. Color formatting is automatic if <code>colorHiddenLines()</code> returns true
	 */
	public String[] getHiddenLines(ItemStack stack);
	
	/**
	 * Whether to automatically format hidden lines
	 */
	public boolean colorHiddenLines(ItemStack stack);
	
	/**
	 * Lines that are shown constantly, must be manually formatted
	 */
	public String[] getStaticLines(ItemStack stack);
}
