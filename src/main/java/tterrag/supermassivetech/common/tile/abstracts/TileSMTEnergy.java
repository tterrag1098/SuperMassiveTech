package tterrag.supermassivetech.common.tile.abstracts;

import java.util.EnumSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageEnergyUpdate;
import tterrag.supermassivetech.common.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

import com.enderio.core.common.util.EnderStringUtils;

public abstract class TileSMTEnergy extends TileSMTInventory implements IEnergyHandler
{
    protected EnergyStorage storage;
    protected int capacity;

    private int lastStored = 0;

    public TileSMTEnergy(int cap)
    {
        super();
        init(cap);
    }

    public TileSMTEnergy(float rangeMult, float strengthMult, int cap)
    {
        super(rangeMult, strengthMult);
        init(cap);
    }

    public TileSMTEnergy(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav, int cap)
    {
        super(rangeMult, strengthMult, maxGravXZ, maxGravY, minGrav);
        init(cap);
    }

    private void init(int cap)
    {
        storage = new EnergyStorage(cap);
    }

    public abstract EnumSet<ForgeDirection> getValidOutputs();

    public abstract EnumSet<ForgeDirection> getValidInputs();

    @Override
    public void updateEntity()
    {
        super.updateEntity();
        if (!worldObj.isRemote)
        {
            pushEnergy();

            if (getEnergyStored() != lastStored)
            {
                sendPacket();
                lastStored = getEnergyStored();
            }
        }
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageEnergyUpdate(xCoord, yCoord, zCoord, getEnergyStored()), worldObj.provider.dimensionId);
    }

    protected void pushEnergy()
    {
        for (ForgeDirection dir : getValidOutputs())
        {
            TileEntity tile = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (tile instanceof IEnergyHandler)
            {
                IEnergyHandler ieh = (IEnergyHandler) tile;
                storage.extractEnergy(ieh.receiveEnergy(dir, storage.extractEnergy(getOutputSpeed(), true), false), false);
            }
        }
    }

    /* I/O Handling */

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        if (getValidOutputs().contains(from))
        {
            int ret = storage.extractEnergy(maxExtract, true);
            if (!simulate)
            {
                storage.extractEnergy(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        if (getValidInputs().contains(from))
        {
            int ret = storage.receiveEnergy(maxReceive, true);
            if (!simulate)
            {
                storage.receiveEnergy(ret, false);
            }
            return ret;
        }
        return 0;
    }

    @Override
    public final boolean canConnectEnergy(ForgeDirection from)
    {
        return getValidInputs().contains(from) || getValidOutputs().contains(from);
    }

    /* IEnergyHandler basic impl */

    @Override
    public final int getEnergyStored(ForgeDirection from)
    {
        return getEnergyStored();
    }

    @Override
    public final int getMaxEnergyStored(ForgeDirection from)
    {
        return getMaxStorage();
    }

    /* IWailaAdditionalInfo */

    @Override
    public void getWailaInfo(java.util.List<String> tooltip, int x, int y, int z, net.minecraft.world.World world)
    {
        super.getWailaInfo(tooltip, x, y, z, world);

        int energyStored = this.getEnergyStored(), maxEnergyStored = this.getMaxStorage();
        int output = this.getOutputSpeed();

        tooltip.add(EnumChatFormatting.WHITE + Utils.lang.localize("tooltip.bufferStorage") + ": " + EnderStringUtils.getColorFor(energyStored, maxEnergyStored) + energyStored
                + " RF");
        tooltip.add(EnumChatFormatting.WHITE + Utils.lang.localize("tooltip.currentOutputMax") + ": " + EnderStringUtils.getColorFor(output, this.getMaxOutputSpeed()) + output
                + " RF");
    }

    /* getters & setters */

    public int getEnergyStored()
    {
        return storage.getEnergyStored();
    }

    public void setEnergyStored(int energy)
    {
        storage.setEnergyStored(energy);
    }

    public int getMaxStorage()
    {
        return storage.getMaxEnergyStored();
    }

    public void setMaxStorage(int storage)
    {
        this.storage.setCapacity(storage);
    }

    public int getOutputSpeed()
    {
        return storage.getMaxExtract();
    }

    public int getMaxOutputSpeed()
    {
        return getOutputSpeed();
    }

    public void setOutputSpeed(int outputSpeed)
    {
        this.storage.setMaxExtract(outputSpeed);
    }

    public int getInputSpeed()
    {
        return storage.getMaxReceive();
    }

    public void setInputSpeed(int inputSpeed)
    {
        this.storage.setMaxReceive(inputSpeed);
    }

    /* Read/Write NBT */

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        storage.writeToNBT(nbt);
        super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        storage.readFromNBT(nbt);
        super.readFromNBT(nbt);
    }
}
