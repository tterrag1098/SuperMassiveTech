/**
 * StorageBlock
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage.block;

import tterrag.ultimateStorage.UltimateStorage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * @author Garrett Spicer-Davis
 * 
 */
public class StorageBlock extends Block
{
	public StorageBlock()
	{
		super(Material.iron);
		setStepSound(soundTypeMetal);
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
}
