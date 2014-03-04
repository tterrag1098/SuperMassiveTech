/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.block;

import java.util.ArrayList;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import tterrag.ultimateStorage.UltimateStorage;
import tterrag.ultimateStorage.lib.Reference;
import tterrag.ultimateStorage.tile.TileStorageBlock;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class StorageBlock extends BlockContainer
{
	private TileStorageBlock te;

	public StorageBlock()
	{
		super(Material.iron);
		setStepSound(soundTypeMetal);
		setHardness(30.0f);
		setCreativeTab(new CreativeTabs(CreativeTabs.getNextID(), "Ultimate Storage")
		{
			@Override
			public Item getTabIconItem()
			{
				return UltimateStorage.storageBlock.getItem(null, 0, 0, 0);
			}
		});
	}

	@Override
	public String getUnlocalizedName()
	{
		return "tterrag.storageBlock";
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		return new TileStorageBlock();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof TileStorageBlock && !player.isSneaking())
		{
			player.openGui(UltimateStorage.instance, 0, world, x, y, z);
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

		if (te instanceof TileStorageBlock && stack.stackTagCompound != null)
		{
			long itemsStored = stack.stackTagCompound.getLong("itemsStored");
			long fluidStored = stack.stackTagCompound.getLong("fluidStored");

			ItemStack stackStored = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack"));
			FluidStack fluidStackStored = FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack"));

			TileStorageBlock tile = (TileStorageBlock) te;

			tile.storedAmount = itemsStored;
			tile.getTank().amountStored = fluidStored;
			tile.setStoredItemOnPlace(stackStored);
			tile.getTank().setStoredFluidOnPlace(fluidStackStored);
		}
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player)
	{
		te = (TileStorageBlock) world.getTileEntity(x, y, z);
		super.onBlockClicked(world, x, y, z, player);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune)
	{
		ItemStack stack = new ItemStack(this);

		NBTTagCompound tag = new NBTTagCompound();

		tag.setLong("itemsStored", te.storedAmount);
		tag.setLong("fluidStored", te.getTank().amountStored);

		if (te.getStoredItem() != null)
		{
			NBTTagCompound itemTag = new NBTTagCompound();
			te.getStoredItem().writeToNBT(itemTag);
			tag.setTag("itemStack", itemTag);
		}

		if (te.getTank().getFluidStored() != null)
		{
			NBTTagCompound fluidTag = new NBTTagCompound();
			te.getTank().getFluidStored().writeToNBT(fluidTag);
			tag.setTag("fluidStack", fluidTag);
		}

		stack.stackTagCompound = tag;

		ArrayList<ItemStack> item = new ArrayList<ItemStack>();
		item.add(stack);
		return item;
	}
}
