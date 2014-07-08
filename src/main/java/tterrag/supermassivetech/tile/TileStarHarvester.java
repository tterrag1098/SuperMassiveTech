package tterrag.supermassivetech.tile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.item.IStarItem;
import tterrag.supermassivetech.network.PacketHandler;
import tterrag.supermassivetech.network.message.MessageStarHarvester;
import tterrag.supermassivetech.network.message.MessageUpdateVenting;
import tterrag.supermassivetech.registry.Achievements;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.util.ClientUtils;
import tterrag.supermassivetech.util.Utils;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;

public class TileStarHarvester extends TileSMTInventory implements ISidedInventory, IEnergyHandler
{
    private int slot = 0;

    public final int maxPerTick = 500;
    private int perTick = maxPerTick;
    private EnergyStorage storage;
    public static final int STORAGE_CAP = 100000;
    public double spinSpeed = 0;
    public float[] spins = { 0, 0, 0, 0 };
    private boolean hasItem = false;
    private boolean needsLightingUpdate = false;
    public boolean venting = false;
    private ForgeDirection top = ForgeDirection.UNKNOWN;

    public TileStarHarvester()
    {
        super(1.0f, 0.5f);
        storage = new EnergyStorage(STORAGE_CAP);
        inventory = new ItemStack[1];
    }

    @Override
    public void updateEntity()
    {
        if (top == ForgeDirection.UNKNOWN)
        {
            top = ForgeDirection.getOrientation(getRotationMeta()).getOpposite();
        }

        if (venting)
        {
            if (!worldObj.getBlock(xCoord, yCoord + 1, zCoord).isAir(worldObj, xCoord, yCoord + 1, zCoord))
            {
                venting = false;
            }

            AxisAlignedBB axis = AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 7, zCoord + 1);

            @SuppressWarnings("unchecked")
            List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axis);

