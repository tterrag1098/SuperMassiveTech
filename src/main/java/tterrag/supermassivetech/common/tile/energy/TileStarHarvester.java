package tterrag.supermassivetech.common.tile.energy;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.api.common.item.IStarItem;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.client.util.ClientUtils;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.common.item.ItemStarSpecial;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageStarHarvester;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateVenting;
import tterrag.supermassivetech.common.registry.Achievements;
import tterrag.supermassivetech.common.registry.Stars;
import tterrag.supermassivetech.common.tile.abstracts.TileSMTEnergy;
import tterrag.supermassivetech.common.util.Utils;

import com.enderio.core.common.util.EnderStringUtils;

public class TileStarHarvester extends TileSMTEnergy implements ISidedInventory, IWailaAdditionalInfo
{
    private int slot = 0;

    public static final int MAX_PER_TICK;
    static
    {
        int max = 0;
        for (IStar type : Stars.instance.getTypes())
        {
            max = Math.max(type.getPowerPerTick(), max);
        }
        MAX_PER_TICK = max * 2; // max output of star harvester will be max output of any star type times 2
    }

    private int perTick = 0;
    public double spinSpeed = 0, lastSpinSpeed = 0;
    public float[] spins = { 0, 0, 0, 0 };
    private boolean hasItem = false;
    private boolean needsLightingUpdate = false;
    public boolean venting = false;
    public boolean dying = false;
    private ForgeDirection top = ForgeDirection.UNKNOWN;

    public static final int MAX_ENERGY = 100000;

    public TileStarHarvester()
    {
        super(1.0f, 0.5f, MAX_ENERGY);
        inventory = new ItemStack[1];
    }

    @Override
    public void updateEntity()
    {
        if (top == ForgeDirection.UNKNOWN)
        {
            top = ForgeDirection.getOrientation(getRotationMeta()).getOpposite();
        }

        super.updateEntity();
        updateAnimation(); // update animation on both sides as this controls spinSpeed, and thus perTick

        if (!worldObj.isRemote)
        {
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

            if (needsLightingUpdate)
            {
                worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                needsLightingUpdate = false;
            }

            if ((inventory[slot] != null) != hasItem || spinSpeed != lastSpinSpeed) // energy change is handled in super
            {
                sendPacket();
                hasItem = inventory[slot] != null;
                lastSpinSpeed = spinSpeed;
            }

            if (inventory[slot] != null && inventory[slot].stackTagCompound != null)
            {
                IStar type = Utils.getType(inventory[slot]);
                int amnt = type.getPowerPerTick();
                int takenFromStar = type.extractEnergy(inventory[slot],
                        Math.min(amnt, venting ? amnt : storage.getMaxEnergyStored() - storage.getEnergyStored()), false);

                if (dying = type.getEnergyStored(inventory[slot]) <= (type.getMaxEnergyStored(inventory[slot]) * ConfigHandler.starDeathTrigger))
                    Utils.setStarFuseRemaining(inventory[slot], Utils.getStarFuseRemaining(inventory[slot]) - 1);

                if (Utils.getStarFuseRemaining(inventory[slot]) <= 0)
                    collapse();

                if (!venting) // remove energy from star but don't add to
                              // internal storage if venting
                {
                    storage.receiveEnergy(takenFromStar, false);
                }
            }
        }

        perTick = (int) (MAX_PER_TICK * spinSpeed);
        if (venting && worldObj.isRemote)
            ClientUtils.spawnVentParticles(worldObj, xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f, top);
        if (dying && worldObj.isRemote)
            ClientUtils.spawnDyingParticles(worldObj, xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
    }

    private void collapse()
    {
        if (Utils.shouldSpawnBlackHole(worldObj))
        {
            worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, SuperMassiveTech.blockRegistry.blackHole);
            explode(4.0f);
        }
        else
        {
            this.insertStar(Utils.setType(new ItemStack(SuperMassiveTech.itemRegistry.starSpecial), ItemStarSpecial.getRandomType(worldObj.rand)), null);
            explode(2.0f);
        }
    }

