package tterrag.supermassivetech.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.tile.TileWaypoint;

public class RenderWaypoint extends TileEntitySpecialRenderer
{
    private ResourceLocation beam = new ResourceLocation("textures/entity/beacon_beam.png");
    
    private void renderWaypointAt(TileWaypoint tile, double x, double y, double z, float tickDelay)
    {
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        bindTexture(beam);
        
        int[] color = tile.getColorArr();
        
        Tessellator tessellator = Tessellator.instance;
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        OpenGlHelper.glBlendFunc(770, 1, 1, 0);
        float f2 = (float)tile.getWorldObj().getTotalWorldTime() + tickDelay;
        float f3 = -f2 * 0.2F - (float)MathHelper.floor_float(-f2 * 0.1F);
        double pY = TileEntityRendererDispatcher.staticPlayerY;
        
        byte b0 = 1;
        double d3 = (double)f2 * 0.025D * (1.0D - (double)(b0 & 1) * 2.5D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(color[0], color[1], color[2], 255);
        tessellator.setBrightness(230);
        double d5 = (double)b0 * 0.2D;
        double d7 = 0.5D + Math.cos(d3 + 2.356194490192345D + y + pY) * d5;
        double d9 = 0.5D + Math.sin(d3 + 2.356194490192345D + y + pY) * d5;
        double d11 = 0.5D + Math.cos(d3 + (Math.PI / 4D) + y + pY) * d5;
        double d13 = 0.5D + Math.sin(d3 + (Math.PI / 4D) + y + pY) * d5;
        double d15 = 0.5D + Math.cos(d3 + 3.9269908169872414D + y + pY) * d5;
        double d17 = 0.5D + Math.sin(d3 + 3.9269908169872414D + y + pY) * d5;
        double d19 = 0.5D + Math.cos(d3 + 5.497787143782138D + y + pY) * d5;
        double d21 = 0.5D + Math.sin(d3 + 5.497787143782138D + y + pY) * d5;
        double d23 = (double)(256.0F * 1);
        double d25 = 0.0D;
        double d27 = 1.0D;
        double d28 = (double)(-1.0F + f3);
        double d29 = (double)(256.0F * 1) * (0.5D / d5) + d28;
        tessellator.addVertexWithUV(x + d7, y + d23, z + d9, d27, d29);
        tessellator.addVertexWithUV(x + d7, y, z + d9, d27, d28);
        tessellator.addVertexWithUV(x + d11, y, z + d13, d25, d28);
        tessellator.addVertexWithUV(x + d11, y + d23, z + d13, d25, d29);
        tessellator.addVertexWithUV(x + d19, y + d23, z + d21, d27, d29);
        tessellator.addVertexWithUV(x + d19, y, z + d21, d27, d28);
        tessellator.addVertexWithUV(x + d15, y, z + d17, d25, d28);
        tessellator.addVertexWithUV(x + d15, y + d23, z + d17, d25, d29);
        tessellator.addVertexWithUV(x + d11, y + d23, z + d13, d27, d29);
        tessellator.addVertexWithUV(x + d11, y, z + d13, d27, d28);
        tessellator.addVertexWithUV(x + d19, y, z + d21, d25, d28);
        tessellator.addVertexWithUV(x + d19, y + d23, z + d21, d25, d29);
        tessellator.addVertexWithUV(x + d15, y + d23, z + d17, d27, d29);
        tessellator.addVertexWithUV(x + d15, y, z + d17, d27, d28);
        tessellator.addVertexWithUV(x + d7, y, z + d9, d25, d28);
        tessellator.addVertexWithUV(x + d7, y + d23, z + d9, d25, d29);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        renderWaypointAt((TileWaypoint) var1, var2, var4, var6, var8);
    }
}
