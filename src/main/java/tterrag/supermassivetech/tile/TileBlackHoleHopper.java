package tterrag.supermassivetech.tile;

import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlackHoleHopper extends TileEntity
{
	private ForgeDirection connection = ForgeDirection.UNKNOWN;
	private ItemStack[] inventory = new ItemStack[1];
	private Constants c = Constants.instance();

	@Override
	public void updateEntity()
	{
		System.out.println(connection);
		if (connection == ForgeDirection.UNKNOWN)
			connection = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));

		for (Object o : worldObj.getEntitiesWithinAABB(Entity.class,
				AxisAlignedBB.getBoundingBox(xCoord + 0.5 - c.RANGE, yCoord + 0.5 - c.RANGE, zCoord + 0.5 - c.RANGE, xCoord + 0.5 + c.RANGE, yCoord + 0.5 + c.RANGE, zCoord + 0.5 + c.RANGE)))
		{
			Utils.applyGravity(c.STRENGTH, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV, c.RANGE, (Entity) o, this);
		}
	}
}
