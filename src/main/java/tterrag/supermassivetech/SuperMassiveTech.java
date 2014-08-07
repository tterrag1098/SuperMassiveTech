package tterrag.supermassivetech;

import net.minecraft.creativetab.CreativeTabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tterrag.supermassivetech.client.gui.GuiHandler;
import tterrag.supermassivetech.common.CommonProxy;
import tterrag.supermassivetech.common.compat.OreDictRegistrations;
import tterrag.supermassivetech.common.compat.RarityAdjuster;
import tterrag.supermassivetech.common.compat.WailaCompat;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.registry.Achievements;
import tterrag.supermassivetech.common.registry.ModBlocks;
import tterrag.supermassivetech.common.registry.ModEnchants;
import tterrag.supermassivetech.common.registry.ModEntities;
import tterrag.supermassivetech.common.registry.ModItems;
import tterrag.supermassivetech.common.registry.Stars;
import tterrag.supermassivetech.common.util.Constants;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * @author Garrett Spicer-Davis
 */
@Mod(modid = ModProps.MODID, name = ModProps.MOD_NAME, version = ModProps.VERSION, dependencies = "required-after:Forge@10.13.0.1150,);after:Waila", guiFactory = "tterrag.supermassivetech.config.ConfigFactorySMT")
public class SuperMassiveTech
{
    @Instance
    public static SuperMassiveTech instance;

    @SidedProxy(clientSide = ModProps.CLIENT_PROXY_CLASS, serverSide = ModProps.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static Logger logger = LogManager.getLogger("SuperMassiveTech");

    public static ModItems itemRegistry = ModItems.instance;
    public static ModEnchants enchantRegistry = ModEnchants.instance;
    public static ModBlocks blockRegistry = ModBlocks.instance;
    public static ModEntities entityRegistry = ModEntities.instance;
    public static Stars starRegistry = Stars.instance;

    public static CreativeTabs tabSMT = Utils.tab;

    public static int renderIDStorage, renderIDHopper, renderIDStarHarvester, renderIDWaypoint, renderIDBlackHole, renderIDCharger;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.init(event.getSuggestedConfigurationFile());

        Constants.init();
        Utils.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        OreDictRegistrations.load();

        Utils.registerEventHandlers();

        proxy.preInit();

        itemRegistry.register();
        blockRegistry.register();
        entityRegistry.init();

        proxy.registerRenderers();

        enchantRegistry.init();

        Achievements.initAchievements();

        starRegistry.registerDefaultStars();

        if (Loader.isModLoaded("Waila"))
            WailaCompat.load();
    }

    @EventHandler
    public static void init(FMLInitializationEvent event)
    {
        PacketHandler.init();

        itemRegistry.addRecipes();
        blockRegistry.addRecipes();
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event)
    {
        RarityAdjuster.fix();
    }
}
