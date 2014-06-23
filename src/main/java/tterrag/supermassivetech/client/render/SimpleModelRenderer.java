package tterrag.supermassivetech.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.obj.WavefrontObject;
import tterrag.supermassivetech.util.ClientUtils;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
 
public class SimpleModelRenderer implements ISimpleBlockRenderingHandler
{
    private final int renderId;
    private final Tessellator tes = Tessellator.instance;
    private final WavefrontObject model;
    
    public SimpleModelRenderer(WavefrontObject model, int renderId)
    {
        this.renderId = renderId;
        this.model = model;
    }
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        RenderHelper.disableStandardItemLighting();
        tes.startDrawingQuads();
        tes.setColorOpaque_F(1, 1, 1);
        ClientUtils.renderWithIcon(model, block.getIcon(0, metadata), tes);
        tes.draw();
        RenderHelper.enableStandardItemLighting();
    }
    
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        tes.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
        tes.setColorOpaque_F(1, 1, 1);
        tes.addTranslation(x + .5F, y + .5F, z + .5F);
        ClientUtils.renderWithIcon(model, block.getIcon(0, world.getBlockMetadata(x, y, z)), tes);
        tes.addTranslation(-x - .5F, -y - .5F, -z - .5F);
        return true;
    }
 
    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }
 
    @Override
    public int getRenderId()
    {
        return renderId;
    }
}
