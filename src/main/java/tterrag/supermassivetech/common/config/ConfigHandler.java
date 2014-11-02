package tterrag.supermassivetech.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import tterrag.core.common.event.ConfigFileChangedEvent;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.util.Constants;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FML)
public class ConfigHandler
{
    public static Configuration config;

    public static String sectionGravity = "Gravity Config";
    public static String sectionEnchants = "Enchant IDs";
    public static String sectionArmor = "Gravity Armor Config";
    public static String sectionMisc = "Other Settings";
    public static String sectionTooltips = "Tooltip Settings";
    public static String sectionStars = "Star Settings";

    public static float maxGravityXZ, maxGravityY, minGravity, range, strength;
    public static boolean doGravityWell;
    public static int gravArmorDrain;
    public static int gravEnchantID;
    
    public static int chargerSpeed = 1000;

    public static int fieldRange = 7;
    public static double fieldUsageBase = 3;
    public static boolean fieldIgnorePlayers = false;
    public static boolean doBlocks = true;
    
    public static double starOutputMult = 1.0;
    public static double starStorageMult = 1.0;
    
    public static boolean betterAchievements = true;

    public static boolean forceEnableLootFix = false;
    public static boolean forceDisableLootFix = false;
    public static int biomeLimit = 50;

    public static String tooltipKey1 = "LSHIFT";
    public static String tooltipKey2 = "RSHIFT";
    public static String wailaKey1 = "LCONTROL";
    public static String wailaKey2 = "RCONTROL";

    public static void init(File file)
    {
        config = new Configuration(file);

        doConfiguration();
    }

    public static void doConfiguration()
    {
        /* @formatter:off */
        config.addCustomCategoryComment(sectionGravity, "All double values must be in float range (don't go crazy, your game would crash anyways)");

        doGravityWell = config.get(sectionGravity, "doGravityWell", true, "Allow blocks to have gravity wells").getBoolean(true);

        strength = (float) config.get(sectionGravity, "gravityStrength", 0.2, "The overall gravity strength, should be <1 typically, only more if you want to be silly").getDouble(0.2);
        range = (float) config.get(sectionGravity, "gravityRange", 6.0, "The range of gravity wells").getDouble(6.0);
        maxGravityXZ = (float) config.get(sectionGravity, "maxGravityXZ", 0.1, "The max gravity that can be applied in the X+Z directions (prevents spikes)").getDouble(0.1);
        maxGravityY = (float) config.get(sectionGravity, "maxGravityY", 0.028, "The max gravity that can be applied in the Y direction, used to allow easier jumping in gravity wells so they are less annoying").getDouble(0.028);
        minGravity = (float) config.get(sectionGravity, "minGravity", 0, "The minimun force to apply, can be zero. Used to prevent \"wobbling\" near the center of axes").getDouble(0);
        gravArmorDrain = config.get(sectionGravity, "gravArmorDrain", 100, "The base value that gravity effects passively drain gravity armor").getInt();

        // ids

        gravEnchantID = config.get(sectionEnchants, "gravityResistID", 42, "The enchantment ID for Gravity Resist").getInt();

        // other

        betterAchievements = config.get(sectionMisc, "superDuperFunMode", betterAchievements, "The way the game should have been made.").getBoolean(betterAchievements);
        forceEnableLootFix = config.get(sectionMisc, "forceEnableLootFix", forceEnableLootFix, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will ENABLE it regardless of that fact").getBoolean();
        forceDisableLootFix = config.get(sectionMisc, "forceDisableLootFix", forceEnableLootFix, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will DISABLE it regardless of that fact").getBoolean();
        
        biomeLimit = config.get(sectionMisc, "biomeLimit", biomeLimit, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis config determines the number of biomes at which this happens (Vanilla has 40).").getInt();
        
        if (forceEnableLootFix && forceDisableLootFix)
        {
            throw new ConflictingConfigsException("You cannot force enable and disable loot fix.");
        }
        
        chargerSpeed = config.get(sectionMisc, "chargerSpeed", chargerSpeed, "The rate at which the charger charges items. Max energy input will be 2x this value, energy buffer will be 10x").getInt();
        
        // grav armor

        fieldRange = config.get(sectionArmor, "fieldRange", fieldRange, "The range of the anti-grav field on the gravity armor.").getInt();
        fieldUsageBase = config.get(sectionArmor, "fieldUsageBase", fieldUsageBase, "Base value of the power usage for the field. This is used as an exponent. The forumula is roughly \'(entity height + entity width)^fieldUsageBase\'").getDouble(fieldUsageBase);
        fieldIgnorePlayers = config.get(sectionArmor, "fieldIgnorePlayers", fieldIgnorePlayers, "Whether or not the anti-grav field ignores players.").getBoolean(fieldIgnorePlayers);
        doBlocks = config.get(sectionArmor, "doBlocks", doBlocks, "Whether the anti-grav affects blocks").getBoolean();
        
        // tooltips
        
        tooltipKey1 = config.get(sectionTooltips, "tooltipKey1", tooltipKey1, "The first key that can be pressed to show extended tooltips (this will show as the key to press on the condensed tooltip).").getString().toUpperCase();
        tooltipKey2 = config.get(sectionTooltips, "tooltipKey2", tooltipKey2, "The second key that can be pressed to show extended tooltips. This is hidden and to be used for keys that have right/left versions. Can be the same as key1.").getString().toUpperCase();

        wailaKey1 = config.get(sectionTooltips, "wailaKey1", wailaKey1, "The first key that can be pressed to show WAILA extended tooltips (this will show as the key to press on the condensed tooltip).").getString().toUpperCase();
        wailaKey2 = config.get(sectionTooltips, "wailaKey2", wailaKey2, "The second key that can be pressed to show WAILA extended tooltips. This is hidden and to be used for keys that have right/left versions. Can be the same as key1.").getString().toUpperCase();
        
        starOutputMult = config.get(sectionStars, "starOutputMult", starOutputMult, "The multiplier to apply to the output rate of stars.").getDouble();
        starStorageMult = config.get(sectionStars, "starStorageMult", starStorageMult, "The multiplier to apply to the storage amount of stars.").getDouble();

        config.save();
        /* @formatter:on */
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equals(ModProps.MODID))
        {
            SuperMassiveTech.logger.info("Refreshing config file...");
            doConfiguration();
            Constants.instance().refresh();
        }
    }
    
    @SubscribeEvent
    public void onConfigReload(ConfigFileChangedEvent event)
    {
        if (event.modID.equals(ModProps.MODID))
        {
            SuperMassiveTech.logger.info("Reloading config from file");
            config.load();
            doConfiguration();
            Constants.instance().refresh();
            event.setSuccessful();
        }
    }

    private static class ConflictingConfigsException extends RuntimeException
    {
        private static final long serialVersionUID = -8119730897598783667L;

        public ConflictingConfigsException(String string)
        {
            super(string);
        }
    }
}
