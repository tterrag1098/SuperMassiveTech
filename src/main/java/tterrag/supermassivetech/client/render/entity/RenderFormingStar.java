package tterrag.supermassivetech.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import org.lwjgl.opengl.GL12;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;

public class RenderFormingStar extends Render
{
    private final ResourceLocation texture = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/items/starHeart.png");

    private int rot;

    private ItemStack stack;

    private final Random rand = new Random();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float var8, float var9)
    {
        if (stack == null)
            stack = new ItemStack(SuperMassiveTech.itemRegistry.heartOfStar);
        
        glPushMatrix();

        glTranslatef((float) x, (float) y, (float) z);

        Tessellator tessellator = Tessellator.instance;

        bindTexture(TextureMap.locationItemsTexture);
        IIcon icon = stack.getItem().getIcon(stack, 0);
        
        glDepthMask(true);
        glRotatef(rot % 360, 0, 1, 0);
        ItemRenderer.renderItemIn2D(tessellator, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), icon.getIconWidth(), icon.getIconHeight(), 1 / 16f);
        glRotatef(-(rot % 360), 0, 1, 0);

        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);
        
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
            tessellator.addVertex(100, 110, 100);

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
