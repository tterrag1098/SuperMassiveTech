package tterrag.supermassivetech;

import net.minecraft.creativetab.CreativeTabs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tterrag.core.common.compat.CompatabilityRegistry;
import tterrag.core.common.util.CreativeTabsCustom;
import tterrag.core.common.util.RegisterTime;
import tterrag.supermassivetech.client.gui.GuiHandler;
import tterrag.supermassivetech.common.CommonProxy;
import tterrag.supermassivetech.common.compat.RarityAdjuster;
import tterrag.supermassivetech.common.compat.enderio.EnderIOCompat;
import tterrag.supermassivetech.common.compat.waila.WailaCompat;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.registry.Achievements;
import tterrag.supermassivetech.common.registry.BlackHoleEnergyRegistry;
import tterrag.supermassivetech.common.registry.ModBlocks;
import tterrag.supermassivetech.common.registry.ModEnchants;
import tterrag.supermassivetech.common.registry.ModEntities;
import tterrag.supermassivetech.common.registry.ModItems;
import tterrag.supermassivetech.common.registry.Stars;
import tterrag.supermassivetech.common.util.Constants;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * @author Garrett Spicer-Davis
 */
@Mod(modid = ModProps.MODID, name = ModProps.MOD_NAME, version = ModProps.VERSION, dependencies = ModProps.DEPENDENCIES, guiFactory = ModProps.GUI_FACTORY_CLASS)
public class SuperMassiveTech
{
    @Instance
    public static SuperMassiveTech instance;

    @SidedProxy(clientSide = ModProps.CLIENT_PROXY_CLASS, serverSide = ModProps.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger("SuperMassiveTech");

    public static final ModItems itemRegistry = ModItems.instance;
    public static final ModEnchants enchantRegistry = ModEnchants.instance;
    public static final ModBlocks blockRegistry = ModBlocks.instance;
    public static final ModEntities entityRegistry = ModEntities.instance;
    public static final Stars starRegistry = Stars.instance;

    public static final CreativeTabs tabSMT = new CreativeTabsCustom(ModProps.MODID, itemRegistry.heartOfStar);

    public static int renderIDStorage, renderIDHopper, renderIDStarHarvester, renderIDWaypoint, renderIDBlackHole, renderIDCharger;

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        ConfigHandler.init(event.getSuggestedConfigurationFile());

        Constants.init();
        Utils.init();

        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        proxy.preInit();

        itemRegistry.register();
        blockRegistry.register();
        entityRegistry.init();

        proxy.registerRenderers();

        enchantRegistry.init();

        Achievements.initAchievements();

        starRegistry.registerDefaultStars();
        BlackHoleEnergyRegistry.INSTANCE.registerDefaults();

        FMLInterModComms.sendMessage("Waila", "register", WailaCompat.class.getName() + ".load");
        
        CompatabilityRegistry.instance().registerCompat("EnderIO", RegisterTime.INIT, EnderIOCompat.class);
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
