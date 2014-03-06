package tterrag.supermassivetech.tile;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlackHoleHopper extends TileGravityWell
{
	private ForgeDirection connection;
	private ItemStack[] inventory = new ItemStack[1];

	public TileBlackHoleHopper()
	{
		super(1, 0.8f);
	}
	
	@Override
	public void updateEntity()
	{
		if (connection == null)
			connection = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));

		super.updateEntity();
	}

	@Override
	protected float getStrengthMultiplier()
	{
		return 1;
	}
}
