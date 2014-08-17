package tterrag.supermassivetech.client.util;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.model.obj.Face;
import net.minecraftforge.client.model.obj.GroupObject;
import net.minecraftforge.client.model.obj.TextureCoordinate;
import net.minecraftforge.client.model.obj.Vertex;
import net.minecraftforge.client.model.obj.WavefrontObject;
import tterrag.supermassivetech.client.handlers.ClientRenderingHandler;

public class RenderingUtils
{
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
    
    public static void render3DItem(EntityItem item, float partialTickTime, boolean rotate)
    {
        float rot = -(Minecraft.getMinecraft().theWorld.getTotalWorldTime() + partialTickTime) % 360 * 2;

        glPushMatrix();
        glDepthMask(true);
        rotate &= Minecraft.getMinecraft().gameSettings.fancyGraphics;
        
        if (rotate)
        {
            glRotatef(rot, 0, 1, 0);
        }
        
        item.hoverStart = 0.0F;
        RenderManager.instance.renderEntityWithPosYaw(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

        glPopMatrix();
    }
    
    public static float getRotation(float partialTick, float mult)
    {
        return ClientRenderingHandler.getElapsed() * mult;
    }
    
    public static void renderBillboardQuad(float rot, double scale)
    {
        glPushMatrix();
        
        glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

        glPushMatrix();        
        
        glRotatef(rot, 0, 0, 1);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        glColor3f(1, 1, 1);
        tessellator.setColorRGBA(255, 255, 255, 255);
        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0);
        tessellator.addVertexWithUV(-scale, scale, 0, 0, 1);
        tessellator.addVertexWithUV(scale, scale, 0, 1, 1);
        tessellator.addVertexWithUV(scale, -scale, 0, 1, 0);
        tessellator.draw();
        glPopMatrix();
        glPopMatrix();
    }
}
