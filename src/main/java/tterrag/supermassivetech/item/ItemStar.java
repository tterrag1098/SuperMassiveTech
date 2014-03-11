package tterrag.supermassivetech.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.EntityItemIndestructible;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.registry.Stars.StarType;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStar extends ItemSMT
{
	private Stars stars = Stars.instance;

	public ItemStar(String unlocName)
	{
		super(unlocName, unlocName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		StarType type = Utils.getType(par1ItemStack);

		if (type == null)
			return 0;
		else
			return type.color;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		NBTTagCompound tag = new NBTTagCompound();
		for (StarType t : stars.types.values())
		{
			tag = new NBTTagCompound();
			tag.setString("type", t.toString());
			ItemStack i = new ItemStack(this);
			i.setTagCompound(tag);
			list.add(i);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(Utils.getType(par1ItemStack).toString());
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		return new EntityItemIndestructible(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
				((EntityItem) location).delayBeforeCanPickup);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}
}
