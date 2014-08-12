package tterrag.supermassivetech.client.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.client.util.ClientUtils;

public class RenderBlackHole extends TileEntitySpecialRenderer
{
    private static final ResourceLocation texture = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "/textures/blocks/blackHole.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTickTime)
    {
        glPushMatrix();
        glTranslated(x + 0.5f, y + 0.5f, z + 0.5f);

        this.bindTexture(texture);

        float rot = ClientUtils.getRotation(partialTickTime, 1);

        glEnable(GL_BLEND);

        renderBillboardedQuad(rot, 0.7);

        glPopMatrix();
    }

    private void renderBillboardedQuad(float rot, double scale)
    {
        glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);

        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glPushMatrix();
        glRotatef(rot, 0, 0, 1);
        glDisable(GL_LIGHTING);
        glAlphaFunc(GL_ALWAYS, 1);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        glColor3f(1, 1, 1);
        tessellator.setColorRGBA(255, 255, 255, 255);
        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0);
        tessellator.addVertexWithUV(-scale, scale, 0, 0, 1);
        tessellator.addVertexWithUV(scale, scale, 0, 1, 1);
        tessellator.addVertexWithUV(scale, -scale, 0, 1, 0);
        tessellator.draw();
        glPopMatrix();
        glPopAttrib();
    }
}
