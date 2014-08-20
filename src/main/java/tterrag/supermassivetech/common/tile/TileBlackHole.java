package tterrag.supermassivetech.common.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import tterrag.supermassivetech.api.common.tile.IBlackHole;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateBlackHole;
import tterrag.supermassivetech.common.registry.BlackHoleEnergyRegistry;
import tterrag.supermassivetech.common.tile.abstracts.TileSMT;
import tterrag.supermassivetech.common.util.Constants;
import tterrag.supermassivetech.common.util.Utils;

public class TileBlackHole extends TileSMT implements IBlackHole
{
    private long storedEnergy = 1;
    private long lastStoredEnergy = 1;
    
    private static final float SIZE_MULT = 1f / 1000000f;
    private static final float SIZE_BASE = 0.5f;
    
    private static final DamageSource dmgBlackHole = new DamageSource("dmg.blackHole")
    {
        public boolean isDamageAbsolute() {return true;}
        
        public IChatComponent func_151519_b(EntityLivingBase entity)
        {
            if (entity instanceof EntityPlayer)
            {
                String text = String.format(Utils.localize("death.blackHole", true), ((EntityPlayer)entity).getCommandSenderName());
                return new ChatComponentText(text);
            }
            else
            {
                return null;
            }
        }
    };

    public TileBlackHole()
    {
        super(1, 1, 1000, 1000, Constants.instance().getMinGrav());
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

//        System.out.println("Energy: " + getEnergy() + "  Size: " + getSize() + "  Strength: " + getStrength() * getStrengthMultiplier() + "  Range: " + getRange() * getRangeMultiplier());
    }

    private void sendPacket()
    {
        PacketHandler.INSTANCE.sendToDimension(new MessageUpdateBlackHole(xCoord, yCoord, zCoord, storedEnergy), worldObj.provider.dimensionId);
    }

    public float getSize()
    {
        return SIZE_BASE + (storedEnergy * SIZE_MULT);
    }

    @Override
    public float getStrength()
    {
        return getSize() / 40;
    }

    @Override
    public float getRange()
    {
        return getSize() * 2;
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
            long energy = getEnergy() + BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(((EntityItem) entity).getEntityItem());
            setEnergy(energy);
            entity.setDead();
        }
        else
        {
            int energy = BlackHoleEnergyRegistry.INSTANCE.getEnergyFor(entity);
            int dmg = getDamageFromSize();
            System.out.println(dmg);
            setEnergy(getEnergy() + (energy * dmg));
            entity.attackEntityFrom(dmgBlackHole, dmg);
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
}
