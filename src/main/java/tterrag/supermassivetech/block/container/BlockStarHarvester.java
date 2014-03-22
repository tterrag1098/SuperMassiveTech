package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tterrag.supermassivetech.tile.TileStarHarvester;

public class BlockStarHarvester extends BlockContainerSMT
{
	public BlockStarHarvester()
	{
		super("tterrag.starHarvester", Material.iron, soundTypeMetal, 15.0f, TileStarHarvester.class);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,	int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) 
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (!world.isRemote && te instanceof TileStarHarvester)
		{
			return ((TileStarHarvester)te).insertStar(player);
		}
		return false;
	}
}
