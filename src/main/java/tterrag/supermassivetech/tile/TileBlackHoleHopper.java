package tterrag.supermassivetech.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileBlackHoleHopper extends TileGravityWell
{
	private ForgeDirection connectionDir;
	private ItemStack[] inventory = new ItemStack[2];
	ArrayList<InventoryConnection> inventories = new ArrayList<InventoryConnection>();

	private InventoryConnection connection;

	private final int searchRange = 5, hiddenSlot = 0, cfgSlot = 1;

	private class InventoryConnection
	{
		public int x, y, z;
		public IInventory inv;

		public InventoryConnection(IInventory inv, int x, int y, int z)
		{
			this.inv = inv;
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public boolean isStillValid()
		{
			return worldObj.getTileEntity(x, y, z) == this.inv;
		}
	}

	public TileBlackHoleHopper()
	{
		super(1, 0.8f);
		inventory[cfgSlot] = new ItemStack(Blocks.dirt);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		
		if (worldObj.isRemote)
			return;

		if (connectionDir == null || connection == null)
		{
			connectionDir = ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
			TileEntity te = worldObj.getTileEntity(connectionDir.offsetX + xCoord, connectionDir.offsetY + yCoord, connectionDir.offsetZ + zCoord);
			if (te != null && te instanceof IInventory)
				connection = new InventoryConnection((IInventory) te, te.xCoord, te.yCoord, te.zCoord);
		}
		else
		{
			checkConnection();
		}

		if (connection == null)
		{
			connectionDir = null;
			return;
		}

		if (onTime())
		{
			searchForInventories();
			System.out.println(inventories.toString());
		}

		for (int i = 0; i < inventories.size(); i++)
			processInventory(inventories.get(i));

		for (int i = 0; i < inventories.size(); i++)
			processConnection();
	}

	private void checkConnection()
	{
		TileEntity te = worldObj.getTileEntity(connection.x, connection.y, connection.z);
		if (te instanceof IInventory)
		{
			connection = new InventoryConnection((IInventory) te, te.xCoord, te.yCoord, te.zCoord);
		}
		else
		{
			connection = null;
			connectionDir = null;
		}
	}

	private void processConnection()
	{
		if (inventory[hiddenSlot] == null)
			return;

		for (int idx = 0; idx < connection.inv.getSizeInventory(); idx++)
		{
			ItemStack stack = connection.inv.getStackInSlot(idx);

			if (connection.inv.isItemValidForSlot(idx, inventory[hiddenSlot]) && (stack == null || (itemStackEquals(stack, inventory[cfgSlot]) && stack.stackSize < stack.getMaxStackSize())))
			{
				if (stack == null)
				{
					ItemStack newStack = inventory[hiddenSlot].copy();
					newStack.stackSize = 1;
					connection.inv.setInventorySlotContents(idx, newStack);
					inventory[hiddenSlot].stackSize--;
				}
				else
				{
					stack.stackSize++;
					inventory[hiddenSlot].stackSize--;
				}
				if (inventory[hiddenSlot].stackSize <= 0)
					inventory[hiddenSlot] = null;

				break;
			}
		}
	}

	/**
	 * Pulls from an inventory, adds to the hiddenSlot
	 * 
	 * @param i - InventoryConnection object to pull from
	 * @return a list of Integers to remove afterwards (removed or changed TEs)
	 */
	private List<Integer> processInventory(InventoryConnection i)
	{
		List<Integer> list = new ArrayList<Integer>();

		if (i.isStillValid())
		{
			loop: for (int idx = 0; idx < i.inv.getSizeInventory(); idx++)
			{
				ItemStack stack = i.inv.getStackInSlot(idx);

				if (stack != null && stack.getItem() == inventory[cfgSlot].getItem() && stack.stackSize > 0
						&& (inventory[hiddenSlot] == null || inventory[hiddenSlot].stackSize < inventory[hiddenSlot].getMaxStackSize()))
				{
					i.inv.decrStackSize(idx, 1);
					if (inventory[hiddenSlot] == null)
						inventory[hiddenSlot] = new ItemStack(stack.getItem());
					else
						inventory[hiddenSlot].stackSize++;
					break loop;
				}
			}
		}
		else
		{
			inventories.remove(i);
/*
			TileEntity te = worldObj.getTileEntity(i.x, i.y, i.z);

			if (te != null && te instanceof IInventory)
			{
				inventories.add(new InventoryConnection((IInventory) te, te.xCoord, te.yCoord, te.zCoord));
			}
*/		}

		return list;
	}

	@Override
	protected float getStrengthMultiplier()
	{
		return 1;
	}

	private boolean onTime()
	{
		return worldObj.getWorldTime() % 20 == 0;
	}

	private void searchForInventories()
	{
		for (int x = -searchRange; x <= searchRange; x++)
		{
			for (int y = -searchRange; y <= searchRange; y++)
			{
				for (int z = -searchRange; z <= searchRange; z++)
				{
					if (isValidTileEntity(xCoord + x, yCoord + y, zCoord + z))
					{
						TileEntity te = worldObj.getTileEntity(xCoord + x, yCoord + y, zCoord + z);
						inventories.add(new InventoryConnection((IInventory) te, te.xCoord, te.yCoord, te.zCoord));
					}
				}
			}
		}
	}

	private boolean isValidTileEntity(int x, int y, int z)
	{
		Block block = worldObj.getBlock(x, y, z);
		if (block != null && block.hasTileEntity(worldObj.getBlockMetadata(x, y, z)))
		{
			TileEntity te = worldObj.getTileEntity(x, y, z);
			return te != null && !(te instanceof TileBlackHoleHopper) && teNotEquals(te, connection) && te instanceof IInventory && notAlreadyFound(te);
		}

		return false;
	}

	private boolean notAlreadyFound(TileEntity te)
	{
		for (InventoryConnection i : inventories)
		{
			if (te.xCoord == i.x && te.yCoord == i.y && te.zCoord == i.z)
				return false;
		}

		return true;
	}

	private boolean teNotEquals(TileEntity te1, InventoryConnection te2)
	{
		// System.out.println("te1: " + te1.xCoord + ", " + te1.yCoord + ", " +
		// te1.zCoord + "  te2: " + te2.x + ", " + te2.y + ", " + te2.z);
		return te1.xCoord != te2.x || te1.yCoord != te2.y || te1.zCoord != te2.z;
	}

	public static boolean itemStackEquals(ItemStack s1, ItemStack s2)
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
}
