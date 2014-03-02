/**
 * UltimateStorage
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.ultimateStorage;

import java.util.EnumMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import tterrag.ultimateStorage.block.StorageBlock;
import tterrag.ultimateStorage.item.ItemBlockStorage;
import tterrag.ultimateStorage.lib.Reference;
import tterrag.ultimateStorage.network.ChannelHandler;
import tterrag.ultimateStorage.network.UltimateStorageGUIHandler;
import tterrag.ultimateStorage.tile.TileStorageBlock;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Mod with one block that will store all your things.
 * 
 * @author Garrett Spicer-Davis
 */
@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class UltimateStorage {
	
	public static Block storageBlock;
	
	@Instance
	public static UltimateStorage instance;
	
	public static Logger logger = Logger.getLogger("UltimateStorage");
	
	public static EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel("ultimateStorage", new ChannelHandler());
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		register();
	}
	
	@EventHandler
	public static void init(FMLPreInitializationEvent event)
	{
		
	}
	
	private static void register()
	{
		storageBlock = new StorageBlock();
		GameRegistry.registerBlock(storageBlock, ItemBlockStorage.class, "storageBlock");
		GameRegistry.registerTileEntity(TileStorageBlock.class, "tileStorageBlock");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new UltimateStorageGUIHandler());
	}

}
