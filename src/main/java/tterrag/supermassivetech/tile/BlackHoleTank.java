package tterrag.supermassivetech.tile;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class BlackHoleTank implements IFluidTank
{

	FluidStack fluidStored;
	public long amountStored;
	public final long max = TileBlackHoleStorage.max;

	@Override
	public FluidStack getFluid()
	{
		return fluidStored;
	}

	@Override
	public int getFluidAmount()
	{
		return amountStored > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) amountStored;
	}

	@Override
	public int getCapacity()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public FluidTankInfo getInfo()
	{
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (amountStored >= max)
			return 0;

		if (fluidStored == null && doFill)
			fluidStored = resource;

		int toAdd = resource.amount;

		if (toAdd + amountStored > max)
		{
			toAdd = (int) (toAdd + amountStored - max);
			if (!doFill)
				return toAdd;
			amountStored = max;
			return toAdd;
		}
		else
		{
			if (!doFill)
				return toAdd;
			amountStored += toAdd;
			return toAdd;
		}
	}

	@Override
	public FluidStack drain(int toRemove, boolean doDrain)
	{
		if (amountStored <= 0)
			return null;

		if (amountStored - toRemove < 0)
		{
			toRemove = (int) (toRemove - amountStored);
			FluidStack stack = new FluidStack(fluidStored.getFluid(), toRemove);
			if (!doDrain)
				return stack;
			amountStored = 0;
			fluidStored = null;
			return stack;
		}
		else
		{
			FluidStack stack = new FluidStack(fluidStored.getFluid(), toRemove);
			if (!doDrain)
				return stack;
			amountStored -= toRemove;
			return stack;
		}
	}

	public void setStoredFluidOnPlace(FluidStack fluidStackStored)
	{
		if (fluidStored == null)
			fluidStored = fluidStackStored;
	}
	
	public FluidStack getFluidStored()
	{
		return fluidStored;
	}
}
