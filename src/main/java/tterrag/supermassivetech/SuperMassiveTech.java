package tterrag.supermassivetech;

import java.util.EnumMap;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.ChannelHandler;
import tterrag.supermassivetech.proxy.CommonProxy;
import tterrag.supermassivetech.registry.ModBlocks;
import tterrag.supermassivetech.registry.ModItems;
import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

/** 
 * @author Garrett Spicer-Davis
 */
@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class SuperMassiveTech
{
	@Instance
	public static SuperMassiveTech instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static Logger logger = Logger.getLogger("SuperMassiveTech");

	public static EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel("ultimateStorage", new ChannelHandler());

	public static ModItems itemRegistry = ModItems.instance;
	public static ModBlocks blockRegistry = ModBlocks.instance;
	
	public static CreativeTabs tabSMT = Utils.tab;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		Constants.init();
		
		itemRegistry.register();
		itemRegistry.addRecipes();
		
		blockRegistry.register();
		blockRegistry.addRecipes();
		
		proxy.registerGuis();
	}

	@EventHandler
	public static void init(FMLPreInitializationEvent event){}
}