            for (EntityLivingBase e : entities)
            {
                e.setFire(5);
            }
        }

        super.updateEntity();

        if (needsLightingUpdate)
        {
            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
            needsLightingUpdate = false;
        }

        if ((inventory[slot] != null) != hasItem)
        {
            sendPacket();
        }

        perTick = (int) (maxPerTick * spinSpeed);

        if (inventory[slot] != null && inventory[slot].stackTagCompound != null)
        {
            IStar type = Utils.getType(inventory[slot]);
            int energy = type.getPowerStored(inventory[slot]);
            int max = type.getPowerPerTick() * 2;
            inventory[slot].getTagCompound().setInteger("energy", venting ? energy - max : energy - storage.receiveEnergy(energy > max ? max : energy, false));
        }

        attemptOutputEnergy();
        attemptOutputEnergy();

        if (venting && worldObj.isRemote)
            ClientUtils.spawnVentParticles(worldObj, xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f, top);

        updateAnimation();
    }

    private void sendPacket()
    {
        hasItem = inventory[slot] != null;
        if (hasItem)
        {
            NBTTagCompound tag = new NBTTagCompound();
            PacketHandler.INSTANCE.sendToAll(new MessageStarHarvester(inventory[slot].writeToNBT(tag), xCoord, yCoord, zCoord));
        }
        else
        {
            PacketHandler.INSTANCE.sendToAll(new MessageStarHarvester(xCoord, yCoord, zCoord));
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
            spinSpeed = spinSpeed <= 0 ? 0 : spinSpeed - 0.01;
        }

        for (int i = 0; i < spins.length; i++)
        {
            if (spins[i] >= 360)
                spins[i] -= 360;

            spins[i] += (float) (i == 0 ? spinSpeed * 15f : spinSpeed * (6f + i * 2));
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
    public boolean canConnectEnergy(ForgeDirection from)
    {
        return from.ordinal() == getRotationMeta();
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return storage.getEnergyStored();
    }
    
    public int getEnergyStored()
    {
        return getEnergyStored(ForgeDirection.UNKNOWN);
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) 
    {
        return STORAGE_CAP;
    }
    
    public int getMaxEnergyStored()
    {
        return getMaxEnergyStored(ForgeDirection.UNKNOWN);
    }

    public boolean handleRightClick(EntityPlayer player, ForgeDirection side)
    {
        ItemStack stack = player.getCurrentEquippedItem();

        if (stack != null)
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
                if (inventory[slot] == null && getBlockMetadata() != getRotationMeta())
                {
                    return insertStar(stack, player);
                }
            }
            else if (isUpright(side) && player.isSneaking())
                return vent();
        }
        else if (inventory[slot] != null)
        {
            if (player.isSneaking())
            {
                if (isUpright(side))
                    return vent();
                else
                    return extractStar(player);
            }
        }
        else if (getBlockMetadata() != getRotationMeta())
        {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, getBlockMetadata() - 6, 3);
            player.inventory.addItemStackToInventory(new ItemStack(SuperMassiveTech.itemRegistry.starContainer));
            return true;
        }

        return printInfo(player);
    }

    private boolean isUpright(ForgeDirection side)
    {
        return getRotationMeta() == 0 && side == top;
    }

    public int getCurrentOutputMax()
    {
        return perTick;
    }

    private boolean vent()
    {
        this.venting = !venting;

        if (!worldObj.isRemote)
            updateClientVenting(venting);

        return true;
    }

    private void updateClientVenting(boolean is)
    {
        PacketHandler.INSTANCE.sendToAll(new MessageUpdateVenting(xCoord, yCoord, zCoord, is));
    }

    private boolean insertStar(ItemStack stack, EntityPlayer player)
    {
        ItemStack insert = stack.copy();
        insert.stackSize = 1;
        inventory[slot] = insert;
        player.getCurrentEquippedItem().stackSize--;
        needsLightingUpdate = true;

        if (!player.worldObj.isRemote)
        {
            ItemStack unlock = stack.copy();
            unlock.setItemDamage(1);
            Achievements.unlock(Achievements.getValidItemStack(unlock), (EntityPlayerMP) player);
        }

        return true;
    }

    private boolean extractStar(EntityPlayer player)
    {
        if (!player.inventory.addItemStackToInventory(inventory[slot]))
            player.worldObj.spawnEntityInWorld(new EntityItemIndestructible(player.worldObj, player.posX, player.posY, player.posZ, inventory[slot], 0, 0, 0, 0));

        inventory[slot] = null;
        needsLightingUpdate = true;
        venting = false;
        return true;
    }

    private boolean printInfo(EntityPlayer player)
    {
        if (player.worldObj.isRemote)
            return true;

        IStar star = Utils.getType(inventory[slot]);

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "-------------------------------------"));

        if (getBlockMetadata() == getRotationMeta())
        {
            player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + Utils.localize("tooltip.noContainerInPlace", true)));
        }
        else if (star != null)
        {
            player.addChatMessage(new ChatComponentText(String.format(EnumChatFormatting.BLUE + "%s: %s" + EnumChatFormatting.WHITE + " %s %d RF/t",
                    Utils.localize("tooltip.currentStarIs", true), star.getTextColor() + star.toString(), Utils.localize("tooltip.at", true), star.getPowerPerTick())));
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.localize("tooltip.powerRemaining", true) + ": "
                    + Utils.getColorForPowerLeft(star.getPowerStored(inventory[slot]), star.getPowerStoredMax())
                    + Utils.formatString("", " RF", inventory[slot].getTagCompound().getInteger("energy"), true, true)));
        }
        else
        {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + Utils.localize("tooltip.noStarInPlace", true)));
        }

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.localize("tooltip.bufferStorage", true) + ": "
                + Utils.getColorForPowerLeft(storage.getEnergyStored(), storage.getMaxEnergyStored()) + Utils.formatString("", " RF", storage.getEnergyStored(), true, true)));
        player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.localize("tooltip.currentOutputMax", true) + ": "
                + Utils.getColorForPowerLeft(perTick, maxPerTick) + Utils.formatString("", " RF/t", perTick, false)));

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

        storage.readFromNBT(nbt.getCompoundTag("energyStorage"));

        spinSpeed = nbt.getDouble("spin");

        NBTTagList list = nbt.getTagList("spins", 5);
        for (int i = 0; i < list.tagCount(); i++)
        {
            spins[i] = list.func_150308_e(i);
        }

        venting = nbt.getBoolean("venting");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagCompound tag = new NBTTagCompound();
        storage.writeToNBT(tag);
        nbt.setTag("energyStorage", tag);

        nbt.setDouble("spin", spinSpeed);

        NBTTagList spinAngles = new NBTTagList();
        for (Float f : spins)
        {
            spinAngles.appendTag(new NBTTagFloat(f));
        }

        nbt.setTag("spins", spinAngles);

        nbt.setBoolean("venting", venting);
    }
}
