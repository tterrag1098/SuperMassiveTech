package tterrag.supermassivetech.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.ItemStar.StarType;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;

public class Utils
{
	private static Constants c = Constants.instance();
	
	public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), "Ultimate Storage")
	{
		@Override
		public Item getTabIconItem()
		{
			return SuperMassiveTech.blockRegistry.blackHoleStorage.getItem(null, 0, 0, 0);
		}
	};
	
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
		if (amnt == TileBlackHoleStorage.max)
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

	public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, int xCoord, int yCoord, int zCoord)
	{
		double dist = Math.sqrt(Math.pow(xCoord + 0.5 - entity.posX, 2) + Math.pow(zCoord + 0.5 - entity.posZ, 2) + Math.pow(yCoord + 0.5 - entity.posY, 2));

		if (dist > range || dist == 0)
			return;

		double xDisplacment = entity.posX - (xCoord + 0.5);
		double yDisplacment = entity.posY - (yCoord + 0.5);
		double zDisplacment = entity.posZ - (zCoord + 0.5);

		// http://en.wikipedia.org/wiki/Spherical_coordinate_system#Coordinate_system_conversions

		double theta = Math.acos(zDisplacment / dist);
		double phi = Math.atan2(yDisplacment, xDisplacment);

		// More strength for everything but players, lower dist is bigger effect
		if (!(entity instanceof EntityPlayer))
			dist *= 0.5;
		else
			dist *= 2;

		double vecX = -gravStrength * Math.sin(theta) * Math.cos(phi) / dist;
		double vecY = -gravStrength * Math.sin(theta) * Math.sin(phi) / dist;
		double vecZ = -gravStrength * Math.cos(theta) / dist;

		if (Math.abs(vecX) > maxGravXZ)
			vecX *= maxGravXZ / Math.abs(vecX);
		if (Math.abs(vecY) > maxGravY)
			vecY *= maxGravY / Math.abs(vecY);
		if (Math.abs(vecZ) > maxGravXZ)
			vecZ *= maxGravXZ / Math.abs(vecZ);

		if (Math.abs(vecX) < minGrav)
			vecX = 0;
		if (Math.abs(vecY) < minGrav)
			vecY = 0;
		if (Math.abs(vecZ) < minGrav)
			vecZ = 0;

		entity.setVelocity(entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);
	}

	public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, TileEntity te)
	{
		applyGravity(gravStrength, maxGravXZ, maxGravY, minGrav, range, entity, te.xCoord, te.yCoord, te.zCoord);
	}

	public static void applyGravity(Entity entity, TileEntity te)
	{
		applyGravity(c.STRENGTH, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV, c.RANGE, entity, te);
	}

	public static void applyGravity(Entity entity, int x, int y, int z)
	{
		applyGravity(c.STRENGTH, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV, c.RANGE, entity, x, y, z);
	}

	public static StarType getType(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemStar && stack.stackTagCompound != null)
			return StarType.valueOf(stack.stackTagCompound.getString("type"));
		else return null;
	}

	public static ItemStack setType(ItemStack stack, StarType type)
	{
		if (stack != null && stack.getItem() instanceof ItemStar)
		{
			if (stack.stackTagCompound == null)
				stack.stackTagCompound = new NBTTagCompound();

			stack.stackTagCompound.setString("type", type.name());
		}
		else if (stack != null)
		{
			SuperMassiveTech.logger.warning(String.format("A mod tried to set the type of an item that was not a star, item was %s", stack.getUnlocalizedName()));
		}
		else
		{
			SuperMassiveTech.logger.severe("A mod tried to set the type of a null itemstack");
		}
		
		return stack;
	}
}
