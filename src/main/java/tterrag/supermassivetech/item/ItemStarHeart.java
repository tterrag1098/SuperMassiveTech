package tterrag.supermassivetech.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.EntityStarHeart;

public class ItemStarHeart extends ItemSMT
{

	public ItemStarHeart(String unlocName)
	{
		super(unlocName, "starHeart");
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{

		return new EntityStarHeart(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ, ((EntityItem) location).delayBeforeCanPickup);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}
}
