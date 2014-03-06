/**
 * UltimateStorage
 *
 * @author Garrett Spicer-Davis
 */
package tterrag.supermassivetech;

import java.util.EnumMap;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import tterrag.supermassivetech.block.BlockBlackHoleHopper;
import tterrag.supermassivetech.block.BlockBlackHoleStorage;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.block.ItemBlockStorage;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.ChannelHandler;
import tterrag.supermassivetech.network.GuiHandler;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.util.Constants;
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
public class UltimateStorage
{

	public static Block blackHoleStorage;
	public static Block blackHoleHopper;
	public static Item star;

	@Instance
	public static UltimateStorage instance;

	public static Logger logger = Logger.getLogger("SuperMassiveTech");

	public static EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel("ultimateStorage", new ChannelHandler());

	public CreativeTabs creativeTab = new CreativeTabs(CreativeTabs.getNextID(), "Ultimate Storage")
	{
		@Override
		public Item getTabIconItem()
		{
			return UltimateStorage.blackHoleStorage.getItem(null, 0, 0, 0);
		}
	};
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		Constants.init();
		register();
	}

	@EventHandler
	public static void init(FMLPreInitializationEvent event)
	{

	}

	private static void register()
	{
		blackHoleStorage = new BlockBlackHoleStorage();
		GameRegistry.registerBlock(blackHoleStorage, ItemBlockStorage.class, "blackHoleStorage");
		GameRegistry.registerTileEntity(TileBlackHoleStorage.class, "tileBlackHoleStorage");
		
		blackHoleHopper = new BlockBlackHoleHopper();
		GameRegistry.registerBlock(blackHoleHopper, "blackHoleHopper");
		GameRegistry.registerTileEntity(TileBlackHoleHopper.class, "tileBlackHoleHopper");
		
		star = new ItemStar("star");
		GameRegistry.registerItem(star, "star");

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
	}

}
