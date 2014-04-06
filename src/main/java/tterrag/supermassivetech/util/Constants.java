package tterrag.supermassivetech.util;

import tterrag.supermassivetech.config.ConfigHandler;

public class Constants
{
	private static Constants instance = new Constants();

	public static Constants instance()
	{
		return instance;
	}

	public final float RANGE;
	public final float STRENGTH;
	public final float MAX_GRAV_XZ, MAX_GRAV_Y, MIN_GRAV;
	public final int ENERGY_DRAIN;

	private Constants()
	{
		RANGE = Math.min(1000, ConfigHandler.range);
		MAX_GRAV_XZ = Math.min(1, ConfigHandler.maxGravityXZ);
		MAX_GRAV_Y = Math.min(1, ConfigHandler.maxGravityY);
		MIN_GRAV = Math.min(Math.min(MAX_GRAV_XZ, MAX_GRAV_Y), ConfigHandler.minGravity);
		STRENGTH = Math.min(1000, ConfigHandler.strength);
		ENERGY_DRAIN = Math.min(1000, ConfigHandler.gravArmorDrain);
	}

	public static void init()
	{
		instance = new Constants();
	}
}
