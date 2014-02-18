/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
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
}
