package tterrag.supermassivetech.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import tterrag.supermassivetech.SuperMassiveTech;

public class BlockSMT extends Block
{

	protected BlockSMT(Material mat, SoundType type, float hardness)
	{
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
	}
}
