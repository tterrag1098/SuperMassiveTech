package tterrag.supermassivetech.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;

public abstract class TileSMTInventory extends TileEntity implements IInventory
{
	protected ItemStack[] inventory;
	protected final float RANGE;
	protected final float STRENGTH;
	protected final float MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV;
	private static Constants c = Constants.instance();

	/**
	 * Sets the tile with all default constant values
	 */
	public TileSMTInventory()
	{
		this(1, 1, c.MAX_GRAV_XZ, c.MAX_GRAV_Y, c.MIN_GRAV);
	}

	/**
	 * Sets the tile with the passed range muliplier and strength multiplier
	 */
	public TileSMTInventory(float rangeMult, float strengthMult)
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
	public TileSMTInventory(float rangeMult, float strengthMult, float maxGravXZ, float maxGravY, float minGrav)
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
				Utils.applyGravity(STRENGTH * getStrengthMultiplier(), MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV, RANGE, (Entity) o, this, showParticles());
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
	 * surrounding entities.
	 */
	public abstract boolean isGravityWell();
	
	public abstract boolean showParticles();

	@Override
	public int getSizeInventory() 
	{
		return inventory.length;
	}


	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		if (this.inventory[i] != null)
		{
			ItemStack itemstack;
			if (this.inventory[i].stackSize <= j)
			{
				itemstack = this.inventory[i];
				this.inventory[i] = null;
				return itemstack;
			}
			else
			{
				itemstack = this.inventory[i].splitStack(j);
				if (this.inventory[i].stackSize == 0)
				{
					this.inventory[i] = null;
				}
				return itemstack;
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlot(int var1) 
	{
		return var1 < inventory.length ? inventory[var1] : null;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		if (this.inventory[i] != null)
		{
			ItemStack itemstack = this.inventory[i];
			this.inventory[i] = null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.inventory[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}
	
	@Override
	public boolean hasCustomInventoryName() 
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) 
	{
		return true;
	}

	@Override
	public void openInventory() 
	{
		// Do nothing
	}

	@Override
	public void closeInventory() 
	{
		// Do nothing
	}

	@Override
	public boolean isItemValidForSlot(int var1, ItemStack var2) 
	{
		return var1 < inventory.length;
	}
}
