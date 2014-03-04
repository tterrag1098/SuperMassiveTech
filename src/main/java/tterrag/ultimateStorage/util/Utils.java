package tterrag.ultimateStorage.util;

import org.lwjgl.opengl.GL11;

import tterrag.ultimateStorage.tile.TileStorageBlock;

public class Utils
{
	/**
	 * Turns an int into a glColor4f function
	 * @author Buildcraft team
	 */
	public static void setGLColorFromInt(int color)
	{
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, 1.0F);
	}
	
	/**
	 * Formats a string and number for use in GUIs and tooltips
	 * @param prefix - The string to put before the formatted number
	 * @param amnt - The number to be formatted
	 * @param isFluid - If the number represents a fluid
	 * @param useDecimals - Whether or not to use decimals in the representation
	 * @return
	 */
	public static String formatString(String prefix, long amnt, boolean isFluid, boolean useDecimals)
	{
		if (amnt == TileStorageBlock.max)
		{
			prefix += "2^40 mB";
			return prefix;
		}

		switch (Long.toString(amnt).length())
		{
		case 7:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 3) : "") + (isFluid ? "MmB" : "M");
			return prefix;
		case 8:
			prefix += Long.toString(amnt).substring(0, 2) + (useDecimals ? "." + Long.toString(amnt).substring(2, 4) : "") + (isFluid ? "MmB" : "M");
			return prefix;
		case 9:
			prefix += Long.toString(amnt).substring(0, 3) + (useDecimals ? "." + Long.toString(amnt).substring(3, 5) : "") + (isFluid ? "MmB" : "M");
			return prefix;
		case 10:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 3) : "") + (isFluid ? "GmB" : "B");
			return prefix;
		case 11:
			prefix += Long.toString(amnt).substring(0, 2) + (useDecimals ? "." + Long.toString(amnt).substring(2, 4) : "") + (isFluid ? "GmB" : "B");
			return prefix;
		case 12:
			prefix += Long.toString(amnt).substring(0, 3) + (useDecimals ? "." + Long.toString(amnt).substring(3, 5) : "") + (isFluid ? "GmB" : "B");
			return prefix;
		case 13:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 5) : "") + (isFluid ? "TmB" : "T");
			return prefix;
		default:
			prefix += "" + amnt + (isFluid ? "mb" : "");
			return prefix;
		}
	}
}
