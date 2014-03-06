package tterrag.supermassivetech.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlackHoleHopper extends TileGravityWell
{
	private ForgeDirection connection = ForgeDirection.UNKNOWN;
	private ItemStack[] inventory = new ItemStack[1];

	@Override
	public void updateEntity()
	{
		System.out.println(connection);
		if (connection == ForgeDirection.UNKNOWN)
			connection = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));

		super.updateEntity();
	}

	@Override
	protected float getStrengthMultiplier()
	{
		return 1;
	}
}
