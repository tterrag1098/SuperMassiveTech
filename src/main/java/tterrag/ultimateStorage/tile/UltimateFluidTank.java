package tterrag.ultimateStorage.tile;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

public class UltimateFluidTank implements IFluidTank {
	
	public FluidStack fluidStored;
	public long amountStored;
	public final long max = TileStorageBlock.max;

	@Override
	public FluidStack getFluid() {
		return fluidStored;
	}

	@Override
	public int getFluidAmount() {
		return amountStored > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) amountStored;
	}

	@Override
	public int getCapacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (!doFill || amountStored >= max) return 0;
		
		if (fluidStored == null)
			fluidStored = resource;
		
		int toAdd = resource.amount;
				
		if (toAdd + amountStored > max)
		{
			toAdd = (int) (toAdd + amountStored - max);
			amountStored = max;
			return toAdd;
		}
		else
		{
			amountStored += toAdd;
			return toAdd;
		}
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

}
