package tterrag.supermassivetech.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class ItemObjRenderer implements ISimpleBlockRenderingHandler
{
	private Block block;
	private int renderID;
	private DirectionalModelRenderer objRenderer;
	
	public ItemObjRenderer(int renderID, DirectionalModelRenderer objRenderer)
	{
		this.renderID = renderID;
		this.objRenderer = objRenderer;
	}
	
	public void init(Block block)
	{
		this.block = block;
	}
	
	@Override
	public void renderInventoryBlock(Block passedBlock, int metadata, int modelId, RenderBlocks renderer)
	{
		if (block == null) throw new RuntimeException("You didn't initialize the item renderer.");
		
		if (passedBlock.getClass() == block.getClass())
			objRenderer.renderDirectionalTileEntityAt(null, 0, 0, 0, true);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return renderID;
	}

}
