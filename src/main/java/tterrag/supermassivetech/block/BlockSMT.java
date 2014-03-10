package tterrag.supermassivetech.block;

import tterrag.supermassivetech.SuperMassiveTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class BlockSMT extends Block {

	protected BlockSMT(Material mat, SoundType type, float hardness) {
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
	}
}
