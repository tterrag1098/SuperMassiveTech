package tterrag.supermassivetech.item;

import tterrag.supermassivetech.entity.EntityStarHeart;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemStarHeart extends ItemSMT {

	public ItemStarHeart(String unlocName) {
		super(unlocName, "starHeart");
	}
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		
		return new EntityStarHeart(world, location.posX, location.posY, location.posZ, itemstack);
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
}
