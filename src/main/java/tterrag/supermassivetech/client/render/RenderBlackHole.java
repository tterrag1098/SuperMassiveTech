package tterrag.supermassivetech.client.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL14;

import tterrag.supermassivetech.ModProps;

public class RenderBlackHole extends TileEntitySpecialRenderer
{
    private static final ResourceLocation texture = new ResourceLocation(ModProps.MOD_TEXTUREPATH, "/textures/blocks/blackHole.png");

    private final int pointScale = 100;

    private float scale = 1f;

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTickTime)
    {
        glPushMatrix();

        this.bindTexture(texture);

        glTranslated(x + 0.5, y + 0.5, z + 0.5);
        glEnable(GL_BLEND);

        glAlphaFunc(GL_GREATER, 0);
        glEnable(GL_POINT_SPRITE);
        glTexEnvi(GL_POINT_SPRITE, GL_COORD_REPLACE, GL_TRUE);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(0f).put(0.01f).put(0.01f).put(0);
        buffer.flip();
        GL14.glPointParameter(GL14.GL_POINT_DISTANCE_ATTENUATION, buffer);

        float pulse = (float) Math.sin(scale) * 1.5f;
        scale += 0.01;
        pulse = 1 + pulse;

        glPointSize(pointScale + pulse);

        glBegin(GL_POINTS);

        glVertex3d(0, 0, 0);

        glEnd();
        glPopMatrix();
    }

    // private void renderBillboardedQuad(double tileX, double tileY, double
    // tileZ, float angle, float scale, float partialTickTime)
    // {
    // if ((Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer))
    // {
    // Tessellator tessellator = Tessellator.instance;
    // float rotX = ActiveRenderInfo.rotationX, rotZ =
    // ActiveRenderInfo.rotationZ, rotYZ = ActiveRenderInfo.rotationYZ, rotXY =
    // ActiveRenderInfo.rotationXY, rotXZ = ActiveRenderInfo.rotationXZ;
    //
    // EntityPlayer player =
    // (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
    // double playerPosX = player.prevPosX + (player.posX - player.prevPosX) *
    // partialTickTime;
    // double playerPosY = player.prevPosY + (player.posY - player.prevPosY) *
    // partialTickTime;
    // double playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) *
    // partialTickTime;
    //
    // GL11.glTranslated(-playerPosX, -playerPosY, -playerPosZ);
    //
    // tessellator.startDrawingQuads();
    // tessellator.setBrightness(220);
    //
    // Vec3 v1 = Vec3.createVectorHelper(-rotX * scale - rotYZ * scale, -rotXZ *
    // scale, -rotZ * scale - rotXY * scale);
    // Vec3 v2 = Vec3.createVectorHelper(-rotX * scale + rotYZ * scale, rotXZ *
    // scale, -rotZ * scale + rotXY * scale);
    // Vec3 v3 = Vec3.createVectorHelper(rotX * scale + rotYZ * scale, rotXZ *
    // scale, rotZ * scale + rotXY * scale);
    // Vec3 v4 = Vec3.createVectorHelper(rotX * scale - rotYZ * scale, -rotXZ *
    // scale, rotZ * scale - rotXY * scale);
    // if (angle != 0.0F)
    // {
    // Vec3 playerVec = Vec3.createVectorHelper(playerPosX, playerPosY,
    // playerPosZ);
    // Vec3 tileVec = Vec3.createVectorHelper(tileX, tileY, tileZ);
    // Vec3 combVec = playerVec.subtract(tileVec).normalize();
    // rotate(combVec.xCoord, combVec.yCoord, combVec.zCoord, angle, v1);
    // rotate(combVec.xCoord, combVec.yCoord, combVec.zCoord, angle, v2);
    // rotate(combVec.xCoord, combVec.yCoord, combVec.zCoord, angle, v3);
    // rotate(combVec.xCoord, combVec.yCoord, combVec.zCoord, angle, v4);
    // }
    //
    // tessellator.setNormal(0.0F, 0.0F, -1.0F);
    // tessellator.addVertexWithUV(tileX + v1.xCoord, tileY + v1.yCoord, tileZ +
    // v1.zCoord, 0, 0);
    // tessellator.addVertexWithUV(tileX + v2.xCoord, tileY + v2.yCoord, tileZ +
    // v2.zCoord, 1, 0);
    // tessellator.addVertexWithUV(tileX + v3.xCoord, tileY + v3.yCoord, tileZ +
    // v3.zCoord, 1, 1);
    // tessellator.addVertexWithUV(tileX + v4.xCoord, tileY + v4.yCoord, tileZ +
    // v4.zCoord, 0, 1);
    //
    // tessellator.draw();
    // }
    // }
    //
    // private void rotate(double x, double y, double z, double angle, Vec3 vec)
    // {
    // double d = -x * vec.xCoord - y * vec.yCoord - z * vec.zCoord;
    // double d1 = angle * vec.xCoord + y * vec.zCoord - z * vec.yCoord;
    // double d2 = angle * vec.yCoord - x * vec.zCoord + z * vec.xCoord;
    // double d3 = angle * vec.zCoord + x * vec.yCoord - y * vec.xCoord;
    // vec.xCoord = (d1 * angle - d * x - d2 * z + d3 * y);
    // vec.yCoord = (d2 * angle - d * y + d1 * z - d3 * x);
    // vec.zCoord = (d3 * angle - d * z - d1 * y + d2 * x);
    // }
}
