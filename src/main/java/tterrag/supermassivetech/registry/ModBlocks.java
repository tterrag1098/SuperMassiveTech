package tterrag.supermassivetech.registry;

import net.minecraft.block.Block;
import tterrag.supermassivetech.block.BlockBlackHoleHopper;
import tterrag.supermassivetech.block.BlockBlackHoleStorage;
import tterrag.supermassivetech.item.block.ItemBlockStorage;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
	public static ModBlocks instance = new ModBlocks();
	
	public Block blackHoleStorage;
	public Block blackHoleHopper;

	public void register()
	{
		blackHoleStorage = new BlockBlackHoleStorage();
		GameRegistry.registerBlock(blackHoleStorage, ItemBlockStorage.class, "blackHoleStorage");
		GameRegistry.registerTileEntity(TileBlackHoleStorage.class, "tileBlackHoleStorage");
		
		blackHoleHopper = new BlockBlackHoleHopper();
		GameRegistry.registerBlock(blackHoleHopper, "blackHoleHopper");
		GameRegistry.registerTileEntity(TileBlackHoleHopper.class, "tileBlackHoleHopper");
	}
	
	public void addRecipes(){}
}
