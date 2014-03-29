package tterrag.supermassivetech.item;

import java.util.List;
import java.util.Random;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGravityArmor extends ItemArmor implements ISpecialArmor, IEnergyContainerItem, IAdvancedTooltip
{
	private ArmorType type;
	private final int CHARGE_SPEED = 1000,
	          DAMAGE_BASE = 2000, 
	          DAMAGE_RAND = 200, 
	          CAPACITY = 1000000;
	
	public static enum ArmorType
	{
		HELMET, CHESTPLATE, LEGS, BOOTS;
	}
	
	public ItemGravityArmor(ArmorMaterial mat, ArmorType type)
	{
		super(mat, 0, type.ordinal());
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
		setMaxDamage(101);
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
		return 0;
	}

	@Override
	public int getEnergyStored(ItemStack container)
	{
		if (container.stackTagCompound == null || !container.stackTagCompound.hasKey("energy"))
			return 0;
		return container.stackTagCompound.getInteger("Energy");
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
			return new ArmorProperties(0, 0.25, 0);
		
		return new ArmorProperties(0, 0.25, 80);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		if (armor.getTagCompound().getInteger("energy") <= 0) return 0;
		
		switch (type.ordinal())
		{
		case 0:
			return 3;
		case 1:
			return 8;
		case 2:
			return 6;
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
		return (new Random().nextInt(DAMAGE_RAND) - (DAMAGE_RAND / 2) + (DAMAGE_BASE * damageAmount));
	}
	
	private int getDamageFromEnergy(NBTTagCompound tag, int max)
	{
		return ((int) (Math.abs((float) tag.getInteger("energy") / CAPACITY - 1) * max)) + 1;
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
	public void getSubItems(Item stack, CreativeTabs tab, List list)
	{
		ItemStack i = new ItemStack(this, 1, 1);
		i.stackTagCompound = new NBTTagCompound();
		i.stackTagCompound.setInteger("energy", CAPACITY);
		list.add(i.copy());
		i.stackTagCompound.setInteger("energy", 0);
		i.setItemDamage(i.getMaxDamage());
		list.add(i.copy());
	}

	@Override
	public String[] getHiddenLines(ItemStack stack)
	{
		return new String[]{"" + stack.getTagCompound().getInteger("energy")};
	}

	@Override
	public String[] getStaticLines(ItemStack stack)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,	EntityPlayer player, List list, boolean held) 
	{
		if (this instanceof IAdvancedTooltip)
		{
			IAdvancedTooltip item = (IAdvancedTooltip) this;
			Utils.formAdvancedTooltip(list, stack, item);
		}
	}
}
