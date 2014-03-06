package tterrag.supermassivetech.tile;

import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public abstract class TileGravityWell extends TileEntity
{
	protected final float RANGE;
	protected final float STRENGTH;
	protected final float MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV;
	private static Constants c = Constants.instance();
	
	public TileGravityWell()
	{
		this(1, 1, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV);
	}
	
	public TileGravityWell(float rangeMult, float strengthMult)
	{
		this(rangeMult, strengthMult, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV);
	}
	
	public TileGravityWell(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav)
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
		for (Object o : worldObj.getEntitiesWithinAABB(Entity.class,
				AxisAlignedBB.getBoundingBox(xCoord + 0.5 - RANGE, yCoord + 0.5 - RANGE, zCoord + 0.5 - RANGE, xCoord + 0.5 + RANGE, yCoord + 0.5 + RANGE, zCoord + 0.5 + RANGE)))
		{
			Utils.applyGravity(STRENGTH * getStrengthMultiplier(), MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV, RANGE, (Entity) o, this);
		}
	}
	
	/**
	 * The multiplier to be applied to the strength of the gravity, used for gravity dependant on stored amount of items of configuration
	 * @return a float to multiply the strength with
	 */
	protected abstract float getStrengthMultiplier();
}

