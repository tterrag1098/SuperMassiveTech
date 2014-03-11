package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import tterrag.supermassivetech.tile.TileStarHarvester;

public class BlockStarHarvester extends BlockContainerSMT
{

	public BlockStarHarvester()
	{
		super("tterrag.starHarvester", Material.iron, soundTypeMetal, 15.0f, TileStarHarvester.class);
	}
}
