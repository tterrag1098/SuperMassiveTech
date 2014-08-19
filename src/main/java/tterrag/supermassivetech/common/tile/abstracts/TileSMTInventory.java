package tterrag.supermassivetech.common.tile.abstracts;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import tterrag.supermassivetech.api.common.compat.IWailaAdditionalInfo;
import tterrag.supermassivetech.common.util.Utils;

public abstract class TileSMTInventory extends TileSMT implements IInventory, IWailaAdditionalInfo
{
    protected ItemStack[] inventory;

    /**
     * Sets the tile with all default constant values
     */
    public TileSMTInventory()
    {
        this(1, 1, c.getMaxGravXZ(), c.getMaxGravY(), c.getMinGrav());
    }

    /**
     * Sets the tile with the passed range muliplier and strength multiplier
     */
    public TileSMTInventory(float rangeMult, float strengthMult)
    {
        this(rangeMult, strengthMult, c.getMaxGravXZ(), c.getMaxGravY(), c.getMinGrav());
    }

    /**
     * Fully customizeable constructor, takes in all gravity parameters
     * 
     * @param rangeMult - mulitplier for range, base is set in config
     * @param strengthMult - multiplier for strength, base is set in config
     * @param maxGravXZ - max gravity that can be applied in the X and Z
     *            directions
     * @param maxGravY - max gravity that can be applied in the Y direction
     * @param minGrav - minimum gravity to be applied, prevents "bouncing"
     */
    public TileSMTInventory(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav)
    {
        super(rangeMult, strengthMult, maxGravXZ, maxGravY, minGrav);
        
        inventory = new ItemStack[0];
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack decrStackSize(int i, int j)
    {
        if (this.inventory[i] != null)
        {
            ItemStack itemstack;
            if (this.inventory[i].stackSize <= j)
            {
                itemstack = this.inventory[i];
                this.inventory[i] = null;
                return itemstack;
            }
            else
            {
                itemstack = this.inventory[i].splitStack(j);
                if (this.inventory[i].stackSize == 0)
                {
                    this.inventory[i] = null;
                }
                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return var1 < inventory.length ? inventory[var1] : null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i)
    {
        if (this.inventory[i] != null)
        {
            ItemStack itemstack = this.inventory[i];
            this.inventory[i] = null;
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemstack)
    {
        this.inventory[i] = itemstack;
        if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
        {
            itemstack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public boolean hasCustomInventoryName()
    {
        return false;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1)
    {
        return true;
    }

    @Override
    public void openInventory()
    {
        // Do nothing
    }

    @Override
    public void closeInventory()
    {
        // Do nothing
    }

    @Override
    public boolean isItemValidForSlot(int var1, ItemStack var2)
    {
        return var1 < inventory.length;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
        nbt.setTag("Items", nbttaglist);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        NBTTagList nbttaglist = nbt.getTagList("Items", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
    }

    @Override
    public Packet getDescriptionPacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        this.readFromNBT(pkt.func_148857_g());
    }
    
    @Override
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world)
    {
        tooltip.add(EnumChatFormatting.WHITE + Utils.localize("tooltip.gravityWell", true) + " " + (this.isGravityWell() ? EnumChatFormatting.GREEN + Utils.localize("tooltip.yes", true) : EnumChatFormatting.RED + Utils.localize("tooltip.no", true)));
    }
}
