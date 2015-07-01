package tterrag.supermassivetech.client.render.entity;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.common.entity.EntityDyingBlock;

import com.enderio.core.client.render.RenderUtil;

import static org.lwjgl.opengl.GL11.*;

public class RenderDyingBlock extends Render
{
    private static final RenderBlocks render = RenderBlocks.getInstance();
    private static final Random rand = new Random();

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

            rand.setSeed(entity.getUniqueID().getLeastSignificantBits());
            int rotOffset = rand.nextInt(360);

            glPushMatrix();
            glTranslated(x, y, z);
            glRotatef(RenderUtil.getRotation(rand.nextInt(10)) + rotOffset, 1, 0, 0);
            glRotatef(RenderUtil.getRotation(rand.nextInt(10)) + rotOffset, 0, 1, 0);
            glRotatef(RenderUtil.getRotation(rand.nextInt(10)) + rotOffset, 0, 0, 1);
            glDisable(GL_LIGHTING);

            bindEntityTexture(entity);

            render.setRenderBoundsFromBlock(block);
            render.renderBlockSandFalling(block, entity.worldObj, bX, bY, bZ, meta);

            glEnable(GL_LIGHTING);
            glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

}
