package tterrag.supermassivetech.client.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.client.util.RenderingUtils;

public class BlackHoleSpecialRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation texture = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "textures/blocks/blackHole.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTickTime)
    {
        glPushMatrix();
        glTranslated(x + 0.5f, y + 0.5f, z + 0.5f);

        this.bindTexture(texture);

        float rot = RenderingUtils.getRotation(partialTickTime, 2);

        glEnable(GL_BLEND);

        glPushMatrix();
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glDisable(GL_LIGHTING);
        glAlphaFunc(GL_ALWAYS, 1);
        RenderingUtils.renderBillboardQuad(rot, 0.7d);
        glPopAttrib();
        glPopMatrix();

        glPopMatrix();
    }
}
