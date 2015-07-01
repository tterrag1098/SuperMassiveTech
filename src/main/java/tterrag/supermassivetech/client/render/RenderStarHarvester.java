package tterrag.supermassivetech.client.render;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;
import tterrag.supermassivetech.common.util.Utils;

import com.enderio.core.client.render.ColorUtil;
import com.enderio.core.client.render.DirectionalModelRenderer;
import com.enderio.core.client.render.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

public class RenderStarHarvester extends DirectionalModelRenderer<TileStarHarvester>
{
    private IModelCustom main, sphere, ring;
    private static ResourceLocation textureMain = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "textures/models/starHarvesterMain.png");
    private static ResourceLocation textureSphere = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "textures/models/starHarvesterSphere.png");
    private static ResourceLocation textureRing1 = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "textures/models/starHarvesterRing.png");

    public RenderStarHarvester(ResourceLocation main, ResourceLocation sphere, ResourceLocation ring)
    {
        super(main, textureMain);

        this.main = AdvancedModelLoader.loadModel(main);
        this.sphere = AdvancedModelLoader.loadModel(sphere);
        this.ring = AdvancedModelLoader.loadModel(ring);
    }

    @Override
    protected int getRotation(TileStarHarvester tile, int metaOverride)
    {
        return super.getRotation(tile, metaOverride) % 6;
    }

    @Override
    protected void renderModel(TileStarHarvester tile, int meta)
    {
        if (tile instanceof TileStarHarvester || tile == null)
        {
            TileStarHarvester harvester = tile;

            GL11.glScalef(0.5f, 0.5f, 0.5f);
            main.renderAll();

            double speed = 0d;
            float[] spins = { 0, 0, 0, 0 };

            Minecraft.getMinecraft().getTextureManager().bindTexture(textureRing1);

            if (tile != null)
            {
                speed = harvester.spinSpeed;
                spins = harvester.spins;
            }

            GL11.glTranslatef(0f, 1f, 0f);

            if (meta > 5)
            {
                GL11.glRotatef(90f, 1f, 0, 0);

                GL11.glRotatef(spins[1], 0, 0, 1f);
                ring.renderAll();
                GL11.glRotatef(-spins[1], 0, 0, 1f);

                GL11.glRotatef(-90f, 1f, 0, 0);
                GL11.glScalef(0.95f, 0.95f, 0.95f);

                GL11.glRotatef(spins[2], 1f, 1f, 0);
                ring.renderAll();
                GL11.glRotatef(-spins[2], 1f, 1f, 0);

                GL11.glRotatef(90f, 0, 0, 1f);
                GL11.glScalef(0.95f, 0.95f, 0.95f);

                GL11.glRotatef(spins[3], 0, 1f, 1f);
                ring.renderAll();
                GL11.glRotatef(-spins[3], 0, 1f, 1f);

                GL11.glRotatef(90f, 0, 0, 1f);
            }

            if (tile != null && harvester.getStackInSlot(0) != null)
            {
                IStar star = Utils.getType(harvester.getStackInSlot(0));

                ColorUtil.setGLColorFromInt(star.getColor());

                Minecraft.getMinecraft().getTextureManager().bindTexture(textureSphere);

                glPushMatrix();
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glRotatef(-spins[0], 0, 1f, 0);
                GL11.glScalef(1f + (float) speed / 5f, 1f + (float) (speed > 1 ? (speed - 1f) / 5f : 0), 1f + (float) speed / 5f);
                GL11.glTranslatef(0f, -1f, 0f);

                sphere.renderAll();
                glPopMatrix();

                GL11.glEnable(GL11.GL_LIGHTING);

                if (tile.dying)
                {
                    renderBeams(tile, speed);
                }
            }
        }
        GL11.glPopMatrix();
    }

    private void renderBeams(TileStarHarvester tile, double speed)
    {
        glPushMatrix();

        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE, GL_ZERO, GL_ONE);
        RenderHelper.disableStandardItemLighting();
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);

        Tessellator tessellator = Tessellator.instance;

        Random rand = Utils.rand;
        rand.setSeed(298347L);

        // glTranslatef(0, 1f, 0);

        float rot = RenderUtil.getRotation(-4f);

        for (int i = 0; i < speed * 5; i++)
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
            tessellator.setColorRGBA(255, 255, 100, 0);
            tessellator.addVertex(5, 4, 5);
            tessellator.addVertex(5, 6, 5);

            tessellator.draw();
        }

        glDisable(GL_BLEND);
        glShadeModel(GL_FLAT);
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_ALPHA_TEST);
        glEnable(GL_LIGHTING);
        RenderHelper.enableStandardItemLighting();

        glPopMatrix();
    }
}
