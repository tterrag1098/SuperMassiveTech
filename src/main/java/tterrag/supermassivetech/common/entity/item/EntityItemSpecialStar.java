package tterrag.supermassivetech.common.entity.item;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemSpecialStar extends EntityItemIndestructible
{
    public EntityItemSpecialStar(World world)
    {
        super(world);
    }

    public EntityItemSpecialStar(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
    {
        super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (this.getEntityItem() != null && this.getEntityItem().stackSize > 1)
        {
            this.setDead();
            if (!this.worldObj.isRemote)
                worldObj.newExplosion(this, posX, posY, posZ, 2.0f + (this.getEntityItem().stackSize - 1), true, true);
        }
    }
}
