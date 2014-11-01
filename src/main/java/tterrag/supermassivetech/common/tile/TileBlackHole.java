package tterrag.supermassivetech.common.tile;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.api.common.tile.IBlackHole;
import tterrag.supermassivetech.common.block.BlockBlackHole;
import tterrag.supermassivetech.common.entity.EntityDyingBlock;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateBlackHole;
import tterrag.supermassivetech.common.registry.BlackHoleEnergyRegistry;
import tterrag.supermassivetech.common.tile.abstracts.TileSMT;
import tterrag.supermassivetech.common.util.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileBlackHole extends TileSMT implements IBlackHole, IWailaAdditionalInfo
{
    private long storedEnergy = 0;
    private long lastStoredEnergy = 0;

    private static final float SIZE_MULT = 1f / 1000000f;
    private static final float SIZE_BASE = 0.5f;

    private static final DamageSource dmgBlackHole = new DamageSourceBlackHole("dmg.blackHole");

    public TileBlackHole()
    {
        super(1, 1, 1000, 1000, Constants.instance().getMinGrav());
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            doBlockSearch();

            if (storedEnergy != lastStoredEnergy)
            {
                sendPacket();
                lastStoredEnergy = storedEnergy;
            }
        }

        doEmitEnergy();
    }

    private void doBlockSearch()
    {
        float sizeMult = 2.9f;
        int size = (int) (getSize() * sizeMult);

        if (size != 0)
        {
            Random rand = worldObj.rand;

            int restartCount = 0;
            int maxChecks = size * 10;
            for (int i = 0; i < 1 && restartCount < maxChecks; i++)
            {
                // find a random block in the cubic range
                int bX = xCoord + rand.nextInt(size) - rand.nextInt(size);
                int bY = yCoord + rand.nextInt(size) - rand.nextInt(size);
                int bZ = zCoord + rand.nextInt(size) - rand.nextInt(size);

                // if is within the circular range
                if (getDistanceFrom(bX, bY, bZ) <= size * sizeMult)
                {
                    Block block = worldObj.getBlock(bX, bY, bZ);
                    float hardness = block.getBlockHardness(worldObj, bX, bY, bZ);

                    // make sure block is not unbreakable, nonexistant, or myself
                    if (!(hardness < 0 || block.isAir(worldObj, bX, bY, bZ) || block instanceof BlockBlackHole))
                    {
                        if (block.renderAsNormalBlock())
                        {
                            worldObj.spawnEntityInWorld(new EntityDyingBlock(worldObj, block, worldObj.getBlockMetadata(bX, bY, bZ), bX, bY, bZ));
                        }

                        worldObj.setBlockToAir(bX, bY, bZ);
                    }
                    else
                    {
                        i--; // try again
                        restartCount++;
                    }
                }
                else
                {
                    i--; // try again
                    restartCount++;
                }
            }
        }
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageUpdateBlackHole(xCoord, yCoord, zCoord, storedEnergy), worldObj.provider.dimensionId);
    }

    private void doEmitEnergy()
    {
        if (worldObj.isRemote)
        {
            spawnEmissionParticles();
        }
        else if (worldObj.getTotalWorldTime() % 5 == 0)
        {
            storedEnergy = Math.max(0, storedEnergy - 1);
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnEmissionParticles()
    {
        if (getEnergy() > 0)
        {
            for (int i = 0; i < getSize() * 2; i++)
            {
                double pX = xCoord + 0.5, pY = yCoord + 0.5, pZ = zCoord + 0.5;
                double maxAngle = getSize() * 0.05;
                double velX = worldObj.rand.nextDouble() * maxAngle - (maxAngle / 2), velZ = worldObj.rand.nextDouble() * maxAngle - (maxAngle / 2), velY = getSize() / 10;
                velY = i % 2 == 0 ? -velY * 1.5 : velY;
                worldObj.spawnParticle("smoke", pX, pY, pZ, velX, velY, velZ);
            }
        }
    }

    @Override
    public float getSize()
    {
        return SIZE_BASE + (storedEnergy * SIZE_MULT);
    }

    @Override
    public float getStrength()
    {
        return Math.min(1f, getSize() / 20);
    }

    @Override
    public float getRange()
    {
        return getSize() * 3;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        float s = getSize() / 2;
        return AxisAlignedBB.getBoundingBox(xCoord + 0.5 - s, yCoord + 0.5 - s, zCoord + 0.5 - s, xCoord + 0.5 + s, yCoord + 0.5 + s, zCoord + 0.5 + s);
    }

    @Override
    public double getMaxRenderDistanceSquared()
    {
        return 8192;
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass > 0;
    }

    @Override
    public boolean isGravityWell()
    {
        return true;
    }

    @Override
    public boolean showParticles()
    {
        return true;
    }

    @Override
    public void incrementEnergy(Entity entity)
    {
        if (entity instanceof EntityItem)
        {
            long energy = getEnergy() + BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(((EntityItem) entity).getEntityItem());
            setEnergy(energy);
            entity.setDead();
        }
        else if (entity instanceof EntityFallingBlock)
        {
            ItemStack block = new ItemStack(((EntityFallingBlock) entity).func_145805_f());
            setEnergy(getEnergy() + BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(block));
            entity.setDead();
        }
        else if (entity instanceof EntityDyingBlock)
        {
            ItemStack block = ((EntityDyingBlock) entity).getBlockStack();
            setEnergy(getEnergy() + BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(block));
            entity.setDead();
        }
        else
        {
            int energy = BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(entity);
            int dmg = getDamageFromSize();
            if (entity.attackEntityFrom(dmgBlackHole, dmg))
            {
                setEnergy(getEnergy() + (energy * dmg));
            }
        }
    }

    private int getDamageFromSize()
    {
        return MathHelper.floor_float(getSize() * 2);
    }

    @Override
    public long getEnergy()
    {
        return storedEnergy;
    }

    @Override
    public void setEnergy(long newEnergy)
    {
        this.storedEnergy = newEnergy;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setLong("storedEnergy", storedEnergy);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        storedEnergy = tag.getLong("storedEnergy");
    }

    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        tooltip.add("Energy: " + getEnergy());
    }
}
