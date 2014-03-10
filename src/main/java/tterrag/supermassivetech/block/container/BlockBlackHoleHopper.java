package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;

public class BlockBlackHoleHopper extends BlockContainerSMT
{
	public BlockBlackHoleHopper()
	{
		super("tterrag.blackHoleHopper", Material.iron, soundTypeMetal, 30.0f, TileBlackHoleHopper.class);
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return opp;
	}
}
