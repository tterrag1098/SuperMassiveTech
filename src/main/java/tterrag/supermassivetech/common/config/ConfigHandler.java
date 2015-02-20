package tterrag.supermassivetech.common.config;

import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import tterrag.core.common.config.AbstractConfigHandler;
import tterrag.supermassivetech.ModProps;

@Handler(HandlerType.FML)
public class ConfigHandler extends AbstractConfigHandler
{
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
    public static double starDeathTrigger = 0.05;

    public static boolean forceEnableLootFix = false;
    public static boolean forceDisableLootFix = false;
    public static int biomeLimit = 50;

    public static String tooltipKey1 = "LSHIFT";
    public static String tooltipKey2 = "RSHIFT";
    public static String wailaKey1 = "LCONTROL";
    public static String wailaKey2 = "RCONTROL";

    public static final ConfigHandler INSTANCE = new ConfigHandler();

    private ConfigHandler()
    {
        super(ModProps.MODID);
    }
    
    @Override
    protected void init()
    {
        addSection(sectionArmor, "armor");
        addSection(sectionEnchants, "enchants");
        addSection(sectionGravity, "gravity");
        addSection(sectionMisc, "misc");
        addSection(sectionStars, "stars");
        addSection(sectionTooltips, "tooltips");
    }

    @Override
    protected void reloadIngameConfigs()
    {
        activateSection(sectionArmor);
        fieldRange = getValue("fieldRange", "The range of the anti-grav field on the gravity armor.", fieldRange);
        fieldUsageBase = getValue("fieldUsageBase", "Base value of the power usage for the field. This is used as an exponent. The forumula is roughly \'(entity height + entity width)^fieldUsageBase\'", fieldUsageBase);
        fieldIgnorePlayers = getValue("fieldIgnorePlayers", "Whether or not the anti-grav field ignores players.", fieldIgnorePlayers);
        doBlocks = getValue("doBlocks", "Whether the anti-grav affects blocks", doBlocks);     

        activateSection(sectionGravity);
        doGravityWell = getValue("doGravityWell", "Allow blocks to have gravity wells", true);
        strength = (float) getValue("gravityStrength", "The overall gravity strength, should be <1 typically, only more if you want to be silly", 0.2f);
        range = (float) getValue("gravityRange", "The range of gravity wells", 6.0f, Bound.of(0f, 1000f));
        maxGravityXZ = (float) getValue("maxGravityXZ", "The max gravity that can be applied in the X+Z directions (prevents spikes)", 0.1f, Bound.of(0f, 100f));
        maxGravityY = (float) getValue("maxGravityY", "The max gravity that can be applied in the Y direction, used to allow easier jumping in gravity wells so they are less annoying", 0.028f, Bound.of(0f, 100f));
        minGravity = (float) getValue("minGravity", "The minimun force to apply, can be zero. Used to prevent \"wobbling\" near the center of axes", 0f, Bound.of(0f, Math.max(maxGravityXZ, maxGravityY)));
        gravArmorDrain = getValue("gravArmorDrain", "The base value that gravity effects passively drain gravity armor", 100);

        activateSection(sectionMisc);
        biomeLimit = getValue("biomeLimit", "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis config determines the number of biomes at which this happens (Vanilla has 40).", biomeLimit);
        forceEnableLootFix = getValue("forceEnableLootFix", "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will ENABLE it regardless of that fact", forceEnableLootFix);
        forceDisableLootFix = getValue("forceDisableLootFix", "This mod will automatically add problematic biome-specific items to loot chests if it detects many biomes.\n\nThis will DISABLE it regardless of that fact", forceEnableLootFix);
        chargerSpeed = getValue("chargerSpeed", "The rate at which the charger charges items. Max energy input will be 2x this value, energy buffer will be 10x", chargerSpeed);

        assert !(forceEnableLootFix && forceDisableLootFix) : "You cannot force enable and disable loot fix.";

        activateSection(sectionTooltips);
        tooltipKey1 = getValue("tooltipKey1", "The first key that can be pressed to show extended tooltips (this will show as the key to press on the condensed tooltip).", tooltipKey1).toUpperCase();
        tooltipKey2 = getValue("tooltipKey2", "The second key that can be pressed to show extended tooltips. This is hidden and to be used for keys that have right/left versions. Can be the same as key1.", tooltipKey2).toUpperCase();
        wailaKey1 = getValue("wailaKey1", "The first key that can be pressed to show WAILA extended tooltips (this will show as the key to press on the condensed tooltip).", wailaKey1).toUpperCase();
        wailaKey2 = getValue("wailaKey2", "The second key that can be pressed to show WAILA extended tooltips. This is hidden and to be used for keys that have right/left versions. Can be the same as key1.", wailaKey2).toUpperCase();

        activateSection(sectionStars);
        starDeathTrigger = getValue("starDeathTrigger", "The percentage of power that must be remaining in the star to begin the death countdown.", starDeathTrigger, Bound.of(0D, 1D));
    }

    @Override
    protected void reloadNonIngameConfigs()
    {
        activateSection(sectionEnchants);
        gravEnchantID = getValue("gravityResistID", "The enchantment ID for Gravity Resist", 42, Bound.of(0, 255));   
        
        activateSection(sectionStars);
        starOutputMult = getValue("starOutputMult", "The multiplier to apply to the output rate of stars.", starOutputMult);
        starStorageMult = getValue("starStorageMult", "The multiplier to apply to the storage amount of stars.", starStorageMult);
    }
}
