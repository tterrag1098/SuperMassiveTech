package tterrag.supermassivetech.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDyingBlock extends Entity
{
    private static final int DATA_ID = 10;
    private static final int MAX_LIFETIME = 10000;

    public EntityDyingBlock(World world)
    {
        super(world);
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    public EntityDyingBlock(World world, Block block, int meta, int x, int y, int z)
    {
        super(world);
        this.setPosition(x + 0.5, y + 0.5, z + 0.5);
        this.setBlock(block, meta);
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
    }

    public void setBlock(Block block, int meta)
    {
        setBlock(new ItemStack(block, 1, meta));
    }

    public void setBlock(ItemStack stack)
    {
        this.getDataWatcher().updateObject(DATA_ID, stack);
    }

    public Block getBlock()
    {
        return Block.getBlockFromItem(getBlockStack().getItem());
    }

    public int getMeta()
    {
        return getBlockStack().getItemDamage();
    }

    public ItemStack getBlockStack()
    {
        return this.getDataWatcher().getWatchableObjectItemStack(DATA_ID);
    }

    @Override
    protected void entityInit()
    {
        this.getDataWatcher().addObject(DATA_ID, new ItemStack(Blocks.stone));
    }

    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(this.posX - 0.5, this.posY - 0.5, this.posZ - 0.5, this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }

    @Override
    public void onUpdate()
    {
        if (!worldObj.isRemote && ticksExisted > 20 && motionX + motionY + motionZ < 0.01)
        {
            int x = MathHelper.floor_double(posX), y = MathHelper.floor_double(posY), z = MathHelper.floor_double(posZ);
            Block curBlock = worldObj.getBlock(x, y, z);
            if (curBlock == null || curBlock.isAir(worldObj, x, y, z) || curBlock.isReplaceable(worldObj, x, y, z))
            {
                worldObj.setBlock(x, y, z, getBlock());
                worldObj.setBlockMetadataWithNotify(x, y, z, getMeta(), 3);
                this.setDead();
            }
            else
            {
                this.posY += 1;
            }
        }
        else
        {
            System.out.println(worldObj.isRemote + " " + ticksExisted + "  " + (motionX + motionY + motionZ));
        }
        
        if (ticksExisted > MAX_LIFETIME)
        {
            this.setDead();
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= 0.04;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98;
        this.motionY *= 0.98;
        this.motionZ *= 0.98;

        super.onUpdate();
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        this.setBlock(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("blockStack")));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        NBTTagCompound blockTag = new NBTTagCompound();
        getBlockStack().writeToNBT(blockTag);
        tag.setTag("blockStack", blockTag);
    }
}
