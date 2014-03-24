package tterrag.supermassivetech.registry;

import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.registry.Stars.StarTier;

public interface IStar
{
	/**
	 * Returns the unique ID of this star, use the static method in Stars.java!
	 */
	public int getID();

	/**
	 * The color of the star icon, a hex value (0xRRGGBB)
	 */
	public int getColor();

	/**
	 * The amount of power currently stored in this star
	 */
	public int getPowerStored(ItemStack stack);

	/**
	 * The amount of power that was initially in this star
	 */
	public int getPowerStoredMax();

	/**
	 * The max power this star can provide in the harvester
	 */
	public int getPowerPerTick();

	/**
	 * The amount of time the fuse lasts on this star
	 */
	public int getFuse();

	/**
	 * <code>toString()</code> here returns the proper name of the star, to be
	 * used in tooltips
	 */
	@Override
	public String toString();

	/**
	 * The tier of this star, a {@link StarTier} type
	 */
	public StarTier getTier();
}
