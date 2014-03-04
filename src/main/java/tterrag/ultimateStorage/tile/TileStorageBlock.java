/**
 * TileStorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.tile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import tterrag.ultimateStorage.UltimateStorage;
import tterrag.ultimateStorage.config.ConfigHandler;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class TileStorageBlock extends TileEntity implements ISidedInventory, IFluidHandler
{
	public final static long max = 1099511627776L;
	private final float RANGE;
	private final float STRENGTH;
	private final float MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV;

	/* Item handling */
	public ItemStack[] inventory;
	public long storedAmount;
	private ItemStack storedItem;

	/* Fluid handling */
	private UltimateFluidTank tank = new UltimateFluidTank();

	public TileStorageBlock()
	{
		inventory = new ItemStack[3];
		RANGE = Math.min(1000, ConfigHandler.range);
		MAX_GRAV_XZ = Math.min(1, ConfigHandler.maxGravityXZ);
		MAX_GRAV_Y = Math.min(1, ConfigHandler.maxGravityY);
		MIN_GRAV = Math.min(Math.min(MAX_GRAV_Y, MAX_GRAV_Y), ConfigHandler.minGravity);
		STRENGTH = Math.min(1000, ConfigHandler.strength);
	}

	public class SlotInput extends Slot
	{
		public SlotInput(IInventory par1iInventory, int par2, int par3, int par4)
		{
			super(par1iInventory, par2, par3, par4);
		}

		@Override
		public boolean isItemValid(ItemStack par1ItemStack)
		{
			return storedItem == null || stacksEqual(par1ItemStack, storedItem);
		}
	}

	public class SlotFluidContainer extends Slot
	{
		public SlotFluidContainer(IInventory inv, int x, int y, int z)
		{
			super(inv, x, y, z);
		}

		@Override
		public boolean isItemValid(ItemStack par1ItemStack)
		{
			if (FluidContainerRegistry.isContainer(par1ItemStack) && FluidContainerRegistry.isFilledContainer(par1ItemStack))
				return tank.fluidStored == null || FluidContainerRegistry.containsFluid(par1ItemStack, tank.fluidStored);

			return false;
		}
	}

	@Override
	public void updateEntity()
	{
		if (tank.amountStored > max)
			tank.amountStored = max;

		for (int i = 0; i < inventory.length; i++)
			if (inventory[i] != null && inventory[i].stackSize <= 0)
				inventory[i] = null;

		if (inventory[1] != null && storedAmount < max)
		{
			if (stacksEqual(inventory[1], storedItem))
			{
				int inputToStorage = inventory[1].stackSize;
				if ((storedAmount + inputToStorage) > max)
				{
					inventory[1].stackSize = (int) (inputToStorage + storedAmount - max);
					storedAmount = max;
				}
				else
				{
					storedAmount += inputToStorage;
					inventory[1] = null;
				}
			}
			else if (storedItem == null)
			{
				storedItem = inventory[1].copy();
				storedAmount = inventory[1].stackSize;
				inventory[1] = null;
			}
			else
			{
				UltimateStorage.logger
						.severe(String.format("Input does not match storage, \"%s\" was not expected in this input! \"%s\" was expected!, X:%d, Y:%d, Z:%d",
								StatCollector.translateToLocal(inventory[1].getUnlocalizedName() + ".name"), StatCollector.translateToLocal(storedItem.getUnlocalizedName() + ".name"), xCoord, yCoord,
								zCoord));
				spitInputItem();
			}
		}

		if (storedAmount != 0)
		{
			int maxStack = storedItem.getMaxStackSize();
			if (inventory[2] == null)
			{
				inventory[2] = storedItem.copy();
				if (storedAmount > maxStack)
				{
					inventory[2].stackSize = maxStack;
					storedAmount -= maxStack;
				}
				else
				{
					inventory[2].stackSize = (int) storedAmount;
					storedAmount = 0;
					storedItem = null;
				}
			}
			else if (inventory[2].stackSize < maxStack && stacksEqual(inventory[2], storedItem))
			{
				int outputFromStorage = maxStack - inventory[2].stackSize;
				if (outputFromStorage < storedAmount)
				{
					inventory[2].stackSize = maxStack;
					storedAmount -= outputFromStorage;
				}
				else
				{
					inventory[2].stackSize += (int) storedAmount;
					storedAmount = 0;
					storedItem = null;
				}
			}
		}

		for (Object o : worldObj.getEntitiesWithinAABB(Entity.class,
				AxisAlignedBB.getBoundingBox(xCoord + 0.5 - RANGE, yCoord + 0.5 - RANGE, zCoord + 0.5 - RANGE, xCoord + 0.5 + RANGE, yCoord + 0.5 + RANGE, zCoord + 0.5 + RANGE)))
		{
			applyGravity(STRENGTH * (1f + (((float) storedAmount + (float) tank.amountStored) / ((float) max * 2f))), MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV, RANGE, (Entity) o);
			System.out.println((1f + (((float) storedAmount + (float) tank.amountStored) / ((float) max * 0.00001f))));
		}
	}

	private void applyGravity(float gravStrength, float maxGravXZ, float maxGravY, float minGrav, float range, Entity entity)
	{
		double dist = Math.sqrt(Math.pow(xCoord + 0.5 - entity.posX, 2) + Math.pow(zCoord + 0.5 - entity.posZ, 2) + Math.pow(yCoord + 0.5 - entity.posY, 2));

		if (dist >= range)
			return;

		double xDisplacment = entity.posX - (xCoord + 0.5);
		double yDisplacment = entity.posY - (yCoord + 0.5);
		double zDisplacment = entity.posZ - (zCoord + 0.5);

		// http://en.wikipedia.org/wiki/Spherical_coordinate_system#Coordinate_system_conversions

		double theta = Math.acos(zDisplacment / dist);
		double phi = Math.atan2(yDisplacment, xDisplacment);

		// More strength for everything but players, lower dist is bigger effect
		if (!(entity instanceof EntityPlayer))
			dist *= 0.5;
		else
			dist *= 2;

		double vecX = -gravStrength * Math.sin(theta) * Math.cos(phi) / dist;
		double vecY = -gravStrength * Math.sin(theta) * Math.sin(phi) / dist;
		double vecZ = -gravStrength * Math.cos(theta) / dist;

		if (Math.abs(vecX) > maxGravXZ)
			vecX *= maxGravXZ / Math.abs(vecX);
		if (Math.abs(vecY) > maxGravY)
			vecY *= maxGravY / Math.abs(vecY);
		if (Math.abs(vecZ) > maxGravXZ)
			vecZ *= maxGravXZ / Math.abs(vecZ);

		if (Math.abs(vecX) < minGrav)
			vecX = 0;
		if (Math.abs(vecY) < minGrav)
			vecY = 0;
		if (Math.abs(vecZ) < minGrav)
			vecZ = 0;

		entity.setVelocity(entity.motionX + vecX, entity.motionY + vecY, entity.motionZ + vecZ);
	}

	private void spitInputItem()
	{
		if (worldObj.isRemote)
			return;

		float f = (float) Math.random();
		float f1 = (float) Math.random();
		float f2 = (float) Math.random();

		EntityItem entityitem = new EntityItem(worldObj, this.xCoord + f, this.yCoord + f1, this.zCoord + f2, inventory[1]);

		entityitem.motionX = (int) Math.random() * 2;
		entityitem.motionY = (int) Math.random() * 2;
		entityitem.motionZ = (int) Math.random() * 2;
		worldObj.spawnEntityInWorld(entityitem);

		inventory[1] = null;
	}

	@Override
	public int getSizeInventory()
	{
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return inventory[var1];
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
	public String getInventoryName()
	{
		return "tterrag.inv.storageBlock";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return true;
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
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return new int[] { 1 };
	}

	@Override
	public boolean canInsertItem(int var1, ItemStack var2, int var3)
	{
		return true;
	}

	@Override
	public boolean canExtractItem(int var1, ItemStack var2, int var3)
	{
		if (var1 == 2) { return true; }
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		if (i == 1) { return storedItem == null || stacksEqual(storedItem, itemstack); }

		if (i == 0) { return FluidContainerRegistry.isContainer(itemstack); }

		return false;
	}

	/**
	 * @author powercrystals
	 */
	public static boolean stacksEqual(ItemStack s1, ItemStack s2)
	{
		if (s1 == null && s2 == null)
			return true;
		if (s1 == null || s2 == null)
			return false;
		if (!s1.isItemEqual(s2))
			return false;
		if (s1.getTagCompound() == null && s2.getTagCompound() == null)
			return true;
		if (s1.getTagCompound() == null || s2.getTagCompound() == null)
			return false;
		return s1.getTagCompound().equals(s2.getTagCompound());
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < this.inventory.length; ++i)
		{
			if (this.inventory[i] != null)
			{
				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				this.inventory[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}
		nbt.setTag("Items", nbttaglist);

		nbt.setLong("stored", storedAmount);
		NBTTagCompound itemstackNBT = new NBTTagCompound();
		if (storedItem != null)
			storedItem.writeToNBT(itemstackNBT);
		nbt.setTag("itemstack", itemstackNBT);

		nbt.setLong("fluidStored", tank.amountStored);
		NBTTagCompound fluidstackNBT = new NBTTagCompound();
		if (tank.fluidStored != null)
			tank.fluidStored.writeToNBT(fluidstackNBT);
		nbt.setTag("fluidstack", fluidstackNBT);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);

		NBTTagList nbttaglist = nbt.getTagList("Items", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i)
		{
			NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
			int j = nbttagcompound1.getByte("Slot") & 255;

			this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
		}

		storedAmount = nbt.getLong("stored");
		storedItem = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbt.getTag("itemstack"));

		tank.amountStored = nbt.getLong("fluidStored");
		tank.fluidStored = FluidStack.loadFluidStackFromNBT((NBTTagCompound) nbt.getTag("fluidstack"));
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.readFromNBT(pkt.func_148857_g());
	}

	/**
	 * @return A copy of the stored item in this block, can be null
	 */
	public ItemStack getStoredItem()
	{
		return storedItem == null ? null : storedItem.copy();
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if (canFill(from, resource.getFluid()))
			return tank.fill(resource, doFill);

		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (canDrain(from, resource.getFluid()))
			return tank.drain(resource.amount, doDrain);

		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return tank.amountStored < max && (tank.fluidStored == null || fluid == tank.fluidStored.getFluid());
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		System.out.println(from);
		return tank.amountStored != 0;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[] { tank.getInfo() };
	}

	public UltimateFluidTank getTank()
	{
		return this.tank;
	}

	public void setStoredItemOnPlace(ItemStack stackStored)
	{
		if (storedItem == null)
			storedItem = stackStored;
	}
}
