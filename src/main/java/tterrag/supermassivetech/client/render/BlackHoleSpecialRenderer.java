package tterrag.supermassivetech.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.api.common.tile.IBlackHole;

import com.enderio.core.client.handlers.ClientHandler;
import com.enderio.core.client.render.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

public class BlackHoleSpecialRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation texture = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "textures/blocks/blackHole.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTickTime)
    {
        IBlackHole bh = (IBlackHole) te;

        float wobbleScale = 0.05f;
        float wobble = ClientHandler.getTicksElapsed() / 10f;
        float wobbleX = (float) (Math.sin(wobble) * wobbleScale) + 1;
        float wobbleY = (float) (Math.sin(wobble) * -1 * wobbleScale) + 1;

        glPushMatrix();
        glTranslated(x + 0.5, y + 0.5, z + 0.5);
        glScalef(wobbleX, wobbleY, 1);

        this.bindTexture(texture);

        float rot = RenderUtil.getRotation(2);

        glPushMatrix();
        glPushAttrib(GL_ALL_ATTRIB_BITS);
        glEnable(GL_BLEND);
        glDisable(GL_LIGHTING);
        glAlphaFunc(GL_ALWAYS, 1);
        glEnable(GL_DEPTH_TEST);
        RenderUtil.renderBillboardQuad(rot, bh.getSize());
        glPopAttrib();
        glPopMatrix();
        glPopMatrix();
    }
}
