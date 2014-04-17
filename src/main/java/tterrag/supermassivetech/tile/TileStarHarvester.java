package tterrag.supermassivetech.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileStarHarvester extends TileSMTInventory implements ISidedInventory, IEnergyHandler, IEnergyInfo
{
    private int slot = 0, perTick = 500;
    private EnergyStorage storage;
    private final int STORAGE_CAP = 100000;
    public double spinSpeed = 0;
    public float spinRot = 0;
    public int lastLightLev;

    public TileStarHarvester()
    {
        super(1.0f, 0.5f);
        storage = new EnergyStorage(STORAGE_CAP);
        inventory = new ItemStack[1];
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        perTick = (int) (500 * spinSpeed);

        if (inventory[slot] != null && inventory[slot].stackTagCompound != null)
        {
            IStar type = Utils.getType(inventory[slot]);
            int energy = type.getPowerStored(inventory[slot]);
            int max = type.getPowerPerTick() * 2;
            inventory[slot].getTagCompound().setInteger("energy", energy - storage.receiveEnergy(energy > max ? max : energy, false));
        }

        attemptOutputEnergy();
        attemptOutputEnergy();

        updateAnimation();
    }

    private void updateAnimation()
    {
        if (isGravityWell())
        {
            spinSpeed = spinSpeed >= 1 ? 1 : spinSpeed + 0.0005;
        }
        else
        {
            spinSpeed = spinSpeed <= 0 ? 0 : spinSpeed - 0.0005;
        }

        if (spinRot >= 360)
            spinRot -= 360;

        spinRot += (float) (spinSpeed * 30f);

        int light = (int) (spinSpeed * 15);
        if (light != lastLightLev)
        {
            lastLightLev = light;
            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
        }
    }

    private void attemptOutputEnergy()
    {
        ForgeDirection f = ForgeDirection.getOrientation(getBlockMetadata());
        TileEntity te = worldObj.getTileEntity(xCoord + f.offsetX, yCoord + f.offsetY, zCoord + f.offsetZ);
        if (te instanceof IEnergyHandler && !(te instanceof TileStarHarvester))
        {
            IEnergyHandler ieh = (IEnergyHandler) te;
            storage.extractEnergy(ieh.receiveEnergy(f.getOpposite(), storage.getEnergyStored() > perTick ? perTick : storage.getEnergyStored(), false), false);
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isGravityWell()
    {
        return inventory[slot] != null && inventory[slot].getItem() instanceof ItemStar;
    }

    @Override
    public boolean showParticles()
    {
        return true;
    }

    @Override
    public String getInventoryName()
    {
        return "tterrag.inventory.starHarvester";
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return 0;
    }

    public void setEnergyStored(int energy)
    {
        storage.setEnergyStored(energy);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public boolean canInterface(ForgeDirection from)
    {
        return from.ordinal() == getBlockMetadata();
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return STORAGE_CAP;
    }

    @Override
    public int getEnergyPerTick()
    {
        return getMaxEnergyPerTick();
    }

    @Override
    public int getMaxEnergyPerTick()
    {
        return ((IStar) inventory[slot].getItem()).getPowerPerTick();
    }

    @Override
    public int getEnergy()
    {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergy()
    {
        return STORAGE_CAP;
    }

    public boolean handleRightClick(EntityPlayer player)
    {
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() instanceof ItemStar && inventory[slot] == null)
        {
            ItemStack insert = stack.copy();
            insert.stackSize = 1;
            inventory[slot] = insert;
            player.getCurrentEquippedItem().stackSize--;
            return true;
        }
        else if (!player.isSneaking() && inventory[slot] != null)
        {
            if (!player.inventory.addItemStackToInventory(inventory[slot]))
                player.worldObj.spawnEntityInWorld(new EntityItemIndestructible(player.worldObj, player.posX, player.posY, player.posZ, inventory[slot], 0, 0, 0, 0));

            inventory[slot] = null;
            return true;
        }
        else if (!player.worldObj.isRemote)
        {
            IStar star = Utils.getType(inventory[slot]);

            player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "------------------------------"));
            if (star != null)
            {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Current star is: " + star.getTextColor() + star.toString()));
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Energy remaining: " + Utils.getColorForPowerLeft(star.getPowerStored(inventory[slot]), star.getPowerStoredMax())
                        + Utils.formatString("", " RF", inventory[slot].getTagCompound().getInteger("energy"), true, true)));
            }
            else
            {
                player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "No star in place!"));
            }

            player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Buffer Storage: " + Utils.getColorForPowerLeft(storage.getEnergyStored(), storage.getMaxEnergyStored())
                    + Utils.formatString("", " RF", storage.getEnergyStored(), true, true)));
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + "Current Output Max: " + Utils.getColorForPowerLeft(perTick, 500) + Utils.formatString("", " RF/t", perTick, false)));

            return false;
        }
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return var1 == 1 ? new int[] { 0 } : new int[] {};
    }

    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3)
    {
        return var1 == 0 && var3 == 1 && var2 != null ? var2.getItem() instanceof ItemStar : false;
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3)
    {
        return var1 == 0 && var3 == 1;
    }
    
    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return var1 == slot && var2 != null && var2.getItem() instanceof ItemStar;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        spinSpeed = nbt.getDouble("spin");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setDouble("spin", spinSpeed);
    }
}
