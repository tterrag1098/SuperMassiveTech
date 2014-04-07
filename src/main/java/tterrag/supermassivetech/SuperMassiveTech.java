package tterrag.supermassivetech;

import java.util.EnumMap;
import java.util.logging.Logger;

import net.minecraft.creativetab.CreativeTabs;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.item.GravityArmorHandler;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.ChannelHandler;
import tterrag.supermassivetech.proxy.CommonProxy;
import tterrag.supermassivetech.registry.ModBlocks;
import tterrag.supermassivetech.registry.ModItems;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.util.Constants;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * @author Garrett Spicer-Davis
 */
@Mod(modid = Reference.MODID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = "after:ThermalExpansion")
public class SuperMassiveTech
{
	@Instance
	public static SuperMassiveTech instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	public static Logger logger = Logger.getLogger("SuperMassiveTech");

	public static ChannelHandler channelHandler = new ChannelHandler();
	public static EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel("ultimateStorage", channelHandler);

	public static ModItems itemRegistry = ModItems.instance;
	public static ModBlocks blockRegistry = ModBlocks.instance;
	public static Stars starRegistry = Stars.instance;

	public static CreativeTabs tabSMT = Utils.tab;

	public static int renderIDStorage, renderIDHopper, renderIDStarHarvester;

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event)
	{
		ConfigHandler.init(event.getSuggestedConfigurationFile());
		Constants.init();
		Utils.init();
		
		proxy.registerGuis();
		proxy.registerRenderers();

		itemRegistry.register();
		itemRegistry.addRecipes();

		blockRegistry.register();
		blockRegistry.addRecipes();

		starRegistry.registerDefaultStars();

		ChannelHandler.init();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		proxy.init();
		
		Utils.registerEventHandlers(GravityArmorHandler.class);
	}
}
