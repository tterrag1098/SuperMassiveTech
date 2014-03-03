/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.block;

import tterrag.ultimateStorage.lib.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import tterrag.ultimateStorage.UltimateStorage;
import tterrag.ultimateStorage.tile.TileStorageBlock;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class StorageBlock extends BlockContainer
{
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
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int whichDirectionFacing = MathHelper.floor_double(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}
}
