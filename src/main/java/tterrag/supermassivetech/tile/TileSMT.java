package tterrag.supermassivetech.tile;

import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class TileSMT extends TileEntity
{
	protected final float RANGE;
	protected final float STRENGTH;
	protected final float MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV;
	private static Constants c = Constants.instance();

	/**
	 * Sets the tile with all default constant values
	 */
	public TileSMT()
	{
		this(1, 1, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV);
	}

	/**
	 * Sets the tile with the passed range muliplier and strength multiplier
	 */
	public TileSMT(float rangeMult, float strengthMult)
	{
		this(rangeMult, strengthMult, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV);
	}

	/**
	 * Fully customizeable constructor, takes in all gravity parameters
	 * 
	 * @param rangeMult - mulitplier for range, base is set in config
	 * @param strengthMult - multiplier for strength, base is set in config
	 * @param maxGravXZ - max gravity that can be applied in the X and Z
	 *            directions
	 * @param maxGravY - max gravity that can be applied in the Y direction
	 * @param minGrav - minimum gravity to be applied, prevents "bouncing"
	 */
	public TileSMT(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav)
	{
		RANGE = c.RANGE * rangeMult;
		STRENGTH = c.STRENGTH * strengthMult;
		MAX_GRAV_XZ = maxGravXZ;
		MAX_GRAV_Y = maxGravY;
		MIN_GRAV = minGrav;
	}

	@Override
	public void updateEntity()
	{
		if (isGravityWell())
		{
			for (Object o : worldObj.getEntitiesWithinAABB(Entity.class,
					AxisAlignedBB.getBoundingBox(xCoord + 0.5 - RANGE, yCoord + 0.5 - RANGE, zCoord + 0.5 - RANGE, xCoord + 0.5 + RANGE, yCoord + 0.5 + RANGE, zCoord + 0.5 + RANGE)))
			{
				Utils.applyGravity(STRENGTH * getStrengthMultiplier(), MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV, RANGE, (Entity) o, this);
			}
		}
	}

	/**
	 * The multiplier to be applied to the strength of the gravity, used for
	 * gravity dependant on stored amount of items of configuration
	 * 
	 * @return a float to multiply the strength with, defaults to 1.
	 */
	protected float getStrengthMultiplier()
	{
		return 1;
	}

	/**
	 * Whether this tile is a gravity well, that is, whether to apply gravity to
	 * surrounding entities. Defaults to false.
	 */
	protected boolean isGravityWell()
	{
		return false;
	}
}
