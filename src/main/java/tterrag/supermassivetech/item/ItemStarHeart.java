package tterrag.supermassivetech.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.item.EntityItemStarHeart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStarHeart extends ItemSMT
{

	public ItemStarHeart(String unlocName)
	{
		super(unlocName, "starHeart");
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		return new EntityItemStarHeart(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
				((EntityItem) location).delayBeforeCanPickup);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		list.add("Creates a star when ignited in-world.");
		list.add("More fire in the vicinity causes");
		list.add("a higher chance for better stars.");
	}
}
