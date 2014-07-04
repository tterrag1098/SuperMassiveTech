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
import tterrag.supermassivetech.handlers.ClientKeyHandler.ArmorPowerState;
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
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomFlameFX(Minecraft.getMinecraft().theWorld, x - 0.5, y - 0.5, z - 0.5, posX, posY, posZ,
                (double) 1 / 13));
    }

    public static void spawnHopperParticle(int... data)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, data[3] + 0.5, data[4] + 0.5, data[5] + 0.5,
                data[0] + 0.5, data[1] + 0.5, data[2] + 0.5, 0.1d));
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
}
