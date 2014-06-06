package tterrag.supermassivetech.client.render.entity;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL14.*;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.lib.Reference;

public class RenderFormingStar extends Render
{
    private final ResourceLocation texture = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/entity/formingStar.png");

    private int rot;
    
    private final Random rand = new Random();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float var8, float var9)
    {
        glPushMatrix();

        glTranslatef((float) x, (float) y, (float) z);

        this.bindTexture(texture);
        
        glEnable(GL_POINT_SPRITE);
        glTexEnvi(GL_POINT_SPRITE, GL_COORD_REPLACE, GL_TRUE);
        glPointSize(100);
        
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(0).put(1).put(0).put(0);
        buffer.flip();
        glPointParameter(GL_POINT_DISTANCE_ATTENUATION, buffer);

        glBegin(GL_POINTS);
        
        glVertex3d(0, 0, 0);

        glEnd();
        
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);

        Tessellator tessellator = Tessellator.instance;
        
        rand.setSeed(2983457L);

        for (int i = 0; i < 50; i++)
        {
            glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);

            glRotatef((rand.nextFloat() * 0.1f * rot) % 360, 0, 1, 0);
            
            tessellator.startDrawingQuads();

            tessellator.setBrightness(255);
            tessellator.setColorRGBA(255, 255, 100, 250);
            
            tessellator.addVertex(0, 0, 0);
            tessellator.addVertex(0, 0, 0);
            tessellator.addVertex(100, 90, 100);
            tessellator.addVertex(100, 110 , 100);
            
            tessellator.draw();
        }
        
        rot++;

        glDisable(GL_BLEND);
        glShadeModel(GL_FLAT);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_LIGHTING);
        RenderHelper.enableStandardItemLighting();

        glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity var1)
    {
        return null;
    }
}
