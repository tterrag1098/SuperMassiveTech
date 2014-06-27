package tterrag.supermassivetech.item;

import java.util.List;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.ClientUtils;
import tterrag.supermassivetech.util.Utils;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGravityArmor extends ItemArmor implements ISpecialArmor, IEnergyContainerItem, IAdvancedTooltip
{
    public final ArmorType type;
    private final int CHARGE_SPEED = 1000, DAMAGE_BASE = 2000, DAMAGE_RAND = 200, CAPACITY = 1000000, MAX = Integer.MAX_VALUE;
    private final float PROT = 0.23f;
    private static long lastMessageTime = 0;

    public static enum ArmorType
    {
        BOOTS, LEGS, CHESTPLATE, HELMET;
    }

    public ItemGravityArmor(ArmorMaterial mat, ArmorType type)
    {
        super(mat, 0, -(type.ordinal() - 3));
        String texture = "", unlocalized = "";
        this.type = type;
        switch (type)
        {
        case BOOTS:
            texture = "supermassivetech:gravityBoots";
            unlocalized = "gravityBoots";
            break;
        case CHESTPLATE:
            texture = "supermassivetech:gravityChest";
            unlocalized = "gravityChestplate";
            break;
        case HELMET:
            texture = "supermassivetech:gravityHelm";
            unlocalized = "gravityHelmet";
            break;
        case LEGS:
            texture = "supermassivetech:gravityLegs";
            unlocalized = "gravityLeggings";
            break;
        }
        setTextureName(texture);
        setCreativeTab(SuperMassiveTech.tabSMT);
        setUnlocalizedName(unlocalized);
        setMaxStackSize(1);
        setMaxDamage(100);
        setNoRepair();
    }

    public ItemStack create()
    {
        ItemStack i = new ItemStack(this, 1, this.getMaxDamage());
        i.stackTagCompound = new NBTTagCompound();
        i.stackTagCompound.setInteger("energy", 0);
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
            ClientUtils.addAllArmorPowersTrue(i.stackTagCompound, this.type);
        return i;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        int suffix = this.armorType == 2 ? 2 : 1; // thanks tcon
        return Reference.MOD_TEXTUREPATH + ":textures/armor/gravityArmor" + suffix + ".png";
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
    {
        if (container.stackTagCompound == null)
        {
            container.stackTagCompound = new NBTTagCompound();
        }
        int energy = container.stackTagCompound.getInteger("energy");
        int energyReceived = Math.min(CAPACITY - energy, Math.min(this.CHARGE_SPEED, maxReceive));
        if (!simulate)
        {
            energy += energyReceived;
            container.stackTagCompound.setInteger("energy", energy);
        }
        container.setItemDamage(getDamageFromEnergy(container.stackTagCompound, container.getMaxDamage()));
        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
    {
        if (container == null || container.getTagCompound() == null)
            return 0;

        int available = container.stackTagCompound.getInteger("energy");
        int removed;
        if (maxExtract < available)
        {
            if (!simulate)
                container.stackTagCompound.setInteger("energy", available - maxExtract);
            removed = maxExtract;
        }
        else
        {
            if (!simulate)
                container.stackTagCompound.setInteger("energy", 0);
            removed = available;
        }
        if (!simulate)
            container.setItemDamage(getDamageFromEnergy(container.stackTagCompound, container.getMaxDamage()));

        return removed;
    }

    @Override
    public int getEnergyStored(ItemStack container)
    {
        if (container == null || container.stackTagCompound == null || !container.stackTagCompound.hasKey("energy"))
            return 0;
        return container.stackTagCompound.getInteger("energy");
    }

    @Override
    public int getMaxEnergyStored(ItemStack container)
    {
        return CAPACITY;
    }

    @Override
    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot)
    {
        if (armor.getTagCompound().getInteger("energy") <= 0)
            return new ArmorProperties(0, PROT, 0);

        if (slot == 0 && source == DamageSource.fall)
            return new ArmorProperties(0, PROT * 4, MAX);

        if (slot == 3 && source == DamageSource.fallingBlock)
            return new ArmorProperties(0, PROT * 4, MAX);

        return new ArmorProperties(0, PROT, source.isUnblockable() && !source.isFireDamage() ? 0 : MAX);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
    {
        if (armor.getTagCompound().getInteger("energy") <= 0)
            return 0;

        switch (type.ordinal())
        {
        case 0:
            return 3;
        case 1:
            return 6;
        case 2:
            return 8;
        case 3:
            return 3;
        default:
            return 0;
        }
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && stack.getTagCompound().getInteger("energy") > 0)
        {
            NBTTagCompound tag = stack.getTagCompound();

            int decrement = tag.getInteger("energy") - (getDamage(damage));
            tag.setInteger("energy", (decrement <= 0 ? 0 : decrement));
            stack.setItemDamage(getDamageFromEnergy(tag, stack.getMaxDamage()));

            stack.stackTagCompound = tag;
        }
    }

    private int getDamage(int damageAmount)
    {
        return (new Random().nextInt(DAMAGE_RAND) - (DAMAGE_RAND / 2)) + (DAMAGE_BASE * damageAmount);
    }

    private int getDamageFromEnergy(NBTTagCompound tag, int max)
    {
        return ((int) (Math.abs(((float) tag.getInteger("energy") / CAPACITY) - 1) * max) + 1);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (par1ItemStack.stackTagCompound == null)
            initTag(par1ItemStack);
    }

    @Override
    public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        initTag(par1ItemStack);

        if (par2World.isRemote && System.currentTimeMillis() > lastMessageTime + 1000)
        {
            par3EntityPlayer.addChatMessage(new ChatComponentText(Utils.localize("tooltip.onArmorCrafted", true)).setChatStyle(new ChatStyle()
                    .setColor(EnumChatFormatting.YELLOW)));
            lastMessageTime = System.currentTimeMillis();
        }
    }

    private void initTag(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
            tag = new NBTTagCompound();

        tag.setInteger("energy", 0);
        stack.setTagCompound(tag);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item stack, CreativeTabs tab, List list)
    {
        ItemStack i = create();
        list.add(i.copy());
        i.stackTagCompound.setInteger("energy", CAPACITY);
        i.setItemDamage(1);
        list.add(i.copy());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        ItemGravityArmor item = (ItemGravityArmor) stack.getItem();

        return ClientUtils.getLinesForArmor(stack, item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        return EnumChatFormatting.WHITE + Utils.localize("tooltip.powerRemaining", true) + ": "
                + Utils.getColorForPowerLeft(stack.getTagCompound().getInteger("energy"), CAPACITY)
                + Utils.formatString("", " RF", stack.getTagCompound().getInteger("energy"), true, true);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    @Override
    public int getItemEnchantability()
    {
        return 0;
    }
}
