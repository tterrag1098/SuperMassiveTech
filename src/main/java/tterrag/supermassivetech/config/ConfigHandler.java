package tterrag.supermassivetech.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler
{

    public static float maxGravityXZ, maxGravityY, minGravity, range, strength;
    public static boolean doGravityWell;
    public static int gravArmorDrain;
    public static int gravEnchantID;

    public static void init(File file)
    {
        Configuration config = new Configuration(file);

        config.load();

        String section = "Configuration";

        config.addCustomCategoryComment(section, "All double values must be in float range (don't go crazy, your game would crash anyways)");

        doGravityWell = config.get(section, "doGravityWell", true, "Allow blocks to have gravity wells").getBoolean(true);

        strength = (float) config.get(section, "gravityStrength", 0.2, "The overall gravity strength, should be <1 typically, only more if you want to be silly").getDouble(0.2);
        range = (float) config.get(section, "gravityRange", 6.0, "The range of gravity wells").getDouble(6.0);
        maxGravityXZ = (float) config.get(section, "maxGravityXZ", 0.1, "The max gravity that can be applied in the X+Z directions (prevents spikes)").getDouble(0.1);
        maxGravityY = (float) config.get(section, "maxGravityY", 0.028,
                "The max gravity that can be applied in the Y direction, used to allow easier jumping in gravity wells so they are less annoying").getDouble(0.028);
        minGravity = (float) config.get(section, "minGravity", 0, "The minimun force to apply, can be zero. Used to prevent \"wobbling\" near the center of axes").getDouble(0);

        gravArmorDrain = config.get(section, "gravArmorDrain", 100, "The base value that gravity effects passively drain gravity armor").getInt();

        // ids

        gravEnchantID = config.get("enchants", "gravityResistID", 42, "The enchantment ID for Gravity Resist").getInt();

        config.save();
    }
}
