package tterrag.supermassivetech.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

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

            GL11.glColor3f(1f, 1f, 1f);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
            mc.getTextureManager().bindTexture(compass);
            int v = getCompassAngle(player);
            mc.ingameGUI.drawTexturedModalRect((width - 120) / 2, BossStatus.bossName != null && BossStatus.statusBarTime > 0 ? 20 : 2, v, 256, 120, 16);
        }
    }
    
    private int getCompassAngle(EntityClientPlayerMP player)
    {
        int yaw = (int) player.rotationYawHead;
        yaw = (yaw - 90) % 360;
        yaw *= (256d / 360d);
        return yaw + 3; // arbitrary number to get the texture to line up...can be changed if texture changes
    }
}
