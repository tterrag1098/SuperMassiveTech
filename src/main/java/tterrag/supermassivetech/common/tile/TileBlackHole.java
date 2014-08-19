package tterrag.supermassivetech.common.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import tterrag.supermassivetech.api.common.tile.IBlackHole;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateBlackHole;
import tterrag.supermassivetech.common.registry.BlackHoleEnergyRegistry;
import tterrag.supermassivetech.common.tile.abstracts.TileSMT;

public class TileBlackHole extends TileSMT implements IBlackHole
{
    private int storedEnergy = 1;
    private int lastStoredEnergy = 1;

    public TileBlackHole()
    {
        super(1, 1, 1000, 1000, c.getMinGrav());
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote && storedEnergy != lastStoredEnergy)
        {
            sendPacket();
            lastStoredEnergy = storedEnergy;
        }

        System.out.println("Strength: " + getStrength() * getStrengthMultiplier() + "  Range: " + getRange() * getRangeMultiplier());
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageUpdateBlackHole(xCoord, yCoord, zCoord, storedEnergy), worldObj.provider.dimensionId);
    }

    public float getSize()
    {
        return storedEnergy * 0.0001f + 0.2f;
    }

    @Override
    protected float getStrengthMultiplier()
    {
        return 0.5f + (getSize() * 0.1f);
    }

    @Override
    protected float getRangeMultiplier()
    {
        return 0.5f + (getSize() * 0.5f);
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

    public void incrementEnergy(Entity entity)
    {
        if (entity instanceof EntityItem)
        {
            setEnergy(getEnergy() + BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(((EntityItem) entity).getEntityItem()));
            entity.setDead();
        }
    }

    @Override
    public int getEnergy()
    {
        return storedEnergy;
    }

    @Override
    public void setEnergy(int newEnergy)
    {
        this.storedEnergy = newEnergy;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("storedEnergy", storedEnergy);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        storedEnergy = tag.getInteger("storedEnergy");
    }
}
