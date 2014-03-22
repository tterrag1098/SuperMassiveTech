package tterrag.supermassivetech.block.container;

import tterrag.supermassivetech.SuperMassiveTech;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;

public abstract class BlockContainerSMT extends BlockContainer
{

	Class<? extends TileEntity> te;
	private String unlocName;

	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te)
	{
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
		this.te = te;
		this.unlocName = unlocName;
	}

	@Override
	public String getUnlocalizedName()
	{
		return unlocName;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		try
		{
			return te.newInstance();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return null;
	}
	
	public boolean hasPlacementRotation()
	{
		return true;
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return hasPlacementRotation() ? opp : 0;
	}
}
