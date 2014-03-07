package tterrag.supermassivetech.entity;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.item.ItemStar.StarType;
import tterrag.supermassivetech.util.Utils;

public class EntityStarHeart extends EntityItem
{

	public EntityStarHeart(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
	{
		super(world, posX, posY, posZ, itemstack);
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.isImmuneToFire = true;
		this.delayBeforeCanPickup = delay;
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
		Utils.setType(star, StarType.values()[new Random().nextInt(StarType.values().length)]);

		worldObj.setBlock((int) posX, (int) posY, (int) posZ, Blocks.air);
		worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ));
		this.setDead();
	}

	@Override
	public boolean isEntityInvulnerable()
	{
		return true;
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
