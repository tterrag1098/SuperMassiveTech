package tterrag.supermassivetech.common.util;

import static tterrag.supermassivetech.SuperMassiveTech.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import tterrag.core.common.Lang;
import tterrag.core.common.util.BlockCoord;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.api.common.item.IStarItem;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.client.util.ClientUtils;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.handlers.GravityArmorHandler;
import tterrag.supermassivetech.common.item.ItemGravityArmor;
import tterrag.supermassivetech.common.item.ItemStar;
import tterrag.supermassivetech.common.network.message.MessageUpdateGravityArmor.PowerUps;
import tterrag.supermassivetech.common.registry.Stars;
import tterrag.supermassivetech.common.tile.TileBlackHoleStorage;
import cofh.api.energy.IEnergyContainerItem;

public class Utils
{
    private static Constants c;
    private static Stars stars = Stars.instance;
    private static Material[] pickMats = { Material.rock, Material.iron, Material.anvil };
    private static Material[] shovelMats = { Material.clay, Material.snow, Material.ground };

    public static final Random rand = new Random();

    public static final Lang lang = new Lang(ModProps.LOCALIZING);
    
    public static void init()
    {
        c = Constants.instance();
    }

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
     * @param maxGravXZ - Max gravity that can be applied in the X and Z directions
     * @param maxGravY - Max gravity that can be applied in the Y direction
     * @param minGrav - Minimum gravity that can be applied (prevents "wobbling" if such a thing ever exists)
     * @param range - The range of the gravitational effects
     * @param entity - Entity to effect
     * @param xCoord - X coord of the center of gravity
     * @param yCoord - Y coord of the center of gravity
     * @param zCoord - Z coord of the center of gravity
     */
    public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, int xCoord, int yCoord, int zCoord,
            boolean showParticles)
    {
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
                        item.extractEnergy(s, (int) (c.getEnergyDrain() * gravForce) / 20, false);
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

        setEntityVelocity(entity, entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);

        showParticles &= dist > 1;

        // shows smoke particles

        if (showParticles && entity.worldObj.isRemote)
        {
            ClientUtils.spawnGravityEffectParticles(xCoord, yCoord, zCoord, entity, (float) Math.min(2, dist));
        }
    }

    public static void setEntityVelocity(Entity entity, double velX, double velY, double velZ)
    {
        entity.motionX = velX;
        entity.motionY = velY;
        entity.motionZ = velZ;
    }

    /**
     * Applies gravity to an entity with the passed configurations, this method calls the other with the TE's xyz coords
     * 
     * @param gravStrength - Strength of the gravity, usually a number < 3
     * @param maxGravXZ - Max gravity that can be applied in the X and Z directions
     * @param maxGravY - Max gravity that can be applied in the Y direction
     * @param minGrav - Minimum gravity that can be applied (prevents "wobbling" if such a thing ever exists)
     * @param range - The range of the gravitational effects
     * @param entity - Entity to effect
     * @param te - {@link TileEntity} to use as the center of gravity
     */
    public static void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity, TileEntity te, boolean showParticles)
    {
        applyGravity(gravStrength, maxGravXZ, maxGravY, minGrav, range, entity, te.xCoord, te.yCoord, te.zCoord, showParticles);
    }

    /**
     * Applies gravity to the passed entity, with a center at the passed TE, calls the other method with default configuration values
     * 
     * @param entity - Entity to affect
     * @param te - {@link TileEntity} to use as the center of gravity
     */
    public static void applyGravity(Entity entity, TileEntity te, boolean showParticles)
    {
        applyGravity(c.getStrength(), c.getMaxGravXZ(), c.getMaxGravY(), c.getMinGrav(), c.getRange(), entity, te, showParticles);
    }

    /**
     * Applies gravity to the passed entity, with a center at the passed coordinates, calls the other method with default configuration values
     * 
     * @param entity - Entity to affect
     * @param x - x coord
     * @param y - y coord
     * @param z - z coord
     */
    public static void applyGravity(Entity entity, int x, int y, int z, boolean showParticles)
    {
        applyGravity(c.getStrength(), c.getMaxGravXZ(), c.getMaxGravY(), c.getMinGrav(), c.getRange(), entity, x, y, z, showParticles);
    }

    /**
     * Gets the star type of a star item, can handle items that are not instances of {@link ItemStar}
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
    
    public static int getStarFuseRemaining(ItemStack star)
    {
        if (star != null && star.getItem() instanceof IStarItem && star.stackTagCompound != null)
            return star.stackTagCompound.getInteger("fuse");
        else
            return 0;
    }
    
    public static void setStarFuseRemaining(ItemStack star, int fuse)
    {
        if (star != null && star.getItem() instanceof IStarItem && star.stackTagCompound != null)
            star.stackTagCompound.setInteger("fuse", fuse);
    }

    /**
     * Sets the type of a star itemstack, can handle items that are not instances of {@link ItemStar}
     * 
     * @param stack - Stack to set the type on
     * @param type - Type to use
     * @return The itemstack affected
     */
    public static ItemStack setType(ItemStack stack, IStar type)
    {
        if (stack != null && stack.getItem() instanceof IStarItem)
        {
            if (stack.stackTagCompound == null)
                stack.stackTagCompound = new NBTTagCompound();

            stack.stackTagCompound.setString("type", type.getName());
            stack.stackTagCompound.setInteger("energy", type.getMaxEnergyStored(stack));
            stack.stackTagCompound.setInteger("fuse", type.getFuse());
        }
        else if (stack != null)
        {
            SuperMassiveTech.logger.error(String.format("A mod tried to set the type of an item that was not a star, item was %s", stack.getUnlocalizedName()));
        }
        else
        {
            SuperMassiveTech.logger.error("A mod tried to set the type of a null itemstack");
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
        if (item != null)
        {
            spawnItemInWorldWithRandomMotion(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, item));
        }
    }

    public static void spawnItemInWorldWithRandomMotion(EntityItem entity)
    {
        float f = (rand.nextFloat() * 0.1f) - 0.05f;
        float f1 = (rand.nextFloat() * 0.1f) - 0.05f;
        float f2 = (rand.nextFloat() * 0.1f) - 0.05f;

        entity.motionX += f;
        entity.motionY += f1;
        entity.motionZ += f2;

        entity.worldObj.spawnEntityInWorld(entity);
    }

    /**
     * Finds the proper tool for this material, returns "none" if there isn't one
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
    public static void formAdvancedTooltip(List<String> lines, ItemStack stack, IAdvancedTooltip tooltip)
    {
        formAdvancedTooltip(lines, stack, tooltip, Keyboard.getKeyIndex(ConfigHandler.tooltipKey1), Keyboard.getKeyIndex(ConfigHandler.tooltipKey2));
    }

    public static void formAdvancedTooltip(List<String> lines, ItemStack stack, IAdvancedTooltip tooltip, int key)
    {
        formAdvancedTooltip(lines, stack, tooltip, key, key);
    }

    public static void formAdvancedTooltip(List<String> lines, ItemStack stack, IAdvancedTooltip tooltip, int key, int alternateKey)
    {
        if (tooltip.getHiddenLines(stack) != null)
        {
            if (Keyboard.isKeyDown(key) || Keyboard.isKeyDown(alternateKey))
            {
                for (String s : lang.splitList(tooltip.getHiddenLines(stack)))
                {
                    String[] ss = s.split("~");
                    for (String line : ss)
                    {
                        lines.add(green ? EnumChatFormatting.GREEN.toString() + line : EnumChatFormatting.WHITE + line);
                    }
                    green = green ? false : true;
                }
                green = true;
            }
            else
            {
                lines.add(String.format("%s -%s- %s", EnumChatFormatting.RED + lang.localize("tooltip.hold") + EnumChatFormatting.YELLOW, getNameForKey(key),
                        EnumChatFormatting.RED + lang.localize("tooltip.moreInfo")));
            }
        }

        if (tooltip.getStaticLines(stack) != null)
        {
            if (tooltip.getHiddenLines(stack) != null)
                lines.add("");

            for (String s : lang.splitList(tooltip.getStaticLines(stack), "~"))
                lines.add(EnumChatFormatting.WHITE + s);
        }
    }

    private static String getNameForKey(int key)
    {
        switch (key)
        {
        case Keyboard.KEY_LSHIFT:
        case Keyboard.KEY_RSHIFT:
            return lang.localize("tooltip.shift");
        case Keyboard.KEY_LCONTROL:
        case Keyboard.KEY_RCONTROL:
            return lang.localize("tooltip.control");
        case Keyboard.KEY_LMENU:
        case Keyboard.KEY_RMENU:
            return lang.localize("tooltip.alt");
        }

        return Keyboard.getKeyName(key);
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

    /**
     * Applies the potion effects associated with gravity to the player at effect level <code> level </code>
     */
    public static void applyGravPotionEffects(EntityPlayer player, int level)
    {
        if (!player.capabilities.isCreativeMode)
        {
            player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, level, true));
            player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, level, true));
        }
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

    public static void writeUUIDsToNBT(UUID[] uuids, NBTTagCompound tag, String toName)
    {
        int[] uuidnums = new int[uuids.length * 4];

        for (int i = 0; i < uuids.length; i++)
        {
            long msd = uuids[i].getMostSignificantBits();
            long lsd = uuids[i].getLeastSignificantBits();

            uuidnums[i * 4] = (int) (msd >> 32);
            uuidnums[i * 4 + 1] = (int) msd;
            uuidnums[i * 4 + 2] = (int) (lsd >> 32);
            uuidnums[i * 4 + 3] = (int) lsd;
        }

        tag.setIntArray(toName, uuidnums);
    }

    public static UUID[] readUUIDsFromNBT(String name, NBTTagCompound tag)
    {
        int[] uuidnums = tag.getIntArray(name);
        UUID[] uuids = new UUID[uuidnums.length / 4];

        for (int i = 0; i < uuidnums.length; i += 4)
        {
            long msd = ((long) uuidnums[i]) << 32;
            msd += uuidnums[i + 1];
            long lsd = ((long) uuidnums[i + 2]) << 32;
            lsd += uuidnums[i + 3];

            uuids[i / 4] = new UUID(msd, lsd);
        }

        return uuids;
    }

    public static int toHex(int r, int g, int b)
    {
        int hex = 0;
        hex = hex | ((r) << 16);
        hex = hex | ((g) << 8);
        hex = hex | ((b));
        return hex;
    }

    // I don't really expect this to be very readable...but it works
    public static void spawnFireworkAround(BlockCoord block, int dimID)
    {
        World world = DimensionManager.getWorld(dimID);

        BlockCoord pos = new BlockCoord(0, 0, 0);

        while (!world.isAirBlock(pos.x, pos.y, pos.z))
        {
            pos.setPosition(moveRandomly(block.x), block.y + 2, moveRandomly(block.z));
        }

        ItemStack firework = new ItemStack(Items.fireworks);
        firework.stackTagCompound = new NBTTagCompound();
        NBTTagCompound expl = new NBTTagCompound();
        expl.setBoolean("Flicker", true);
        expl.setBoolean("Trail", true);

        int[] colors = new int[rand.nextInt(8) + 1];
        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = ItemDye.field_150922_c[rand.nextInt(16)];
        }
        expl.setIntArray("Colors", colors);
        byte type = (byte) (rand.nextInt(3) + 1);
        type = type == 3 ? 4 : type;
        expl.setByte("Type", type);

        NBTTagList explosions = new NBTTagList();
        explosions.appendTag(expl);

        NBTTagCompound fireworkTag = new NBTTagCompound();
        fireworkTag.setTag("Explosions", explosions);
        fireworkTag.setByte("Flight", (byte) 1);
        firework.stackTagCompound.setTag("Fireworks", fireworkTag);

        EntityFireworkRocket e = new EntityFireworkRocket(world, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, firework);
        world.spawnEntityInWorld(e);
    }

    private static final double distMult = 12d;

    private static double moveRandomly(double base)
    {
        return base + 0.5 + rand.nextDouble() * distMult - (distMult / 2);
    }

    public static boolean doStatesMatch(EntityPlayer e, PowerUps power, int slot, String state)
    {
        ItemStack armor = e.inventory.armorInventory[slot];
        return armorIsGravityArmor(armor) && armor.stackTagCompound.getString(power.toString()).equals(state);
    }

    public static boolean armorIsGravityArmor(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ItemGravityArmor;
    }

    public static double getGravResist(EntityPlayer player, double mult)
    {
        double percent = 0.0;
        for (int i = 0; i < 4; i++)
        {
            if (doStatesMatch(player, PowerUps.GRAV_RESIST, i, GravityArmorHandler.ON))
            {
                percent += 0.25;
            }
            else
            {
                int level = EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, player.inventory.armorInventory[i]);
                if (level > 0)
                {
                    percent += SuperMassiveTech.enchantRegistry.gravity.getReduction(1 / 5, level);
                }
            }
        }
        return percent * mult;
    }

    public static double getGravResist(EntityPlayer player)
    {
        return getGravResist(player, 1.0);
    }
    
    public static boolean shouldSpawnBlackHole(World worldObj) // TODO: implement something better for this
    {
      return worldObj.rand.nextBoolean();
    }
    
    /// Expensive (relatively speaking)
    public static int coordRound(double coord)
    {
      return new BigDecimal(coord).setScale(0, RoundingMode.HALF_DOWN).intValue();
    }

    public static boolean canBreakBlock(EntityPlayer player, World world, BlockCoord blockCoord)
    {
      // Make sure block isn't spawn protected or unbreakable
      return world.canMineBlock(player, blockCoord.x, blockCoord.y, blockCoord.z)
          && world.getBlock(blockCoord.x, blockCoord.y, blockCoord.z).canEntityDestroy(world, blockCoord.x, blockCoord.y, blockCoord.z, player);
    }
}
