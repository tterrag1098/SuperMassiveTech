package tterrag.supermassivetech.client.render.entity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;

import tterrag.core.client.util.RenderingUtils;
import tterrag.supermassivetech.common.entity.EntityDyingBlock;
import tterrag.supermassivetech.common.util.Utils;

public class RenderDyingBlock extends Render
{
    private static final Tessellator tes = Tessellator.instance;
    private static final RenderBlocks render = RenderBlocks.getInstance();

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTickTime)
    {
        if (entity instanceof EntityDyingBlock)
        {
            Block block = ((EntityDyingBlock) entity).getBlock();
            int meta = ((EntityDyingBlock) entity).getMeta();
            
            int bX = MathHelper.floor_double(entity.posX);
            int bY = MathHelper.floor_double(entity.posY);
            int bZ = MathHelper.floor_double(entity.posZ);
            
            glPushMatrix();
            glTranslated(x, y, z);
            glRotatef(RenderingUtils.getRotation(partialTickTime, 10f), 1, 0, 0);
            glRotatef(RenderingUtils.getRotation(partialTickTime, 5f), 0, 1, 0);
            glRotatef(RenderingUtils.getRotation(partialTickTime, 1f), 0, 0, 1);
            glDisable(GL_LIGHTING);
            
            bindEntityTexture(entity);
            
            render.setRenderBoundsFromBlock(block);
            render.renderBlockSandFalling(block, entity.worldObj, bX, bY, bZ, meta);
            
            glPopMatrix();
        }
    }

    private void drawIcon(IIcon icon, Block block)
    {
        float minU = icon.getMinU(), maxU = icon.getMaxU(), minV = icon.getMinV(), maxV = icon.getMaxV();

        int color = block.getBlockColor();
        Utils.setGLColorFromInt(color);
        
        tes.startDrawingQuads();
        tes.addVertexWithUV(-0.5, -0.5, 0, minU, minV);
        tes.addVertexWithUV(0.5, -0.5, 0, maxU, minV);
        tes.addVertexWithUV(0.5, 0.5, 0, maxU, maxV);
        tes.addVertexWithUV(-0.5, 0.5, 0, minU, maxV);
        tes.draw();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

}
