package tterrag.supermassivetech.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        
        ItemStack helm = player.inventory.armorInventory[3];
        if (helm != null && helm.getItem() instanceof ItemGravityArmor)
        {
            int width = event.resolution.getScaledWidth();

            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(compass);
            int v = getCompassAngle(player);            
            
            GL11.glColor3f(1f, 1f, 1f);

            mc.ingameGUI.drawTexturedModalRect((width - 140) / 2 + getZOffset(mc), 2 + getXOffset(mc), v - 9, 256, 140, 16);

            GL11.glColor3f(1f, 0f, 0f);            
            
            mc.ingameGUI.drawTexturedModalRect((width / 2) + getZOffset(mc), 4 + getXOffset(mc), 127, 0, 3, 9);
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
}
