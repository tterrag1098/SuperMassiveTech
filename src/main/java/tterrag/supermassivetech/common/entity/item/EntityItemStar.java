package tterrag.supermassivetech.common.entity.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.item.ItemStarSpecial;

public class EntityItemStar extends EntityItemIndestructible
{
    public EntityItemStar(World world)
    {
        super(world);
    }

    public EntityItemStar(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
    {
        super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!worldObj.isRemote)
        {
            int x = MathHelper.floor_double(posX);
            int y = MathHelper.floor_double(posY);
            int z = MathHelper.floor_double(posZ);

            Block block = worldObj.getBlock(x, y, z);

            if (block != SuperMassiveTech.blockRegistry.invisibleLight && block.isAir(worldObj, x, y, z))
            {
                worldObj.setBlock(x, y, z, SuperMassiveTech.blockRegistry.invisibleLight);
            }
        }

        if (this.getEntityItem() != null && this.getEntityItem().getItem() instanceof ItemStarSpecial && this.getEntityItem().stackSize > 1)
        {
            this.setDead();
            if (!this.worldObj.isRemote)
            {
                worldObj.newExplosion(this, posX, posY, posZ, 2.0f + (this.getEntityItem().stackSize - 1), true, true);
            }
        }
    }
}
