package tterrag.supermassivetech.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.tile.TileBlackHoleHopper;

public class HopperRenderer extends TileEntitySpecialRenderer
{
	private IModelCustom model;
	public static HopperRenderer instance = new HopperRenderer();
	
	public HopperRenderer()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("supermassivetech", "models/hopper.obj"));
	}
	
	private static final ResourceLocation texture = new ResourceLocation("supermassivetech", "textures/models/hopper.png");

	public void renderTileEntityBLHAt(TileBlackHoleHopper tile, double x, double y, double z, boolean metaOverride)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5f, (float) y - (metaOverride ? 0.24f : 0.03f), (float) z + 0.5f);
				
		int meta = metaOverride ? 0 : tile.getBlockMetadata();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		switch(meta)
		{
		case 1: 
			GL11.glRotatef(180f, 0, 0, 1);
			GL11.glTranslatef(0, -1.06f, 0);
			break;
		case 2:
			GL11.glRotatef(90f, 1f, 0, 0);
			GL11.glTranslatef(0, -0.53f, -0.53f);
			break;
		case 3:
			GL11.glRotatef(90f, -1f, 0, 0);
			GL11.glTranslatef(0, -0.53f, 0.53f);
			break;
		case 4:
			GL11.glRotatef(90f, 0, 0, -1f);
			GL11.glTranslatef(-0.53f, -0.53f, 0);
			break;
		case 5:
			GL11.glRotatef(90f, 0, 0, 1f);
			GL11.glTranslatef(0.53f, -0.53f, 0);
			break;
		}
		
		GL11.glScalef(0.5f, 0.515f, 0.5f);
				
		model.renderAll();
		
		GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float yaw)
	{
		renderTileEntityBLHAt((TileBlackHoleHopper) tile, x, y, z, false);
	}
}
