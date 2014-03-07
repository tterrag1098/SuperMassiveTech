/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.supermassivetech.block;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class BlockBlackHoleStorage extends BlockContainer
{
	public BlockBlackHoleStorage()
	{
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setHardness(30.0f);
		setCreativeTab(SuperMassiveTech.tabSMT);
	}

	@Override
	public String getUnlocalizedName()
	{
		return "tterrag.storageBlock";
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileBlackHoleStorage();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileBlackHoleStorage && !player.isSneaking())
		{
			player.openGui(SuperMassiveTech.instance, 0, world, x, y, z);
			return true;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	private IIcon side, bottom, top, front;

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reg)
	{
		this.side = reg.registerIcon(Reference.MOD_TEXTUREPATH + ":" + (this.getUnlocalizedName().substring(8)) + "sides");
		this.bottom = reg.registerIcon(Reference.MOD_TEXTUREPATH + ":" + (this.getUnlocalizedName().substring(8)) + "top");
		this.top = reg.registerIcon(Reference.MOD_TEXTUREPATH + ":" + (this.getUnlocalizedName().substring(8)) + "top");
		this.front = reg.registerIcon(Reference.MOD_TEXTUREPATH + ":" + (this.getUnlocalizedName().substring(8)) + "front");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if (side == 1)
			return this.top;
		else if (side == 0)
			return this.top;
		else if (metadata == 2 && side == 2)
			return this.front;
		else if (metadata == 3 && side == 5)
			return this.front;
		else if (metadata == 0 && side == 3)
			return this.front;
		else if (metadata == 1 && side == 4)
			return this.front;
		else
			return this.side;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
	{
		int whichDirectionFacing = MathHelper.floor_double(player.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);

		TileEntity te = world.getTileEntity(x, y, z);

		if (te instanceof TileBlackHoleStorage && stack.stackTagCompound != null && !world.isRemote)
		{
			long itemsStored = stack.stackTagCompound.getLong("itemsStored");
			long fluidStored = stack.stackTagCompound.getLong("fluidStored");

			ItemStack stackStored = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack"));
			FluidStack fluidStackStored = FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack"));

			TileBlackHoleStorage tile = (TileBlackHoleStorage) te;

			tile.storedAmount = itemsStored;
			tile.getTank().amountStored = fluidStored;
			tile.setStoredItemOnPlace(stackStored);
			tile.getTank().setStoredFluidOnPlace(fluidStackStored);
		}
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode && !world.isRemote)
			dropItem(world, getNBTItem(world, x, y, z), x, y, z);	
	}

	public ItemStack getNBTItem(World world, int x, int y, int z)
	{
		ItemStack stack = new ItemStack(this);
		TileBlackHoleStorage te = (TileBlackHoleStorage) world.getTileEntity(x, y, z);
		
		if (te == null)
			return stack;
			
		NBTTagCompound tag = new NBTTagCompound();

		long itemAmount = te.storedAmount + (te.inventory[2] == null ? 0 : te.inventory[2].stackSize);
		tag.setLong("itemsStored", itemAmount);
		tag.setLong("fluidStored", te.getTank().amountStored);

		if (te.getStoredItem() != null)
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			te.getStoredItem().writeToNBT(itemTag);
			tag.setTag("itemStack", itemTag);
		}
		
		if (te.getStoredItem() == null && te.inventory[2] != null)
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			te.inventory[2].writeToNBT(itemTag);
			tag.setTag("itemStack", itemTag);
		}

		if (te.getTank().getFluidStored() != null)
		{
			NBTTagCompound fluidTag = new NBTTagCompound();
			te.getTank().getFluidStored().writeToNBT(fluidTag);
			tag.setTag("fluidStack", fluidTag);
		}

		stack.stackTagCompound = tag;
		return stack;
	}
	
	private void dropItem(World world, ItemStack item, int x, int y, int z)
	{
		float f = (float) Math.random() + x;
		float f1 = (float) Math.random() + y;
		float f2 = (float) Math.random() + z;
		
		world.spawnEntityInWorld(new EntityItem(world, f, f1, f2, item));
	}
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		return new ItemStack(this); // Temp code why doesn't this work D:
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return null;
	}
}
