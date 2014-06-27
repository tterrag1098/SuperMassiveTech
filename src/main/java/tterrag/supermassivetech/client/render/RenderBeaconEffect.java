package tterrag.supermassivetech.client.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.tile.TileWaypoint;

public class RenderBeaconEffect extends TileEntitySpecialRenderer
{
    private ResourceLocation beam = new ResourceLocation("textures/entity/beacon_beam.png");

    private void renderWaypointAt(TileWaypoint tile, double x, double y, double z, float tickDelay)
    {
        if (tile.waypoint == null || tile.waypoint.isNull() || !tile.waypoint.players.contains(Minecraft.getMinecraft().thePlayer.getCommandSenderName()))
        {
            return;
        }

        glAlphaFunc(GL_GREATER, 0.1F);
        bindTexture(beam);

        int[] color = tile.getColorArr();

        Tessellator tessellator = Tessellator.instance;
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, 10497.0F);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, 10497.0F);
        glDisable(GL_LIGHTING);
        glDisable(GL_CULL_FACE);
        glEnable(GL_BLEND);
        glDepthMask(false);
        GL11.glTranslatef(0, 0.74f, 0);

        // make it glow
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_CONSTANT_ALPHA);

        float f2 = tile.getWorldObj().getTotalWorldTime() + tickDelay;
        float f3 = -f2 * 0.2F - MathHelper.floor_float(-f2 * 0.1F);
        double pY = TileEntityRendererDispatcher.staticPlayerY;

        double d3 = f2 * 0.025D * (1.0D - 2.5D);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(color[0], color[1], color[2], 255);
        tessellator.setBrightness(0xF000F0);
        double c = 0.2D;

        // obfuscated names, basically are the points for the rects, sin/cos
        // used for rotation
        double d7 = 0.5D + Math.cos(d3 + 2.356194490192345D + y + pY) * c;
        double d9 = 0.5D + Math.sin(d3 + 2.356194490192345D + y + pY) * c;
        double d11 = 0.5D + Math.cos(d3 + (Math.PI / 4D) + y + pY) * c;
        double d13 = 0.5D + Math.sin(d3 + (Math.PI / 4D) + y + pY) * c;
        double d15 = 0.5D + Math.cos(d3 + 3.9269908169872414D + y + pY) * c;
        double d17 = 0.5D + Math.sin(d3 + 3.9269908169872414D + y + pY) * c;
        double d19 = 0.5D + Math.cos(d3 + 5.497787143782138D + y + pY) * c;
        double d21 = 0.5D + Math.sin(d3 + 5.497787143782138D + y + pY) * c;
        double d23 = 256.0F * 1;
        double d25 = 0.0D;
        double d27 = 1.0D;
        double d28 = -1.0F + f3;
        double d29 = 256.0F * 1 * (0.5D / c) + d28;

        // four quads = 16 verts
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

        // put everything back where we found it
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glDepthMask(true);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.5F);
        GL11.glTranslatef(0, -0.74f, 0);
    }

    @Override
    public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8)
    {
        renderWaypointAt((TileWaypoint) var1, var2, var4, var6, var8);
    }
}
