package tterrag.supermassivetech.client.fx;

import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.world.World;

/**
 * Smoke particle that doesn't "float" upwards, noclips, and disappears inside
 * of gravity wells
 */
public class EntityCustomSmokeFX extends EntitySmokeFX
{
	private double toX, toY, toZ;

	public EntityCustomSmokeFX(World world, double x, double y, double z, double toX, double toY, double toZ, double movementFactor)
	{
		this(world, x, y, z, (toX - x) * movementFactor, (toY - y) * movementFactor, (toZ - z) * movementFactor, 1.0F);

		this.toX = toX;
		this.toY = toY;
		this.toZ = toZ;
	}

	public EntityCustomSmokeFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, float par14)
	{
		super(par1World, par2, par4, par6, par8, par10, par12, par14);
		this.noClip = true;
	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge)
		{
			this.setDead();
		}

		if (isInRange())
		{
			this.setDead();
		}
		
		System.out.println(this.posX + " " + this.posY);

		this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
		this.moveEntity(this.motionX, this.motionY, this.motionZ);

		if (this.posY == this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}
	}

	private boolean isInRange()
	{
		return Math.floor(toX) == Math.floor(posX) &&
			   Math.floor(toY) == Math.floor(posY) &&
			   Math.floor(toZ) == Math.floor(posZ);
	}
}
