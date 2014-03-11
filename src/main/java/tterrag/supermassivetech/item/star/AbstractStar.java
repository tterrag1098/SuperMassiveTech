package tterrag.supermassivetech.item.star;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.EntityItemIndestructible;
import tterrag.supermassivetech.item.ItemSMT;

public abstract class AbstractStar extends ItemSMT implements IStar{

	public AbstractStar(String unlocName, String textureName) {
		super(unlocName, textureName);
	}
	
	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}
	
	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack) {
		return new EntityItemIndestructible(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ, ((EntityItem)location).delayBeforeCanPickup);
	}

	@Override
	public int getMaxPower(ItemStack stack) {
		return 0;
	}

	@Override
	public int usePower(ItemStack stack, int power) {
		return 0;
	}

	@Override
	public boolean isCritical(ItemStack stack) {
		return false;
	}

	@Override
	public double getCriticalTimeleft(ItemStack stack) {
		return 0;
	}
}
