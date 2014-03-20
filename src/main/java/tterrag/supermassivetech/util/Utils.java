package tterrag.supermassivetech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.registry.Stars.StarType;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Utils
{
	private static Constants c = Constants.instance();
	private static Stars stars = Stars.instance;
	
	public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), "superMassiveTech")
	{
		@Override
		public Item getTabIconItem()
		{
			return SuperMassiveTech.blockRegistry.blackHoleStorage.getItem(null, 0, 0, 0);
		}
	};

	/**
	 * Turns an int into a glColor4f function
	 * 
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
	 * 
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

	/**
	 * Applies gravity to an entity with the passed configurations
	 * 
	 * @param gravStrength - Strength of the gravity, usually a number < 3
	 * @param maxGravXZ - Max gravity that can be applied in the X and Z
	 *            directions
	 * @param maxGravY - Max gravity that can be applied in the Y direction
	 * @param minGrav - Minimum gravity that can be applied (prevents "wobbling"
	 *            if such a thing ever exists)
	 * @param range - The range of the gravitational effects
	 * @param entity - Entity to effect
	 * @param xCoord - X coord of the center of gravity
	 * @param yCoord - Y coord of the center of gravity
	 * @param zCoord - Z coord of the center of gravity
	 */
	public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, int xCoord, int yCoord, int zCoord, boolean showParticles)
	{
		// distance forumla
		double dist = Math.sqrt(Math.pow(xCoord + 0.5 - entity.posX, 2) + Math.pow(zCoord + 0.5 - entity.posZ, 2) + Math.pow(yCoord + 0.5 - entity.posY, 2));

		if (dist > range || dist == 0)
			return;

		double xDisplacment = entity.posX - (xCoord + 0.5);
		double yDisplacment = entity.posY - (yCoord + 0.5);
		double zDisplacment = entity.posZ - (zCoord + 0.5);

		// http://en.wikipedia.org/wiki/Spherical_coordinate_system#Coordinate_system_conversions

		double theta = Math.acos(zDisplacment / dist);
		double phi = Math.atan2(yDisplacment, xDisplacment);

		showParticles &= dist > 1;
		
		// More strength for everything but players, lower dist is bigger effect
		if (!(entity instanceof EntityPlayer))
			dist *= 0.5;
		else if (entity instanceof EntityFX)
			return;
		else
		{
			if (((EntityPlayer) entity).capabilities.isCreativeMode)
				return;

			dist *= 2;
		}

		double vecX = -gravStrength * Math.sin(theta) * Math.cos(phi) / dist;
		double vecY = -gravStrength * Math.sin(theta) * Math.sin(phi) / dist;
		double vecZ = -gravStrength * Math.cos(theta) / dist;

		// trims gravity below max
		if (Math.abs(vecX) > maxGravXZ)
			vecX *= maxGravXZ / Math.abs(vecX);
		if (Math.abs(vecY) > maxGravY)
			vecY *= maxGravY / Math.abs(vecY);
		if (Math.abs(vecZ) > maxGravXZ)
			vecZ *= maxGravXZ / Math.abs(vecZ);

		// trims gravity above min
		if (Math.abs(vecX) < minGrav)
			vecX = 0;
		if (Math.abs(vecY) < minGrav)
			vecY = 0;
		if (Math.abs(vecZ) < minGrav)
			vecZ = 0;

		entity.setVelocity(entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);
	
		// shows smoke particles
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && showParticles && FMLClientHandler.instance().getClient().effectRenderer != null && Minecraft.getMinecraft().thePlayer != null)
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, entity.posX, entity.posY, entity.posZ, ((xCoord + 0.5) - entity.posX) / 10, ((yCoord + 0.5) - entity.posY) / 10, ((zCoord + 0.5) - entity.posZ) / 10));
	}

	/**
	 * Applies gravity to an entity with the passed configurations, this method
	 * calls the other with the TE's xyz coords
	 * 
	 * @param gravStrength - Strength of the gravity, usually a number < 3
	 * @param maxGravXZ - Max gravity that can be applied in the X and Z
	 *            directions
	 * @param maxGravY - Max gravity that can be applied in the Y direction
	 * @param minGrav - Minimum gravity that can be applied (prevents "wobbling"
	 *            if such a thing ever exists)
	 * @param range - The range of the gravitational effects
	 * @param entity - Entity to effect
	 * @param te - {@link TileEntity} to use as the center of gravity
	 */
	public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, TileEntity te, boolean showParticles)
	{
		applyGravity(gravStrength, maxGravXZ, maxGravY, minGrav, range, entity, te.xCoord, te.yCoord, te.zCoord, showParticles);
	}

	/**
	 * Applies gravity to the passed entity, with a center at the passed TE,
	 * calls the other method with default configuration values
	 * 
	 * @param entity - Entity to affect
	 * @param te - {@link TileEntity} to use as the center of gravity
	 */
	public static void applyGravity(Entity entity, TileEntity te, boolean showParticles)
	{
		applyGravity(c.STRENGTH, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV, c.RANGE, entity, te, showParticles);
	}

	/**
	 * Applies gravity to the passed entity, with a center at the passed
	 * coordinates, calls the other method with default configuration values
	 * 
	 * @param entity - Entity to affect
	 * @param x - x coord
	 * @param y - y coord
	 * @param z - z coord
	 */
	public static void applyGravity(Entity entity, int x, int y, int z, boolean showParticles)
	{
		applyGravity(c.STRENGTH, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV, c.RANGE, entity, x, y, z, showParticles);
	}

	/**
	 * Gets the star type of a star item, can handle items that are not
	 * instances of {@link ItemStar}
	 * 
	 * @param stack - Stack to get the type from
	 * @return {@link StarType} of the item
	 */
	public static IStar getType(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemStar && stack.stackTagCompound != null)
			return stars.getTypeByName(stack.stackTagCompound.getString("type"));
		else
			return null;
	}

	/**
	 * Sets the type of a star itemstack, can handle items that are not
	 * instances of {@link ItemStar}
	 * 
	 * @param stack - Stack to set the type on
	 * @param type - Type to use
	 * @return The itemstack effected
	 */
	public static ItemStack setType(ItemStack stack, StarType type)
	{
		if (stack != null && stack.getItem() instanceof ItemStar)
		{
			if (stack.stackTagCompound == null)
				stack.stackTagCompound = new NBTTagCompound();

			stack.stackTagCompound.setString("type", type.toString());
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
