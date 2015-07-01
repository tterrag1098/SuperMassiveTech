package tterrag.supermassivetech.client.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tterrag.supermassivetech.common.tile.energy.TileCharger;

import com.enderio.core.client.render.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

public class ChargerSpecialRenderer extends TileEntitySpecialRenderer
{
    private EntityItem item = null;

    private void renderChargerAt(TileCharger tile, double x, double y, double z, float partialTickTime)
    {
        glPushMatrix();
        glTranslated(x, y, z);

        if (item == null)
        {
            item = new EntityItem(tile.getWorldObj());
        }

        ItemStack stack = tile.getStackInSlot(0);
        item.setEntityItemStack(stack);

        if (stack != null)
        {
            glPushMatrix();
            glTranslatef(0.5f, 0.3f, 0.5f);
            RenderUtil.render3DItem(item, true);
            glPopMatrix();
        }

        if (tile.isCharging())
        {
            glPushAttrib(GL_ALL_ATTRIB_BITS);

            setupGlTranslucent();

            glTranslatef(0.5f, 0.5f, 0.5f);

            Tessellator tessellator = Tessellator.instance;

            float rot = -RenderUtil.getRotation(10f);

            renderAllTranslucent(tessellator, rot);

            glRotatef(180, 1, 0, 0);

            renderAllTranslucent(tessellator, rot);

            glPopAttrib();
        }

        glPopMatrix();
    }

    private void setupGlTranslucent()
    {
        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glShadeModel(GL_SMOOTH);
        glDisable(GL_ALPHA_TEST);
        glDisable(GL_CULL_FACE);
        glDepthMask(false);

        RenderHelper.disableStandardItemLighting();

        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
    }

    private void renderAllTranslucent(Tessellator tessellator, float rot)
    {
        glPushMatrix();
        glTranslatef(0, 0.14f, 0);
        glRotatef(45, 0, 1, 0);
        glRotatef(45, 1, 0, 0);

        for (int i = 0; i < 4; i++)
        {
            glRotatef(90, 0, -1, 1);
            glPushMatrix();
            glRotatef(rot, 0, 1, 0);
            for (int j = 0; j < 4; j++)
            {
                drawPowerTranslucent(tessellator);
                glRotatef(-90, 0, 1, 0);
            }
            glPopMatrix();
        }

        glPopMatrix();
    }

    private void drawPowerTranslucent(Tessellator tessellator)
    {
        double width = 0.08875;
        double startPt = -0.785;
        double endPt = -0.4;

        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(100, 255, 255, 100);
        tessellator.addVertex(-width, startPt, -width);
        tessellator.addVertex(width, startPt, -width);
        tessellator.addVertex(width, endPt, -width);
        tessellator.addVertex(-width, endPt, -width);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime)
    {
        if (tile instanceof TileCharger)
            renderChargerAt((TileCharger) tile, x, y, z, partialTickTime);
    }
}
