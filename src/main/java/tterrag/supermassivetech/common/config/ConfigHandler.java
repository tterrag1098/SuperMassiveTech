package tterrag.supermassivetech.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.util.Constants;
import tterrag.supermassivetech.common.util.Handler;
import tterrag.supermassivetech.common.util.Handler.HandlerType;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(types = HandlerType.FML)
public class ConfigHandler
{
    public static Configuration config;

    public static String sectionGravity = "Gravity Config";
    public static String sectionEnchants = "Enchant IDs";
    public static String sectionArmor = "Gravity Armor Config";
    public static String sectionMisc = "Other Settings";

    public static float maxGravityXZ, maxGravityY, minGravity, range, strength;
    public static boolean doGravityWell;
    public static int gravArmorDrain;
    public static int gravEnchantID;
    public static boolean showOredictTooltips;

    public static int fieldRange = 7;
    public static double fieldUsageBase = 3;
    public static boolean fieldIgnorePlayers = false;

    public static boolean betterAchievements = true;
    
    public static boolean forceEnableLootFix = false;
    public static boolean forceDisableLootFix = false;
    public static int biomeLimit = 50;

    public static void init(File file)
    {
        config = new Configuration(file);

        doConfiguration();

        config.getCategory(sectionGravity.toLowerCase()).setRequiresWorldRestart(false).setRequiresMcRestart(false);
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

        showOredictTooltips = config.get(sectionMisc, "showOredictionaryTooltip", false, "Shows the oredict registration in the tooltip for every item").getBoolean(true);
        betterAchievements = config.get(sectionMisc, "superDuperFunMode", betterAchievements, "The way the game should have been made.").getBoolean(betterAchievements);
        forceEnableLootFix = config.get(sectionMisc, "forceEnableLootFix", forceEnableLootFix, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will ENABLE it regardless of that fact").getBoolean();
        forceDisableLootFix = config.get(sectionMisc, "forceDisableLootFix", forceEnableLootFix, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will DISABLE it regardless of that fact").getBoolean();
        biomeLimit = config.get(sectionMisc, "biomeLimit", biomeLimit, "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis config determines the number of biomes at which this happens (Vanilla has 40).").getInt();
        
        if (forceEnableLootFix && forceDisableLootFix)
        {
            throw new ConflictingConfigsException("You cannot force enable and disable loot fix.");
        }
        
        // grav armor

        fieldRange = config.get(sectionArmor, "fieldRange", fieldRange, "The range of the anti-grav field on the gravity armor.").getInt();
        fieldUsageBase = config.get(sectionArmor, "fieldUsageBase", fieldUsageBase, "Base value of the power usage for the field. This is used as an exponent. The forumula is roughly \'(entity height + entity width)^fieldUsageBase\'").getDouble(fieldUsageBase);
        fieldIgnorePlayers = config.get(sectionArmor, "fieldIgnorePlayers", fieldIgnorePlayers, "Whether or not the anti-grav field ignores players.").getBoolean(fieldIgnorePlayers);

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
        else
        {
            SuperMassiveTech.logger.info("Not refreshing config file for modid \"" + event.modID + "\"");
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