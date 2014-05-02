package tterrag.supermassivetech.entity.item;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileStarHarvester;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityItemDepletedNetherStar extends EntityItemIndestructible
{
    private int counter = 0;
    private final int wait = 5;

    public EntityItemDepletedNetherStar(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
    {
        super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        int x = (int) this.posX, y = (int) this.posY - 1, z = (int) this.posZ;

        if (this.getEntityItem() != null && this.getEntityItem().getItem() != Items.nether_star && worldObj.getBlock(x, y, z) == SuperMassiveTech.blockRegistry.starHarvester)
        {
            TileStarHarvester te = (TileStarHarvester) worldObj.getTileEntity(x, y, z);
            if (te.venting)
            {
                System.out.println(x + " " + y + " " + z);

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

            if (this.getEntityItem().getItemDamage() > SuperMassiveTech.itemRegistry.depletedNetherStar.maxDamage)
                this.setEntityItemStack(new ItemStack(Items.nether_star));
        }
    }
}
