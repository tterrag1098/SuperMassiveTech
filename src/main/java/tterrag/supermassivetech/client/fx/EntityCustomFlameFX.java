package tterrag.supermassivetech.client.fx;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.world.World;

public class EntityCustomFlameFX extends EntityFlameFX
{
    private double toX, toY, toZ;

    /**
     * @param x - starting posX
     * @param y - starting posY
     * @param z - starting posZ
     * @param toX - ending posX
     * @param toY - ending posY
     * @param toZ - ending posZ
     * @param movementFactor - factor to multiply movement by (usually < 1)
     */
    public EntityCustomFlameFX(World world, double x, double y, double z, double toX, double toY, double toZ, double movementFactor)
    {
        super(world, x, y, z, (toX + 0.5 - x) * movementFactor, (toY + 0.3 - y) * movementFactor, (toZ + 0.5 - z) * movementFactor);

        this.toX = toX + 0.5;
        this.toY = toY + 0.3;
        this.toZ = toZ + 0.5;
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

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
        return Math.floor(toX) == Math.floor(posX) && Math.floor(toY) == Math.floor(posY) && Math.floor(toZ) == Math.floor(posZ);
    }
}
