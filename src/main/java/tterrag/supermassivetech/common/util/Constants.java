package tterrag.supermassivetech.common.util;

import tterrag.supermassivetech.common.config.ConfigHandler;

public class Constants
{
    private static final Constants instance = new Constants();

    public static Constants instance()
    {
        return instance;
    }

    private float range;
    private float strength;
    private float maxGravXZ, maxGravY, minGrav;
    private int energyDrain;
    private float starDeathTrigger = 0.05f; // percent remaining fuel to trigger collapse

    private Constants()
    {
        refresh();
    }

    public static void init()
    {
        instance.refresh();
    }

    public void refresh()
    {
        range = Math.min(1000, ConfigHandler.range);
        maxGravXZ = Math.min(1, ConfigHandler.maxGravityXZ);
        maxGravY = Math.min(1, ConfigHandler.maxGravityY);
        minGrav = Math.min(Math.min(maxGravXZ, maxGravY), ConfigHandler.minGravity);
        strength = Math.min(1000, ConfigHandler.strength);
        energyDrain = Math.min(1000, ConfigHandler.gravArmorDrain);
    }

    public float getRange()
    {
        return range;
    }

    public float getStrength()
    {
        return strength;
    }

    public float getMaxGravXZ()
    {
        return maxGravXZ;
    }

    public float getMaxGravY()
    {
        return maxGravY;
    }

    public float getMinGrav()
    {
        return minGrav;
    }

    public int getEnergyDrain()
    {
        return energyDrain;
    }

    public float getStarDeathTrigger()
    {
        return starDeathTrigger;
    }
}
