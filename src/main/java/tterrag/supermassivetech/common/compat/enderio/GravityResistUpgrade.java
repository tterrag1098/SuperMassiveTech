package tterrag.supermassivetech.common.compat.enderio;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.item.IStarItem;
import tterrag.supermassivetech.common.registry.Stars.StarTier;
import tterrag.supermassivetech.common.util.Utils;
import crazypants.enderio.item.darksteel.AbstractUpgrade;
import crazypants.enderio.item.darksteel.ItemDarkSteelArmor;

public class GravityResistUpgrade extends AbstractUpgrade
{
    public static final String UPGRADE_NAME = "gravResistUpgrade";
    public static final String TIER_KEY = "gravUpgradeTier";
    public static final ItemStack UPGRADE_ITEM = new ItemStack(SuperMassiveTech.itemRegistry.star);

    StarTier level;

    public GravityResistUpgrade(int levels, int tier)
    {
        this(levels, StarTier.values()[tier]);
    }

    public GravityResistUpgrade(int levels, StarTier tier)
    {
        super(UPGRADE_NAME, UPGRADE_NAME, UPGRADE_ITEM, levels);
        this.level = tier;
    }

    public GravityResistUpgrade(NBTTagCompound tag)
    {
        super(UPGRADE_NAME, tag);
        this.level = StarTier.values()[tag.getInteger(TIER_KEY)];
    }

    @Override
    public boolean canAddToItem(ItemStack stack)
    {
        GravityResistUpgrade has = loadFromItem(stack);
        return has == null ? stack != null && stack.getItem() instanceof ItemDarkSteelArmor && this.level == StarTier.LOW : this.level == has.level.next();
    }

    @Override
    public boolean isUpgradeItem(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof IStarItem && Utils.getType(stack).getTier() == level;
    }

    @Override
    public boolean hasUpgrade(ItemStack stack)
    {
        return super.hasUpgrade(stack) && stack.getTagCompound().getCompoundTag(id).getInteger(TIER_KEY) == level.ordinal();
    }

    @Override
    public ItemStack getUpgradeItem()
    {
        return upgradeItem;
    }

    @Override
    public String getUnlocalizedName()
    {
        return UPGRADE_NAME + "." + level.ordinal();
    }

    @Override
    public String getUpgradeItemName()
    {
        return super.getUpgradeItemName() + " (" + Utils.lang.localize("tooltip.tier") + " " + level.toString() + ")";
    }

    @Override
    public void writeUpgradeToNBT(NBTTagCompound upgradeRoot)
    {
        upgradeRoot.setInteger(TIER_KEY, level.ordinal());
    }

    public static GravityResistUpgrade loadFromItem(ItemStack stack)
    {
        if (stack == null)
        {
            return null;
        }
        if (stack.stackTagCompound == null)
        {
            return null;
        }
        if (!stack.stackTagCompound.hasKey(KEY_UPGRADE_PREFIX + UPGRADE_NAME))
        {
            return null;
        }
        return new GravityResistUpgrade((NBTTagCompound) stack.stackTagCompound.getTag(KEY_UPGRADE_PREFIX + UPGRADE_NAME));
    }
}
