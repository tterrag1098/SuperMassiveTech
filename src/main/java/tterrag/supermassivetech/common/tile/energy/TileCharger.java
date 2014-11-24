package tterrag.supermassivetech.common.tile.energy;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.core.client.sound.BlockSound;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageChargerUpdate;
import tterrag.supermassivetech.common.tile.abstracts.TileSMTEnergy;
import tterrag.supermassivetech.common.util.Utils;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileCharger extends TileSMTEnergy implements ISidedInventory
{
    private boolean hadItem = false;

    private static final ResourceLocation soundLoc = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "charger.on");

    private float pitch = 0.7f, volume = 0.1f;
    private final float pitchIncr = 0.02f, volumeIncr = 0.0125f;

    @SideOnly(Side.CLIENT)
    private BlockSound sound;

    private boolean soundPlaying = false;

    public TileCharger()
    {
        super(ConfigHandler.chargerSpeed * 10);

        inventory = new ItemStack[1];

        setOutputSpeed(ConfigHandler.chargerSpeed);
        setInputSpeed(ConfigHandler.chargerSpeed * 2);
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!worldObj.isRemote)
        {
            if (inventory[0] != null && inventory[0].getItem() instanceof IEnergyContainerItem)
            {
                IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();
                int canTake = item.receiveEnergy(inventory[0], getOutputSpeed(), true);
                int canGive = storage.extractEnergy(getOutputSpeed(), true);

                if (canTake >= canGive)
                {
                    item.receiveEnergy(inventory[0], canGive, false);
                    storage.extractEnergy(canGive, false);
                }
                else
                {
                    item.receiveEnergy(inventory[0], canTake, false);
                    storage.extractEnergy(canTake, false);
                }

                if (canTake > 0 && canGive > 0)
                {
                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }

            if (inventory[0] != null != hadItem && !worldObj.isRemote)
            {
                markDirty();
                sendPacket();
                hadItem = inventory[0] != null;
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            }
        }
        else
        {
            if (isCharging())
            {
                playSound();
            }
            else
            {
                stopSound(false);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void playSound()
    {
        volume = Math.min(volume + volumeIncr, 0.2f);
        pitch = Math.min(pitch + pitchIncr, 1.0f);

        if (!soundPlaying)
        {
            if (sound != null)
            {
                sound.setDonePlaying(true);
                sound = null;
            }

            sound = new BlockSound(soundLoc).setVolume(volume).setDoRepeat(true).setLocation(xCoord + 0.5f, yCoord + 0.5f, zCoord + 0.5f);
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(sound);
        }

        sound.setVolume(volume).setPitch(pitch);

        soundPlaying = true;
    }

    @SideOnly(Side.CLIENT)
    public void stopSound(boolean force)
    {
        soundPlaying = false;
        if (sound != null)
        {
            if (volume <= 0.01f || force)
            {
                sound.setDonePlaying(true);
                sound = null;
            }
            else
            {
                volume -= volumeIncr;
                pitch -= pitchIncr;
                sound.setVolume(volume).setPitch(pitch);
            }
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (worldObj.isRemote)
        {
            stopSound(true);
        }
    }

    public boolean isCharging()
    {
        if (inventory[0] == null || !(inventory[0].getItem() instanceof IEnergyContainerItem))
        {
            return false;
        }

        IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();

        return getEnergyStored() > 0 && item.getEnergyStored(inventory[0]) < item.getMaxEnergyStored(inventory[0]);
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToAllAround(new MessageChargerUpdate(xCoord, yCoord, zCoord, getEnergyStored(), inventory[0]), getPacketRange());
    }

    private TargetPoint getPacketRange()
    {
        return new TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 5);
    }

    @Override
    public String getInventoryName()
    {
        return ModProps.LOCALIZING + ".tile.charger";
    }

    @Override
    public boolean isGravityWell()
    {
        return false;
    }

    @Override
    public boolean showParticles()
    {
        return isGravityWell();
    }

    @Override
    public EnumSet<ForgeDirection> getValidOutputs()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getValidInputs()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    /* ISidedInventory */

    @Override
    public int[] getAccessibleSlotsFromSide(int side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side)
    {
        return stack != null && slot == 0;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, int side)
    {
        return canInsertItem(slot, stack, side);
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    /* IWailaAdditionalInfo */

    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        super.getWailaInfo(tooltip, x, y, z, world);

        String str = EnumChatFormatting.WHITE + Utils.lang.localize("tooltip.itemStorage") + ": ";

        if (inventory[0] != null && inventory[0].getItem() instanceof IEnergyContainerItem)
        {
            IEnergyContainerItem item = (IEnergyContainerItem) inventory[0].getItem();
            int power = item.getEnergyStored(inventory[0]);
            str += String.format("%s / %s", Utils.formatString(Utils.getColorForPowerLeft(power, item.getMaxEnergyStored(inventory[0])).toString(), " RF", power, true, true),
                    Utils.formatString("", " RF", item.getMaxEnergyStored(inventory[0]), true, true));
        }
        else
        {
            str += EnumChatFormatting.GRAY + Utils.lang.localize("tooltip.na");
        }

        tooltip.add(str);
    }
}
