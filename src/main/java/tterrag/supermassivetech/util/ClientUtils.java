package tterrag.supermassivetech.util;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientUtils
{
    private static Random rand = new Random();
    
    public static void spawnGravityEffectParticles(int xCoord, int yCoord, int zCoord, Entity entity, float range)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && FMLClientHandler.instance().getClient().effectRenderer != null && Minecraft.getMinecraft().thePlayer != null)
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, entity.posX, entity.posY, entity.posZ, xCoord + 0.5,
                    yCoord + 0.5, zCoord + 0.5, 1 / range));
    }

    public static boolean calculateClientJumpState()
    {
        return Minecraft.getMinecraft().gameSettings.keyBindJump.getIsKeyPressed();
    }

    public static void spawnVentParticles(World world, float x, float y, float z, ForgeDirection top)
    {
        if (top != ForgeDirection.UP) return;
        
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
        for(GroupObject go : model.groupObjects)
        {
            for(Face f : go.faces) {
                Vertex n = f.faceNormal;
                tes.setNormal(n.x, n.y, n.z);
                for(int i = 0; i < f.vertices.length; i++) 
                {
                    Vertex v = f.vertices[i];
                    TextureCoordinate t = f.textureCoordinates[i];
                    tes.addVertexWithUV(v.x, v.y, v.z,
                        icon.getInterpolatedU(t.u * 16),
                        icon.getInterpolatedV(t.v * 16));
                }
            }
        }
    }
}
