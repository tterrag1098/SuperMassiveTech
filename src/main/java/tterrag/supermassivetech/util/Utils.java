package tterrag.supermassivetech.util;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import static tterrag.supermassivetech.SuperMassiveTech.*;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Utils
{
	private static Constants c = Constants.instance();
	private static Stars stars = Stars.instance;
	private static Material[] pickMats = {Material.rock, Material.iron, Material.anvil};
	private static Material[] shovelMats = {Material.clay, Material.snow, Material.ground};

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
	 * @param suffix - The string to put after the formatted number
	 * @param amnt - The number to be formatted
	 * @param useDecimals - Whether or not to use decimals in the representation
	 * @param formatK - Whether or not to format the thousands
	 * @return
	 */
	public static String formatString(String prefix, String suffix, long amnt, boolean useDecimals, boolean formatK)
	{
		if (amnt == TileBlackHoleStorage.max)
		{
			prefix += "2^40" + suffix;
			return prefix;
		}

		if (formatK && Long.toString(amnt).length() < 7 && Long.toString(amnt).length() > 3)
		{
			return formatSmallerNumber(prefix, suffix, amnt, useDecimals);
		}

		switch (Long.toString(amnt).length())
		{
		case 7:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 3) : "") + "M" + suffix;
			return prefix;
		case 8:
			prefix += Long.toString(amnt).substring(0, 2) + (useDecimals ? "." + Long.toString(amnt).substring(2, 4) : "") + "M" + suffix;
			return prefix;
		case 9:
			prefix += Long.toString(amnt).substring(0, 3) + (useDecimals ? "." + Long.toString(amnt).substring(3, 5) : "") + "M" + suffix;
			return prefix;
		case 10:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 3) : "") + "B" + suffix;
			return prefix;
		case 11:
			prefix += Long.toString(amnt).substring(0, 2) + (useDecimals ? "." + Long.toString(amnt).substring(2, 4) : "") + "B" + suffix;
			return prefix;
		case 12:
			prefix += Long.toString(amnt).substring(0, 3) + (useDecimals ? "." + Long.toString(amnt).substring(3, 5) : "") + "B" + suffix;
			return prefix;
		case 13:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 5) : "") + "T" + suffix;
			return prefix;
		default:
			prefix += "" + amnt + suffix;
			return prefix;
		}
	}

	/**
	 * Formats a string and number for use in GUIs and tooltips
	 * 
	 * @param prefix - The string to put before the formatted number
	 * @param suffix - The string to put after the formatted number
	 * @param amnt - The number to be formatted
	 * @param useDecimals - Whether or not to use decimals in the representation
	 * @return
	 */
	public static String formatString(String prefix, String suffix, long amnt, boolean useDecimals)
	{
		return formatString(prefix, suffix, amnt, useDecimals, false);
	}

	private static String formatSmallerNumber(String prefix, String suffix, long amnt, boolean useDecimals)
	{
		switch (Long.toString(amnt).length())
		{
		case 4:
			prefix += Long.toString(amnt).substring(0, 1) + (useDecimals ? "." + Long.toString(amnt).substring(1, 3) : "") + "K" + suffix;
			return prefix;
		case 5:
			prefix += Long.toString(amnt).substring(0, 2) + (useDecimals ? "." + Long.toString(amnt).substring(2, 4) : "") + "K" + suffix;
			return prefix;
		case 6:
			prefix += Long.toString(amnt).substring(0, 3) + (useDecimals ? "." + Long.toString(amnt).substring(3, 5) : "") + "K" + suffix;
			return prefix;
		}
		return "";
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
		if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityItem))
			return;

		// distance forumla
		double dist = Math.sqrt(Math.pow(xCoord + 0.5 - entity.posX, 2) + Math.pow(zCoord + 0.5 - entity.posZ, 2) + Math.pow(yCoord + 0.5 - entity.posY, 2));

		if (dist > range)
			return;

		double xDisplacment = entity.posX - (xCoord + 0.5);
		double yDisplacment = entity.posY - (yCoord + 0.5);
		double zDisplacment = entity.posZ - (zCoord + 0.5);

		// http://en.wikipedia.org/wiki/Spherical_coordinate_system#Coordinate_system_conversions
		double theta = Math.acos(zDisplacment / dist);
		double phi = Math.atan2(yDisplacment, xDisplacment);

		// Gravity decreases linearly 
		double gravForce = gravStrength * (1 - dist / range);

		// More strength for everything but players
		if (entity instanceof EntityPlayer){
			if (((EntityPlayer) entity).capabilities.isCreativeMode)
				return;

			gravForce *= 0.5;

			double armorMult = 1.0;
			for (ItemStack s : ((EntityPlayer)entity).inventory.armorInventory){
				if (s != null && itemRegistry.armors.contains(s.getItem()))
				{
					armorMult -= 0.25;
				}
			}
			
			gravForce *= armorMult;
			
		} else {
			gravForce *= 2;
		}

		double vecX = -gravForce * Math.sin(theta) * Math.cos(phi);
		double vecY = -gravForce * Math.sin(theta) * Math.sin(phi);
		double vecZ = -gravForce * Math.cos(theta);

		// trims gravity above max
		if (Math.abs(vecX) > maxGravXZ)
			vecX *= maxGravXZ / Math.abs(vecX);
		if (Math.abs(vecY) > maxGravY)
			vecY *= maxGravY / Math.abs(vecY);
		if (Math.abs(vecZ) > maxGravXZ)
			vecZ *= maxGravXZ / Math.abs(vecZ);

		// trims gravity below min
		if (Math.abs(vecX) < minGrav)
			vecX = 0;
		if (Math.abs(vecY) < minGrav)
			vecY = 0;
		if (Math.abs(vecZ) < minGrav)
			vecZ = 0;

		entity.setVelocity(entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);

		showParticles &= dist > 1;

		// shows smoke particles
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && showParticles && FMLClientHandler.instance().getClient().effectRenderer != null
				&& Minecraft.getMinecraft().thePlayer != null)
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, entity.posX, entity.posY, entity.posZ, xCoord + 0.5,
					yCoord + 0.5, zCoord + 0.5, (double) (1/range)));
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

	public static int getStarPowerRemaining(ItemStack star)
	{
		if (star != null && star.getItem() instanceof ItemStar && star.stackTagCompound != null)
			return star.stackTagCompound.getInteger("energy");
		else
			return 0;
	}

	/**
	 * Sets the type of a star itemstack, can handle items that are not
	 * instances of {@link ItemStar}
	 * 
	 * @param stack - Stack to set the type on
	 * @param type - Type to use
	 * @return The itemstack effected
	 */
	public static ItemStack setType(ItemStack stack, IStar type)
	{
		if (stack != null && stack.getItem() instanceof ItemStar)
		{
			if (stack.stackTagCompound == null)
				stack.stackTagCompound = new NBTTagCompound();

			stack.stackTagCompound.setString("type", type.toString());
			stack.stackTagCompound.setInteger("energy", type.getPowerStoredMax());
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

	/**
	 * @author powercrystals
	 */
	public static boolean stacksEqual(ItemStack s1, ItemStack s2)
	{
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null || s2 == null)
			return false;
		if (!s1.isItemEqual(s2))
			return false;
		if (s1.getTagCompound() == null && s2.getTagCompound() == null)
			return true;
		if (s1.getTagCompound() == null || s2.getTagCompound() == null)
			return false;
		return s1.getTagCompound().equals(s2.getTagCompound());
	}

	public static void spawnItemInWorldWithRandomMotion(World world, ItemStack item, int x, int y, int z)
	{
		float f = (float) Math.random() + x;
		float f1 = (float) Math.random() + y;
		float f2 = (float) Math.random() + z;

		world.spawnEntityInWorld(new EntityItem(world, f, f1, f2, item));
	}

	/**
	 * Finds the proper tool for this material, returns "none" if there isn't one
	 */
	public static String getToolClassFromMaterial(Material mat)
	{
		if (ArrayUtils.contains(pickMats, mat)) return "pickaxe";
		if (ArrayUtils.contains(shovelMats, mat)) return "shovel";
		if (mat == Material.wood) return "axe";
		else return "none";
	}

	/**
	 * Gets the tool level for this material
	 */
	public static int getToolLevelFromMaterial(Material mat)
	{
		if (ArrayUtils.contains(pickMats, mat))
		{
			if (mat == Material.rock) return 0;
			if (mat == Material.iron) return 1;
			if (mat == Material.anvil) return 1;
		}
		return 0;
	}

	private static boolean green = true;
	/**
	 * In-place adds to a list, forming an advanced tooltip from the passed item
	 */
	public static void formAdvancedTooltip(List<Object> list, ItemStack stack, IAdvancedTooltip item)
	{
		if (item.getHiddenLines(stack) != null)
		{
			if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
			{
				for (String s : item.getHiddenLines(stack))
				{
					String[] ss = s.split("\n");
					for (String line : ss)
					{
						list.add(green ? EnumChatFormatting.GREEN.toString() + line : EnumChatFormatting.WHITE + line);
					}
					green = green ? false : true;
				}
				green = true;
			}
			else
			{
				list.add(EnumChatFormatting.RED + "Hold" + EnumChatFormatting.YELLOW + " -Shift- " + EnumChatFormatting.RED + "for more info");
			}
		}

		if (item.getStaticLines(stack) != null)
		{
			list.add("");

			for (String s : item.getStaticLines(stack))
				list.add(s);
		}		
	}
}
