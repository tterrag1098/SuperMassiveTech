package tterrag.supermassivetech.client.render;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tterrag.supermassivetech.tile.energy.TileCharger;
import tterrag.supermassivetech.util.ClientUtils;

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
            ClientUtils.render3DItem(item, partialTickTime, true);
            glPopMatrix();
        }

        if (tile.isCharging())
        {
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            glDisable(GL_TEXTURE_2D);
            glEnable(GL_BLEND);
            glShadeModel(GL_SMOOTH);
            glDisable(GL_ALPHA_TEST);
            glDisable(GL_CULL_FACE);

            RenderHelper.disableStandardItemLighting();

            OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

            glTranslatef(0.5f, 0.5f, 0.5f);

            Tessellator tessellator = Tessellator.instance;

            float rot = -(tile.getWorldObj().getTotalWorldTime() + partialTickTime) % 360 * 2;

            for (int i = 0; i < 8; i++)
            {
                glRotatef(90, 0, 1, 0);

                if (i == 4)
                {
                    glRotatef(180, 1, 0, 0);
                }

                if (i < 4)
                {
                    glPushMatrix();
                    glTranslatef(0, 0.15f, 0);
                    glRotatef(45, 1, 0, 1);
                    glRotatef(rot, 0, 1, 0);
                    drawPowerTranslucent(tessellator);
                    glRotatef(90, 0, 1, 0);
                    drawPowerTranslucent(tessellator);
                    glPopMatrix();
                }
                else
                {
                    glPushMatrix();
                    glTranslatef(0, 0.15f, 0);
                    glRotatef(45, 1, 0, 1);
                    glRotatef(rot, 0, 1, 0);
                    drawPowerTranslucent(tessellator);
                    glRotatef(90, 0, 1, 0);
                    drawPowerTranslucent(tessellator);
                    glPopMatrix();
                }
            }
            glPopAttrib();
        }

        glPopMatrix();
    }

    private void drawPowerTranslucent(Tessellator tessellator)
    {
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(100, 255, 255, 100);
        tessellator.addVertex(-0.05, -0.2, 0);
        tessellator.addVertex(0.05, -0.2, 0);
        tessellator.addVertex(0.05, -0.7, 0);
        tessellator.addVertex(-0.05, -0.7, 0);
        tessellator.draw();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTickTime)
    {
        if (tile instanceof TileCharger)
            renderChargerAt((TileCharger) tile, x, y, z, partialTickTime);
    }
}
