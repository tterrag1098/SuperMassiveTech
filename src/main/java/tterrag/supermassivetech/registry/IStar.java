package tterrag.supermassivetech.registry;

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
	 * The max power this star can provide in the harvester
	 */
	public int getMaxPower();
	
	/**
	 * The amount of time the fuse lasts on this star
	 */
	public int getFuse();
	
	/**
	 * <code>toString()</code> here returns the proper name of the star, to be used in tooltips
	 */
	public String toString();
}
