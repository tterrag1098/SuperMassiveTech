package tterrag.supermassivetech.item.star;

import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.registry.Stars.StarType;

/**
 * Interface for implementations of stars. Stars are very abstract, but must register their types in the starRegistry object. Registering a star type means that it can be created from star hearts, and used the star harvester. Star types will automatically be displayed in the tooltip of the item using the <code>toString</code> method of the StarType
 * @author Garrett Spicer-Davis
 *
 */
public interface IStar {
	
	/**
	 * The max power per tick that this star can provide
	 * @param stack - stack being harvested
	 * @return RF amount
	 */
	public int getMaxPower(ItemStack stack);
	
	/**
	 * Attempts to use power from the star
	 * @param stack - stack being harvested
	 * @param power - amount of RF attempted to be used
	 * @return amount of power actually used
	 */
	public int usePower(ItemStack stack, int power);

	/**
	 * Whether this star is going critical, that is, about to turn into a black hole
	 * @param stack - stack being harvested
	 * @return whether the star is in critical mode
	 */
	public boolean isCritical(ItemStack stack);
	
	/**
	 * Only called if the star is going critical, how much time is remaining before forming a black hole
	 * @param stack - stack being harvested
	 * @return time left
	 */
	public double getCriticalTimeleft(ItemStack stack);
	
	public StarType getType(ItemStack stack);
}
