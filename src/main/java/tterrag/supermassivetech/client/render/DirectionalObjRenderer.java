package tterrag.supermassivetech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.tile.TileSMTInventory;

/**
 * Renders a model with directional placement
 * 
 * @author Garrett Spicer-Davis
 */
public class DirectionalObjRenderer extends TileEntitySpecialRenderer
{
	private IModelCustom model;
	private ResourceLocation texture;// = new ResourceLocation("supermassivetech", "textures/models/hopper.png");

	public DirectionalObjRenderer(ResourceLocation model, ResourceLocation texture)
	{
		this.model = AdvancedModelLoader.loadModel(model);//new ResourceLocation("supermassivetech", "models/hopper.obj"));
		this.texture = texture;
	}

	public void renderDirectionalTileEntityAt(TileSMTInventory tile, double x, double y, double z, boolean metaOverride)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y - (metaOverride ? 0.1f : 0), (float) z + 0.5f);

		int meta = metaOverride ? 0 : tile.getBlockMetadata();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

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

		model.renderAll();

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float yaw)
	{
		renderDirectionalTileEntityAt((TileSMTInventory) tile, x, y, z, false);
	}
}
