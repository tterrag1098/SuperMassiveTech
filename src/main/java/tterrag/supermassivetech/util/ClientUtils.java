package tterrag.supermassivetech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import tterrag.supermassivetech.client.fx.EntityCustomSmokeFX;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ClientUtils
{
    public static void spawnGravityEffectParticles(int xCoord, int yCoord, int zCoord, Entity entity, float range)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER && FMLClientHandler.instance().getClient().effectRenderer != null
                && Minecraft.getMinecraft().thePlayer != null)
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(new EntityCustomSmokeFX(Minecraft.getMinecraft().thePlayer.worldObj, entity.posX, entity.posY, entity.posZ, xCoord + 0.5,
                    yCoord + 0.5, zCoord + 0.5, 1 / range));
    }
}
