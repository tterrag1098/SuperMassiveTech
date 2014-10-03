package tterrag.supermassivetech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import tterrag.core.client.render.DirectionalModelRenderer;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;
import tterrag.supermassivetech.common.util.Utils;

public class RenderStarHarvester extends DirectionalModelRenderer
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
    protected int getRotation(TileEntity tile, int metaOverride)
    {
        return super.getRotation(tile, metaOverride) % 6;
    }

    @Override
    protected void renderModel(TileEntity tile, int meta)
    {
        if (tile instanceof TileStarHarvester)
        {
            TileStarHarvester harvester = (TileStarHarvester) tile;

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

                Utils.setGLColorFromInt(star.getColor());

                Minecraft.getMinecraft().getTextureManager().bindTexture(textureSphere);

                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glRotatef(-spins[0], 0, 1f, 0);
                GL11.glScalef(1f + (float) speed / 5f, 1.0f, 1f + (float) speed / 5f);
                GL11.glTranslatef(0f, -1f, 0f);

                sphere.renderAll();

                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
        GL11.glPopMatrix();
    }
}
