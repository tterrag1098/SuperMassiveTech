package tterrag.supermassivetech.entity.item;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityItemIndestructible extends EntityItem
{
    public EntityItemIndestructible(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
    {
        super(world, posX, posY, posZ, itemstack);
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.isImmuneToFire = true;
        this.delayBeforeCanPickup = delay;
    }

    public EntityItemIndestructible(World world)
    {
        super(world);
    }

    @Override
    public boolean isEntityInvulnerable()
    {
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
    }
}
