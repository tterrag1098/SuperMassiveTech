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

import tterrag.supermassivetech.block.waypoint.Waypoint;
import tterrag.supermassivetech.lib.Reference;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HelmetOverlayHandler
{
    private static final ResourceLocation compass = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/overlay/compass.png");
    private static final ResourceLocation test = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/overlay/test.png");
    
    @SubscribeEvent
    public void onClientOverlay(RenderGameOverlayEvent.Text event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityClientPlayerMP player = mc.thePlayer;

        ItemStack helm = player.inventory.armorInventory[3];
        if (helm != null && helm.getItem() instanceof ItemGravityArmor)
        {
            int width = event.resolution.getScaledWidth();

            GL11.glColor3f(1f, 1f, 1f);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(compass);
            int v = getCompassAngle(player);
            mc.ingameGUI.drawTexturedModalRect((width - 120) / 2 - 35 * (mc.gameSettings.showDebugProfilerChart ? 1 : 0), BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? 20
                    : mc.gameSettings.showDebugInfo ? 25 : 2, v, 256, 120, 16); // TODO make helper method

            renderWaypoints(width, player, player.posX, player.posY, player.posZ);
        }
    }

    private void renderWaypoints(int width, EntityPlayer player, double x, double y, double z)
    {
        for (Waypoint wp : Waypoint.waypoints)
        {
            double w = (double) width;
            Minecraft.getMinecraft().getTextureManager().bindTexture(test);
            
            double angle = angleTo(player, wp.x + 0.5, wp.y + 0.5, wp.z + 0.5);
            angle *= (w - 16) / (double) 360;
            angle += w / 2;
            
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(normalizeAngle(w, angle),  2, 16, 16, 2, 16);
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
        if (normal > 0)
            return (int) normal;
        else
        {
            normal = (Math.abs(normal) + (Math.abs(normal) * (34d / width))); // I don't know why 34 don't ask me, probably int -> double inaccuracy
            return (int) (width - normal);
        }
    }
}
