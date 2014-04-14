package tterrag.supermassivetech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.tile.TileStarHarvester;

public class RenderStarHarvester extends TileEntitySpecialRenderer
{
    private IModelCustom main, sphere;
    private ResourceLocation textureMain = new ResourceLocation("supermassivetech", "textures/models/starHarvesterMain.png");
    private ResourceLocation textureSphereInactive = new ResourceLocation("supermassivetech", "textures/models/starHarvesterSphereInactive.png");

    public RenderStarHarvester(ResourceLocation main, ResourceLocation sphere)
    {
        this.main = AdvancedModelLoader.loadModel(main);
        this.sphere = AdvancedModelLoader.loadModel(sphere);
    }

    public void renderDirectionalTileEntityAt(TileStarHarvester tile, double x, double y, double z, boolean metaOverride)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y - (metaOverride ? 0.1f : 0), (float) z + 0.5f);

        int meta = metaOverride ? 0 : tile.getBlockMetadata();
        Minecraft.getMinecraft().getTextureManager().bindTexture(textureMain);

        switch (meta)
        {
        case 1:
            GL11.glRotatef(180f, 0, 0, 1);
            GL11.glTranslatef(0, -1f, 0);
            break;
        case 2:
            GL11.glRotatef(90f, 1f, 0, 0);
            GL11.glTranslatef(0, -0.5f, -0.5f);
            break;
        case 3:
            GL11.glRotatef(90f, -1f, 0, 0);
            GL11.glTranslatef(0, -0.5f, 0.5f);
            break;
        case 4:
            GL11.glRotatef(90f, 0, 0, -1f);
            GL11.glTranslatef(-0.5f, -0.5f, 0);
            break;
        case 5:
            GL11.glRotatef(90f, 0, 0, 1f);
            GL11.glTranslatef(0.5f, -0.5f, 0);
            break;
        }

        GL11.glScalef(0.5f, 0.5f, 0.5f);
        main.renderAll();

        double speed = 0d;
        float spinRot = 0f;

        Minecraft.getMinecraft().getTextureManager().bindTexture(textureSphereInactive);

        if (tile != null)
        {
            speed = tile.spinSpeed;
            spinRot = tile.spinRot;
        }

        GL11.glRotatef(spinRot, 0, 1f, 0);

        if (speed < 0.4f)
        {
            GL11.glColor4f(0.4f + ((float) speed * 1.5f), 0.4f - (float) speed, 0.4f - (float) speed, 1f);
        }
        else
        {
            GL11.glColor4f(1.0f, ((float) speed * 2 - 1.2f), 0, 1.0f);
        }

        GL11.glScalef(1f + (float) speed / 5f, 1.0f, 1f + (float) speed / 5f);

        sphere.renderAll();

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float yaw)
    {
        renderDirectionalTileEntityAt((TileStarHarvester) tile, x, y, z, tile == null);
    }
}
