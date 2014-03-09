package tterrag.supermassivetech.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;

public class BlockBlackHoleHopper extends BlockContainer
{
	public BlockBlackHoleHopper()
	{
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setHardness(30.0f);
		setCreativeTab(SuperMassiveTech.tabSMT);
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return "tterrag.blackHoleHopper";
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileBlackHoleHopper();
	}
	
	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return opp;
	}
}
