package tterrag.supermassivetech.util;

import static tterrag.supermassivetech.SuperMassiveTech.itemRegistry;

import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.item.IStarItem;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class Utils
{
    private static Constants c;
    private static Stars stars = Stars.instance;
    private static Material[] pickMats = { Material.rock, Material.iron, Material.anvil };
    private static Material[] shovelMats = { Material.clay, Material.snow, Material.ground };

    public static void init()
    {
        c = Constants.instance();
    }

    public static CreativeTabs tab = new CreativeTabs(CreativeTabs.getNextID(), "superMassiveTech")
    {
        @Override
        public Item getTabIconItem()
        {
            return SuperMassiveTech.itemRegistry.heartOfStar;
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

        if (formatK && Long.toString(amnt).length() < 7 && Long.toString(amnt).length() > 3) { return formatSmallerNumber(prefix, suffix, amnt, useDecimals); }

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
        if (entity instanceof EntityFX)
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

        if (entity instanceof EntityPlayer)
        {
            if (((EntityPlayer) entity).capabilities.isCreativeMode)
                return;

            // instant half gravity
            gravForce *= 0.5;

            double armorMult = 1.0;
            for (ItemStack s : ((EntityPlayer) entity).inventory.armorInventory)
            {
                // handles gravity armor
                if (s != null && itemRegistry.armors.contains(s.getItem()))
                {
                    IEnergyContainerItem item = (IEnergyContainerItem) s.getItem();
                    if (item.getEnergyStored(s) > 0)
                    {
                        item.extractEnergy(s, (int) (c.ENERGY_DRAIN * gravForce) / 20, false);
                        armorMult -= 0.23;
                    }
                }
                // handles enchant
                else if (s != null && EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, s) != 0)
                {
                    armorMult -= 0.23 / 2;
                    s.damageItem(new Random().nextInt(100) < 2 && !entity.worldObj.isRemote ? 1 : 0, (EntityLivingBase) entity);
                }
            }

            gravForce *= armorMult;

        }
        else
        {
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
                    yCoord + 0.5, zCoord + 0.5, 1 / range));
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
        if (stack != null && stack.getItem() instanceof IStarItem && stack.stackTagCompound != null)
            return stars.getTypeByName(stack.stackTagCompound.getString("type"));
        else
            return null;
    }

    public static int getStarPowerRemaining(ItemStack star)
    {
        if (star != null && star.getItem() instanceof IStarItem && star.stackTagCompound != null)
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
        if (stack != null && stack.getItem() instanceof IStarItem)
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
     * Finds the proper tool for this material, returns "none" if there isn't
     * one
     */
    public static String getToolClassFromMaterial(Material mat)
    {
        if (ArrayUtils.contains(pickMats, mat))
            return "pickaxe";
        if (ArrayUtils.contains(shovelMats, mat))
            return "shovel";
        if (mat == Material.wood)
            return "axe";
        else
            return "none";
    }

    /**
     * Gets the tool level for this material
     */
    public static int getToolLevelFromMaterial(Material mat)
    {
        if (ArrayUtils.contains(pickMats, mat))
        {
            if (mat == Material.rock)
                return 0;
            if (mat == Material.iron)
                return 1;
            if (mat == Material.anvil)
                return 1;
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
                for (String s : splitList(item.getHiddenLines(stack)))
                {
                    String[] ss = s.split("~");
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
                list.add(String.format("%s -%s- %s", EnumChatFormatting.RED + localize("tooltip.hold", true) + EnumChatFormatting.YELLOW, localize("tooltip.shift", true), EnumChatFormatting.RED + localize("tooltip.moreInfo", true)));
            }
        }

        if (item.getStaticLines(stack) != null)
        {
            if (item.getHiddenLines(stack) != null)
                list.add("");

            for (String s : splitList(item.getStaticLines(stack), "~"))
                list.add(EnumChatFormatting.WHITE + s);
        }
    }

    public static EnumChatFormatting getColorForPowerLeft(double power, double powerMax)
    {
        if (power / powerMax <= .1)
            return EnumChatFormatting.RED;
        else if (power / powerMax <= .25)
            return EnumChatFormatting.GOLD;
        else
            return EnumChatFormatting.GREEN;
    }

    public static void registerEventHandlers(boolean useForge, Class<?>... classes)
    {
        for (Class<?> c : classes)
        {
            try
            {
                if (useForge)
                    MinecraftForge.EVENT_BUS.register(c.newInstance());
                else
                    FMLCommonHandler.instance().bus().register(c.newInstance());
            }
            catch (Throwable t)
            {
                SuperMassiveTech.logger.severe(String.format("Failed to register handler %s, this is a serious bug, certain functions will not be avaialble!", c.getName()));
                t.printStackTrace();
            }
        }
    }

    /**
     * Applies the potion effects associated with gravity to the player at
     * effect level <code> level </code>
     */
    public static void applyGravPotionEffects(EntityPlayer player, int level)
    {
        if (!player.capabilities.isCreativeMode)
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, level, true));
            player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, level, true));
        }
    }
    
    public static String localize(String unloc, boolean appendModid)
    {
        if (appendModid)
            return localize(Reference.LOCALIZING + "." + unloc);
        else return localize(unloc);
    }
    
    public static String localize(String unloc)
    {
        return StatCollector.translateToLocal(unloc);
    }
    
    public static String[] localizeList(String unloc)
    {
        return splitList(localize(unloc, true));
    }
    
    private static String[] splitList(String list, String splitRegex)
    {
        return list.split(splitRegex);
    }
    
    public static String[] splitList(String list)
    {
        return splitList(list, "\\|");
    }

    public static String makeTooltipString(List<String> strs)
    {
        String toReturn = "";
        for (String s : strs)
        {
            toReturn += s;
            if (strs.indexOf(s) != strs.size() - 1)
                toReturn += "~";
        }
        return toReturn;
    }
}
