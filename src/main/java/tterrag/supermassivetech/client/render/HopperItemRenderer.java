package tterrag.supermassivetech.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.block.container.BlockBlackHoleHopper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class HopperItemRenderer implements ISimpleBlockRenderingHandler
{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
	{
		if (block instanceof BlockBlackHoleHopper)
			HopperRenderer.instance.renderTileEntityBLHAt(null, 0, 0, 0, true);
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
		return SuperMassiveTech.renderIDHopper;
	}

}
