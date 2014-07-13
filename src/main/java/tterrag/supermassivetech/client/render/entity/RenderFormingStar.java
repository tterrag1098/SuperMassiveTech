package tterrag.supermassivetech.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.util.ClientUtils;

public class RenderFormingStar extends Render
{
    private float rot;

    private ItemStack stack;
    private EntityItem item;

    private final Random rand = new Random();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float var8, float var9)
    {
        if (stack == null)
        {
            stack = new ItemStack(SuperMassiveTech.itemRegistry.heartOfStar);
            item = new EntityItem(Minecraft.getMinecraft().theWorld, x, y, z, stack);
        }

        rot = rot > 1000 ? 0 : rot;

        item.setPosition(entity.posX, entity.posY, entity.posZ);

        glTranslatef((float) x, (float) y, (float) z);

        Tessellator tessellator = Tessellator.instance;

        bindTexture(TextureMap.locationItemsTexture);
        GL11.glPushMatrix();

        glScalef(0.75f, 0.75f, 0.75f);
        glTranslatef(0, -1f / 5f, 0);
        glDepthMask(true);
        glTranslated(0, Math.sin(rot / 100) / 10, 0);
        glRotatef(rot % 360, 0, 1, 0);
        ClientUtils.render3DItem(item, tessellator);
        glRotatef(-(rot % 360), 0, 1, 0);
        glTranslatef(0, 1f / 5f, 0);

        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);

        rand.setSeed(2983457L);

        for (int i = 0; i < rot / 20; i++)
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
            tessellator.addVertex(100, 110, 100);

            tessellator.draw();
        }

        rot += 0.5f;

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
