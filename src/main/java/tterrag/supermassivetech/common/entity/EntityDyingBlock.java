package tterrag.supermassivetech.common.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDyingBlock extends Entity
{
    private static final int DATA_ID = 10;
    
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
    public AxisAlignedBB getCollisionBox(Entity e)
    {
        return getBoundingBox();
    }
    
    @Override
    public AxisAlignedBB getBoundingBox()
    {
        return AxisAlignedBB.getBoundingBox(this.posX - 0.5, this.posY - 0.5, this.posZ - 0.5, this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5);
    }
    
    @Override
    public void onUpdate()
    {
        this.moveEntity(motionX, motionY, motionZ);

        this.motionY -= 0.04;
        
        if (worldObj.getTotalWorldTime() % 500 == 0)
        {
            this.setDead();
        }
        
        super.onUpdate();
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
