package tterrag.supermassivetech.entity;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.registry.Stars.StarType;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class EntityStarHeart extends EntityItemIndestructible
{
	public EntityStarHeart(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
	{
		super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.isBurning())
		{
			changeToStar();
		}
	}

	private void changeToStar()
	{
		ItemStack star = new ItemStack(SuperMassiveTech.itemRegistry.star, this.getEntityItem().stackSize);

		// Sets the type of the star to a random type
		Utils.setType(star, (StarType) SuperMassiveTech.starRegistry.types.values().toArray()[new Random().nextInt(SuperMassiveTech.starRegistry.types.values().size())]);

		worldObj.newExplosion(this, posX, posY, posZ, 3.0f + (this.getEntityItem().stackSize), true, true);

		worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, star));
		
		this.setDead();
	}

	@Override
	public boolean isBurning()
	{
		boolean flag = this.worldObj != null && this.worldObj.isRemote;
		// TODO PR forge or AT
		Integer fire = ObfuscationReflectionHelper.getPrivateValue(Entity.class, this, "fire", "field_70151_c");
		return (fire > 0 || flag && this.getFlag(0));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (par1DamageSource.isFireDamage())
			return false;
		else
			return super.attackEntityFrom(par1DamageSource, par2);
	}
}
