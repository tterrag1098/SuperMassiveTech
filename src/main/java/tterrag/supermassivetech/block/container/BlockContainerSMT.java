package tterrag.supermassivetech.block.container;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;

public abstract class BlockContainerSMT extends BlockContainer
{

	Class<? extends TileEntity> te;
	private String unlocName;
	private int renderID;

	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te)
	{
		this(unlocName, mat, type, hardness, te, 0);
	}
	
	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te, int renderID)
	{
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
		this.te = te;
		this.unlocName = unlocName;
		this.renderID = renderID;
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
	
	public boolean hasCustomModel()
	{
		return true;
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return hasPlacementRotation() ? opp : 0;
	}
	
	@Override
	public int getRenderType()
	{
		return renderID;
	}
	
	@Override
	public boolean renderAsNormalBlock()
	{
		return !hasCustomModel();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return !hasCustomModel();
	}
}
