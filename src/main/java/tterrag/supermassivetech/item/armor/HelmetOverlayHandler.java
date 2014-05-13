package tterrag.supermassivetech.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import tterrag.supermassivetech.block.waypoint.Waypoint;
import tterrag.supermassivetech.lib.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HelmetOverlayHandler
{
    private static final ResourceLocation compass = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/overlay/compass.png");
    
    @SubscribeEvent
    public void onClientOverlay(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;

        System.out.println(Waypoint.waypoints.size());
        
        ItemStack helm = player.inventory.armorInventory[3];
        if (helm != null && helm.getItem() instanceof ItemGravityArmor)
        {
            int width = event.resolution.getScaledWidth();

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(compass);
            int v = getCompassAngle(player);            
            
            GL11.glColor3f(1f, 1f, 1f);

            mc.ingameGUI.drawTexturedModalRect((width - 120) / 2 + getZOffset(mc), 2 + getXOffset(mc), v + 1, 256, 120, 16);

            GL11.glColor3f(1f, 0f, 0f);            
            
            mc.ingameGUI.drawTexturedModalRect((width / 2) + getZOffset(mc), 4 + getXOffset(mc), 127, 0, 2, 9);
            renderWaypoints(mc, width, player, player.posX, player.posY, player.posZ);
        }
    }

    private void renderWaypoints(Minecraft mc, int width, EntityPlayer player, double x, double y, double z)
    {
        for (Waypoint wp : Waypoint.waypoints)
        {
            double w = (double) width;
            
            double angle = angleTo(player, wp.x + 0.5, wp.y + 0.5, wp.z + 0.5);
            angle *= (w - 16) / (double) 360;
            angle += w / 2;
            
            Color c = wp.getColor();
            
            GL11.glColor3b(c.getRedByte(), c.getGreenByte(), c.getBlueByte());
            
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(normalizeAngle(w, angle) + getZOffset(mc), 3 + getXOffset(mc), 0, 16, 5, 16);
        }
    }

    public static double angleTo(EntityPlayer player, double x, double y, double z)
    {
        double dx = player.posX - x;
        double dz = player.posZ - z;
             
        double angleRaw = player.rotationYawHead + Math.toDegrees(Math.atan(dx / dz));
        
        if (dx < 0 & dz < 0)
        {
            angleRaw = angleRaw - 180;
        }
        else if (dz < 0)
        {
            angleRaw = angleRaw + 180;
        }
                
        while (angleRaw < 0)
            angleRaw += 360;
        
        return (180 + angleRaw) % 360;
    }

    private int getCompassAngle(EntityClientPlayerMP player)
    {
        int yaw = (int) player.rotationYawHead;
        yaw = (yaw - 90) % 360;
        yaw *= (256d / 360d);
        return yaw + 3; // arbitrary number to get the texture to line up...can
                        // be changed if texture changes
    }
    
    private int normalizeAngle(double width, double angle)
    {
        double normal = width - angle;
        if (normal < 0)
        {
            normal = (Math.abs(normal) + (Math.abs(normal) * (34d / width))); // I don't know why 34 don't ask me, probably int -> double inaccuracy
            normal = width - normal;
        }
        
        if (normal < (width / 2))
        {
            normal = Math.max((width / 2) - 62, normal);
        }
        else
        {
            normal = Math.min((width / 2) + 58, normal);
        }
        
        return (int) normal;
    }
    
    private int getXOffset(Minecraft mc)
    {
        return BossStatus.statusBarTime > 0 || BossStatus.bossName != null ? 18 : mc.gameSettings.showDebugInfo ? 23 : 0;
    }
    
    private int getZOffset(Minecraft mc)
    {
        return mc.gameSettings.showDebugProfilerChart ? -35 : 0;
    }
    
    public void renderTileEntityAt(TileEntityBeacon tile, double x, double y, double z, float tickDelay)
    {
        float f1 = tile.func_146002_i();
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        if (f1 > 0.0F)
        {
            Tessellator tessellator = Tessellator.instance;
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            
            OpenGlHelper.glBlendFunc(770, 1, 1, 0);
            
            float rawTime = (float)tile.getWorldObj().getTotalWorldTime() + tickDelay;
            float normalTime = -rawTime * 0.2F - (float)MathHelper.floor_float(-rawTime * 0.1F);
           
            double time = (double)rawTime * 0.025D * (1.0D - (double)(1 & 1) * 2.5D);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(255, 255, 255, 32);
            
            double d5 = (double)1 * 0.2D;
            
            double point1x = 0.5D + Math.cos(time + 2.356194490192345D) * d5;
            double point1z = 0.5D + Math.sin(time + 2.356194490192345D) * d5;
            double point2x = 0.5D + Math.cos(time + (Math.PI / 4D)) * d5;
            double point2z = 0.5D + Math.sin(time + (Math.PI / 4D)) * d5;
            double point3x = 0.5D + Math.cos(time + 3.9269908169872414D) * d5;
            double point3z = 0.5D + Math.sin(time + 3.9269908169872414D) * d5;
            double point4x = 0.5D + Math.cos(time + 5.497787143782138D) * d5;
            double point4z = 0.5D + Math.sin(time + 5.497787143782138D) * d5;
            
            double maxY = (double)(256.0F * f1);
          
            double u1 = 0.0D;
            double u2 = 1.0D;
            double v1 = (double)(-1.0F + normalTime);
            double v2 = (double)(256.0F * f1) * (0.5D / d5) + v1;
            
            tessellator.addVertexWithUV(x + point1x, y + maxY, z + point1z, u2, v2);
            tessellator.addVertexWithUV(x + point1x, y, z + point1z, u2, v1);
            tessellator.addVertexWithUV(x + point2x, y, z + point2z, u1, v1);
            tessellator.addVertexWithUV(x + point2x, y + maxY, z + point2z, u1, v2);
            tessellator.addVertexWithUV(x + point4x, y + maxY, z + point4z, u2, v2);
            tessellator.addVertexWithUV(x + point4x, y, z + point4z, u2, v1);
            tessellator.addVertexWithUV(x + point3x, y, z + point3z, u1, v1);
            tessellator.addVertexWithUV(x + point3x, y + maxY, z + point3z, u1, v2);
            tessellator.addVertexWithUV(x + point2x, y + maxY, z + point2z, u2, v2);
            tessellator.addVertexWithUV(x + point2x, y, z + point2z, u2, v1);
            tessellator.addVertexWithUV(x + point4x, y, z + point4z, u1, v1);
            tessellator.addVertexWithUV(x + point4x, y + maxY, z + point4z, u1, v2);
            tessellator.addVertexWithUV(x + point3x, y + maxY, z + point3z, u2, v2);
            tessellator.addVertexWithUV(x + point3x, y, z + point3z, u2, v1);
            tessellator.addVertexWithUV(x + point1x, y, z + point1z, u1, v1);
            tessellator.addVertexWithUV(x + point1x, y + maxY, z + point1z, u1, v2);
            tessellator.draw();
            
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            
            GL11.glDepthMask(false);
            
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA(255, 255, 255, 32);
            
            double d30 = 0.2D;
            double d4 = 0.2D;
            double d6 = 0.8D;
            double d8 = 0.2D;
            double d10 = 0.2D;
            double d12 = 0.8D;
            double d14 = 0.8D;
            double d16 = 0.8D;
            double d18 = (double)(256.0F * f1);
            double d20 = 0.0D;
            double d22 = 1.0D;
            double d24 = (double)(-1.0F + normalTime);
            double d26 = (double)(256.0F * f1) + d24;
            
            tessellator.addVertexWithUV(x + d30, y + d18, z + d4, d22, d26);
            tessellator.addVertexWithUV(x + d30, y, z + d4, d22, d24);
            tessellator.addVertexWithUV(x + d6, y, z + d8, d20, d24);
            tessellator.addVertexWithUV(x + d6, y + d18, z + d8, d20, d26);
            tessellator.addVertexWithUV(x + d14, y + d18, z + d16, d22, d26);
            tessellator.addVertexWithUV(x + d14, y, z + d16, d22, d24);
            tessellator.addVertexWithUV(x + d10, y, z + d12, d20, d24);
            tessellator.addVertexWithUV(x + d10, y + d18, z + d12, d20, d26);
            tessellator.addVertexWithUV(x + d6, y + d18, z + d8, d22, d26);
            tessellator.addVertexWithUV(x + d6, y, z + d8, d22, d24);
            tessellator.addVertexWithUV(x + d14, y, z + d16, d20, d24);
            tessellator.addVertexWithUV(x + d14, y + d18, z + d16, d20, d26);
            tessellator.addVertexWithUV(x + d10, y + d18, z + d12, d22, d26);
            tessellator.addVertexWithUV(x + d10, y, z + d12, d22, d24);
            tessellator.addVertexWithUV(x + d30, y, z + d4, d20, d24);
            tessellator.addVertexWithUV(x + d30, y + d18, z + d4, d20, d26);
            tessellator.draw();
            
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(true);
        }

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
    }
}
