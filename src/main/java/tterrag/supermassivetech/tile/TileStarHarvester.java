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
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.item.IStarItem;
import tterrag.supermassivetech.network.packet.PacketStarHarvester;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;

public class TileStarHarvester extends TileSMTInventory implements ISidedInventory, IEnergyHandler, IEnergyInfo
{
    private int slot = 0, perTick = 500;
    private EnergyStorage storage;
    public static final int STORAGE_CAP = 100000;
    public double spinSpeed = 0;
    public float spinRot = 0;
    public int lastLightLev;
    private boolean hasItem = false;

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
        
        if ((inventory[slot] != null) != hasItem)
        {
            sendPacket();
        }

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

    private void sendPacket()
    {
        hasItem = inventory[slot] != null;
        if (hasItem)
        {
            NBTTagCompound tag = new NBTTagCompound();
            SuperMassiveTech.channelHandler.sendToAll(new PacketStarHarvester(inventory[slot].writeToNBT(tag), xCoord, yCoord, zCoord));
        }
        else
        {
            SuperMassiveTech.channelHandler.sendToAll(new PacketStarHarvester(xCoord, yCoord, zCoord));
        }
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
        ForgeDirection f = ForgeDirection.getOrientation(getRotationMeta());
        TileEntity te = worldObj.getTileEntity(xCoord + f.offsetX, yCoord + f.offsetY, zCoord + f.offsetZ);
        if (te instanceof IEnergyHandler && !(te instanceof TileStarHarvester))
        {
            IEnergyHandler ieh = (IEnergyHandler) te;
            storage.extractEnergy(ieh.receiveEnergy(f.getOpposite(), storage.getEnergyStored() > perTick ? perTick : storage.getEnergyStored(), false), false);
        }
    }
    
    public int getRotationMeta()
    {
        return getBlockMetadata() % 6;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isGravityWell()
    {
        return inventory[slot] != null && inventory[slot].getItem() instanceof IStarItem;
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
        return from.ordinal() == getRotationMeta();
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
        
        if (player.isSneaking())
        {
            return printInfo(player);
        }
        else if (stack != null)
        {
            if (stack.getItem() == SuperMassiveTech.itemRegistry.starContainer)
            {
                if (getBlockMetadata() == getRotationMeta())
                {
                    worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getBlockMetadata() + 6, 3);
                    player.getCurrentEquippedItem().stackSize--;
                    return true;
                }
            }
            else if (stack.getItem() instanceof IStarItem)
            {
                if (inventory[slot] == null)
                {
                    insertStar(stack, player);
                    return true;
                }
            }
            return inventory[slot] == null ? printInfo(player) : extractStar(player);
        }
        else if (inventory[slot] != null)
        {
            return extractStar(player);
        }
        
        return printInfo(player);
    }
    
    private boolean insertStar(ItemStack stack, EntityPlayer player)
    {
        ItemStack insert = stack.copy();
        insert.stackSize = 1;
        inventory[slot] = insert;
        player.getCurrentEquippedItem().stackSize--;
        return true;
    }
    
    private boolean extractStar(EntityPlayer player)
    {
        if (!player.inventory.addItemStackToInventory(inventory[slot]))
            player.worldObj.spawnEntityInWorld(new EntityItemIndestructible(player.worldObj, player.posX, player.posY, player.posZ, inventory[slot], 0, 0, 0, 0));

        inventory[slot] = null;
        return true;
    }
    
    private boolean printInfo(EntityPlayer player)
    {
        if (player.worldObj.isRemote) return true;
        
        IStar star = Utils.getType(inventory[slot]);

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "------------------------------"));
        
        if (getBlockMetadata() == getRotationMeta())
        {
            player.addChatComponentMessage(new ChatComponentText("No Container Installed!"));
        }
        else if (star != null)
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
        
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int var1)
    {
        return var1 == 1 && inventory[slot] == null ? new int[] { slot } : new int[] {};
    }

    @Override
    public boolean canInsertItem(int var1, ItemStack var2, int var3)
    {
        return var1 == slot && var3 == ForgeDirection.OPPOSITES[getRotationMeta()] && var2 != null ? var2.getItem() instanceof IStarItem : false;
    }

    @Override
    public boolean canExtractItem(int var1, ItemStack var2, int var3)
    {
        return var1 == slot && var3 == getRotationMeta();
    }
    
    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return var1 == slot && inventory[var1] == null && var2 != null && var2.getItem() instanceof IStarItem;
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
