/**
 * UltimateStorage
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage;

import net.minecraft.block.Block;
import tterrag.ultimateStorage.block.StorageBlock;
import tterrag.ultimateStorage.lib.Reference;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Mod with one block that will store all your things.
 * 
 * @author Garrett Spicer-Davis
 */
@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class UltimateStorage {
	
	public static Block storageBlock;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		registerBlock();
	}
	
	@EventHandler
	public static void init(FMLPreInitializationEvent event)
	{
		
	}
	
	private static void registerBlock()
	{
		storageBlock = new StorageBlock();
		GameRegistry.registerBlock(storageBlock, "storageBlock");
	}

}
