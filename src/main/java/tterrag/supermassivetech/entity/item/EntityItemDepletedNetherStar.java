package tterrag.supermassivetech.entity.item;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileStarHarvester;

public class EntityItemDepletedNetherStar extends EntityItemIndestructible
{
    private int counter = 0;
    private final int wait = 5;

    public EntityItemDepletedNetherStar(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
    {
        super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
        this.lifespan = Integer.MAX_VALUE;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        
        int x = MathHelper.floor_double(this.posX), y = MathHelper.floor_double(this.posY) - 1, z = MathHelper.floor_double(this.posZ);

        if (this.getEntityItem() != null && this.getEntityItem().getItem() != Items.nether_star && worldObj.getBlock(x, y, z) == SuperMassiveTech.blockRegistry.starHarvester)
        {
            TileStarHarvester te = (TileStarHarvester) worldObj.getTileEntity(x, y, z);
            if (te.venting)
            {
                if (counter >= wait * this.getEntityItem().stackSize)
                {
                    counter = 0;
                    this.getEntityItem().setItemDamage(this.getEntityItem().getItemDamage() + 1);
                }
                else
                {
                    counter++;
                }
            }

            if (this.getEntityItem().getItemDamage() >= SuperMassiveTech.itemRegistry.depletedNetherStar.maxDamage)
                this.setEntityItemStack(new ItemStack(Items.nether_star));
        }
    }
}