    private void explode(float str)
    {
        worldObj.newExplosion(null, xCoord + 0.5, yCoord - 0.5, zCoord + 0.5, str, true, true);
    }

    @Override
    protected void pushEnergy()
    {
        super.pushEnergy();
    }

    private void sendPacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        PacketHandler.INSTANCE.sendToDimension(new MessageStarHarvester(inventory[slot] == null ? null : inventory[slot].writeToNBT(tag), xCoord, yCoord, zCoord, spinSpeed,
                dying), worldObj.provider.dimensionId);
    }

    private void updateAnimation()
    {
        if (!worldObj.isRemote) // do not change spin speed on client, allow packet sync
        {
            int max = getMaxSpinSpeed();
            if (isGravityWell() && spinSpeed < max)
            {
                spinSpeed += 0.0005 + (dying ? 0.01 : 0);
            }
            else if (!isGravityWell() || spinSpeed > max + 0.02)
            {
                spinSpeed = spinSpeed <= 0 ? 0 : spinSpeed - 0.01;
            }
        }

        for (int i = 0; i < spins.length; i++)
        {
            if (spins[i] >= 360)
                spins[i] -= 360;

            spins[i] += (float) (i == 0 ? spinSpeed * 15f : spinSpeed * (6f + i * 2));
        }
    }

    private int getMaxSpinSpeed()
    {
        return dying ? 4 : 1;
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
    public EnumSet<ForgeDirection> getValidInputs()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getValidOutputs()
    {
        return EnumSet.of(ForgeDirection.VALID_DIRECTIONS[getRotationMeta()]);
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

    @Override
    public int getOutputSpeed()
    {
        return perTick;
    }

    @Override
    public int getMaxOutputSpeed()
    {
        return MAX_PER_TICK;
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
        needsLightingUpdate = true;

        if (player != null)
        {
            player.getCurrentEquippedItem().stackSize--;
            if (!player.worldObj.isRemote)
            {
                ItemStack unlock = stack.copy();
                unlock.setItemDamage(1);
                Achievements.unlock(Achievements.getValidItemStack(unlock), (EntityPlayerMP) player);
            }
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
        dying = false;
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
            player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + Utils.lang.localize("tooltip.noContainerInPlace")));
        }
        else if (star != null)
        {
            player.addChatMessage(new ChatComponentText(String.format(EnumChatFormatting.BLUE + "%s: %s" + EnumChatFormatting.WHITE + " %s %d RF/t",
                    Utils.lang.localize("tooltip.currentStarIs"), star.getTextColor() + star.toString(), Utils.lang.localize("tooltip.at"), star.getPowerPerTick())));
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.lang.localize("tooltip.powerRemaining") + ": "
                    + EnderStringUtils.getColorFor(star.getEnergyStored(inventory[slot]), star.getMaxEnergyStored(inventory[slot]))
                    + Utils.formatStringForBHS("", " RF", inventory[slot].getTagCompound().getInteger("energy"), true, true)));
        }
        else
        {
            player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + Utils.lang.localize("tooltip.noStarInPlace")));
        }

        player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.lang.localize("tooltip.bufferStorage") + ": "
                + EnderStringUtils.getColorFor(storage.getEnergyStored(), storage.getMaxEnergyStored()) + Utils.formatStringForBHS("", " RF", storage.getEnergyStored(), true, true)));
        player.addChatMessage(new ChatComponentText(EnumChatFormatting.BLUE + Utils.lang.localize("tooltip.currentOutputMax") + ": "
                + EnderStringUtils.getColorFor(perTick, MAX_PER_TICK) + EnderStringUtils.formatString("", " RF/t", perTick, false)));

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

    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        super.getWailaInfo(tooltip, x, y, z, world);

        if (this.getBlockMetadata() < 5)
        {
            tooltip.add("" + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + Utils.lang.localize("tooltip.noContainerInPlace"));
        }
        else if (!this.isGravityWell())
        {
            tooltip.add("" + EnumChatFormatting.RED + EnumChatFormatting.ITALIC + Utils.lang.localize("tooltip.noStarInPlace"));
        }
    }
}
