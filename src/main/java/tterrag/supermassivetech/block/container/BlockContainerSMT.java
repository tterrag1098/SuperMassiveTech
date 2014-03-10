package tterrag.supermassivetech.block.container;

import tterrag.supermassivetech.SuperMassiveTech;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockContainerSMT extends BlockContainer{

	Class<? extends TileEntity> te;
	private String unlocName;
	
	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te) {
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
		this.te = te;
		this.unlocName = unlocName;
	}
	
	@Override
	public String getUnlocalizedName() {
		return unlocName;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		try { return te.newInstance(); }
		catch (Throwable t) { t.printStackTrace(); }
		return null;
	}

}
