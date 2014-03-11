package tterrag.supermassivetech.block.container;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;

public class BlockBlackHoleHopper extends BlockHopper
{
	public BlockBlackHoleHopper()
	{
		super();
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
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return opp;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		return new TileBlackHoleHopper();
	}

	@Override
	public int getRenderType()
	{
		return 38;
	}

	@Override
	public void breakBlock(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, Block p_149749_5_, int p_149749_6_)
	{
		// nothing for now
	}

	@Override
	public boolean onBlockActivated(World p_149727_1_, int p_149727_2_, int p_149727_3_, int p_149727_4_, EntityPlayer p_149727_5_, int p_149727_6_, float p_149727_7_, float p_149727_8_,
			float p_149727_9_)
	{
		// nothing for now
		return false;
	}
}
