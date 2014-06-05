package tterrag.supermassivetech.util;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.supermassivetech.client.fx.EntityCustomFlameFX;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import tterrag.supermassivetech.handlers.ClientKeyHandler;
import tterrag.supermassivetech.handlers.ClientKeyHandler.ArmorPower;
import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.item.ItemGravityArmor.ArmorType;
import tterrag.supermassivetech.tile.TileStarHarvester;
import cpw.mods.fml.client.FMLClientHandler;

/**
 * Random utils for spawning particles and other client-only things. Mostly used
 * in packet handling.
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
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
        }
        if (settings.particleSetting == 2)
        {
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntityFlameFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
            Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySmokeFX(world, x, y, z, getRandMotionXZ(), 0.5f, getRandMotionXZ()));
        }
    }

    private static float getRandMotionXZ()
    {
        return rand.nextFloat() * 0.2f - 0.1f;
    }

    public static void renderWithIcon(WavefrontObject model, IIcon icon, Tessellator tes)
    {
        for (GroupObject go : model.groupObjects)
        {
            for (Face f : go.faces)
            {
                Vertex n = f.faceNormal;
                tes.setNormal(n.x, n.y, n.z);
                for (int i = 0; i < f.vertices.length; i++)
                {
                    Vertex v = f.vertices[i];
                    TextureCoordinate t = f.textureCoordinates[i];
                    tes.addVertexWithUV(v.x, v.y, v.z, icon.getInterpolatedU(t.u * 16), icon.getInterpolatedV(t.v * 16));
                }
            }
        }
    }

    public static MovingObjectPosition getMouseOver()
    {
        return Minecraft.getMinecraft().objectMouseOver;
    }

    public static void spawnStarHeartParticles(int x, int y, int z, double posX, double posY, double posZ)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomFlameFX(Minecraft.getMinecraft().theWorld, x + 0.5, y + 0.5, z + 0.5, posX, posY, posZ, (double) 1 / 13));
    }

    public static void spawnHopperParticle(int[] data)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, data[3] + 0.5, data[4] + 0.5, data[5] + 0.5, data[0] + 0.5,
                data[1] + 0.5, data[2] + 0.5, 0.1d));
    }

    public static void setStarHarvetserSlotContents(NBTTagCompound data, int x, int y, int z)
    {
        World world = Minecraft.getMinecraft().theWorld;

        if (world != null)
        {
            TileEntity t = world.getTileEntity(x, y, z);
            if (t != null && t instanceof TileStarHarvester)
            {
                ((TileStarHarvester) t).setInventorySlotContents(0, data == null ? null : ItemStack.loadItemStackFromNBT(data));
            }
        }
    }
    
    public static void setStarHarvesterVenting(int x, int y, int z, boolean venting)
    {
        ((TileStarHarvester) Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z)).venting = venting;
    }

    public static void spawnGravityParticle(int xCoord, int yCoord, int zCoord, double x, double y, double z)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.effectRenderer != null && mc.thePlayer != null)
        {
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, xCoord + 0.5 + x, yCoord + 0.5 + y, zCoord + 0.5 + z,
                    xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 0.05));
        }
    }

    public static String getLinesForArmor(ItemStack stack, ItemGravityArmor item)
    {
        String ret = "";
        for (ArmorPower ap : ClientKeyHandler.powers)
        {
            if (ArrayUtils.contains(ap.getArmorSlots(), (byte) item.type.ordinal()))
            {
                ret += EnumChatFormatting.WHITE + ap.getBinding().getKeyDescription() + ": " + (stack.stackTagCompound.getBoolean(ap.getPowerType().toString()) ? EnumChatFormatting.GREEN + Utils.localize("tooltip.on", true) : EnumChatFormatting.RED + Utils.localize("tooltip.off", true)) + "~";
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
                stackTagCompound.setBoolean(ap.getPowerType().toString(), true);
            }
        }
    }
}
