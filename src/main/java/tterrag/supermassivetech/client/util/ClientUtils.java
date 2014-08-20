package tterrag.supermassivetech.client.util;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.supermassivetech.client.fx.EntityCustomFlameFX;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import tterrag.supermassivetech.client.handlers.ClientKeyHandler;
import tterrag.supermassivetech.client.handlers.ClientKeyHandler.ArmorPower;
import tterrag.supermassivetech.client.handlers.ClientKeyHandler.ArmorPowerState;
import tterrag.supermassivetech.common.item.ItemGravityArmor;
import tterrag.supermassivetech.common.item.ItemGravityArmor.ArmorType;
import tterrag.supermassivetech.common.network.message.tile.MessageChargerUpdate;
import tterrag.supermassivetech.common.network.message.tile.MessageEnergyUpdate;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateBlackHole;
import tterrag.supermassivetech.common.tile.TileBlackHole;
import tterrag.supermassivetech.common.tile.abstracts.TileSMTEnergy;
import tterrag.supermassivetech.common.tile.energy.TileCharger;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Random utils for spawning particles and other client-only things. Mostly used in packet handling.
 */
public class ClientUtils
{
    private static Random rand = new Random();

    public static void spawnGravityEffectParticles(int xCoord, int yCoord, int zCoord, Entity entity, float dist)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.effectRenderer == null || mc.thePlayer == null)
            return;

        double x = entity.posX, y = entity.posY + (entity instanceof EntityPlayer ? -0.6 : (entity.height / 2)), z = entity.posZ;

        mc.effectRenderer.addEffect(new EntityCustomSmokeFX(mc.thePlayer.worldObj, x, y, z, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, dist / 35));
    }

    public static boolean calculateClientJumpState()
    {
        return Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed();
    }

    public static void spawnVentParticles(World world, float x, float y, float z, ForgeDirection top)
    {
        if (top != ForgeDirection.UP)
            return;

        GameSettings settings = Minecraft.getMinecraft().gameSettings;
        for (int i = 0; i < -((settings.particleSetting - 2) * 2); i++)
        {
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
        }
        if (settings.particleSetting == 2)
        {
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotion(), 0.5f, getRandMotion()));
        }
    }

    private static float getRandMotion()
    {
        return getRandMotion(1);
    }

    private static float getRandMotion(float mult)
    {
        return (rand.nextFloat() * 0.2f - 0.1f) * mult;
    }

    public static MovingObjectPosition getMouseOver()
    {
        return Minecraft.getMinecraft().objectMouseOver;
    }

    public static void spawnStarHeartParticles(int x, int y, int z, double posX, double posY, double posZ)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomFlameFX(Minecraft.getMinecraft().theWorld, x - 0.5, y - 0.5, z - 0.5, posX, posY, posZ,
                (double) 1 / 13));
    }

    public static void spawnHopperParticle(int... data)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, data[3] + 0.5, data[4] + 0.5, data[5] + 0.5,
                data[0] + 0.5, data[1] + 0.5, data[2] + 0.5, 0.1d));
    }

    public static void updateStarHarvester(NBTTagCompound data, int x, int y, int z, double spinSpeed)
    {
        World world = Minecraft.getMinecraft().theWorld;

        if (world != null)
        {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null && t instanceof TileStarHarvester)
            {
                ((TileStarHarvester)t).setInventorySlotContents(0, data == null ? null : ItemStack.loadItemStackFromNBT(data));
                ((TileStarHarvester)t).spinSpeed = spinSpeed;
            }
        }
    }

    public static void setStarHarvesterVenting(int x, int y, int z, boolean venting)
    {
        TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
        if (te != null && te instanceof TileStarHarvester)
        {
            ((TileStarHarvester) te).venting = venting;
        }
    }

    public static void spawnGravityParticle(int xCoord, int yCoord, int zCoord, double x, double y, double z)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.effectRenderer != null && mc.thePlayer != null)
        {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, xCoord + 0.5 + x, yCoord
                    + 0.5 + y, zCoord + 0.5 + z, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 0.05));
        }
    }

    public static String getLinesForArmor(ItemStack stack, ItemGravityArmor item)
    {
        String ret = "";
        for (ArmorPower ap : ClientKeyHandler.powers)
        {
            if (ArrayUtils.contains(ap.getArmorSlots(), (byte) item.type.ordinal()))
            {
                ArmorPowerState state = ap.getState(stack.stackTagCompound.getString(ap.getPowerType().toString()));
                ret += EnumChatFormatting.WHITE + ap.getBinding().getKeyDescription() + ": " + state.color + state.name + "~";
            }
        }
        return ret == "" ? null : ret;
    }

    public static void addAllArmorPowersTrue(NBTTagCompound stackTagCompound, ArmorType type)
    {
        for (ArmorPower ap : ClientKeyHandler.powers)
        {
            if (ArrayUtils.contains(ap.getArmorSlots(), (byte) type.ordinal()))
            {
                stackTagCompound.setString(ap.getPowerType().toString(), ap.getDefaultState().name);
            }
        }
    }

    public static void spawnConflictParticles(Entity e1, Entity e2)
    {
        double distX = e1.posX - e2.posX;
        double distY = e1.posY - e2.posY;
        double distZ = e1.posZ - e2.posZ;
        double centerX = e1.posX - distX / 2;
        double centerY = e1.posY - distY / 2;
        double centerZ = e1.posZ - distZ / 2;

        for (int i = 0; i < 25; i++)
        {
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(e1.worldObj, centerX, centerY, centerZ, getRandMotion(3), getRandMotion(3), getRandMotion(3)));

            if (i % 4 == 0)
                Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(e1.worldObj, centerX, centerY, centerZ, e2.posX, e2.posY + 1, e2.posZ, 0.1));
            else if (i % 4 == 1)
                Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(e1.worldObj, centerX, centerY, centerZ, e1.posX, e1.posY + 1, e1.posZ, 0.1));
        }
    }

    public static void updateEnergy(MessageEnergyUpdate message)
    {
        TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);
        if (te instanceof TileSMTEnergy)
        {
            ((TileSMTEnergy) te).setEnergyStored(message.stored);
        }
    }

    public static void updateCharger(MessageChargerUpdate message)
    {
        TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);

        if (te != null && te instanceof TileCharger)
        {
            ((TileCharger) te).setInventorySlotContents(0, message.item);
        }
    }

    public static void updateBlackHole(MessageUpdateBlackHole message)
    {
        TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(message.x, message.y, message.z);

        if (te != null && te instanceof TileBlackHole)
        {
            ((TileBlackHole) te).setEnergy(message.energy);
        }
    }
}
