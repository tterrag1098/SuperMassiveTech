package tterrag.supermassivetech.registry;

import java.util.HashMap;
import static tterrag.supermassivetech.registry.Stars.StarTier.*;

public class Stars
{
	private static int nextStarID = 0;
	public static Stars instance = new Stars();
	
	public enum StarTier
	{
		LOW, 
		NORMAL,
		HIGH,
	}

	/**
	 * Defines a new type of star, all registered star types will have their own
	 * item and a chance to be created when a star heart is used
	 * 
	 * @author Garrett Spicer-Davis
	 */
	public class StarType implements IStar
	{
		private String name;
		private int id;
		private int color, powerMax, powerStored, powerPerTick, fuse;
		StarTier tier;

		/**
		 * Creates a new <code>StarType</code> object
		 * 
		 * @param name - name of the star type
		 * @param tier - {@link StarTier} of this star, low &lt; normal &lt; high, in terms of value
		 * @param color - hex color value
		 * @param powerMax - max amount of power "stored" in this star
		 * @param powerPerTick - max power per tick (in RF) this provides in a star
		 *            harvester
		 * @param fuse - amount of time before exploding once it goes critical
		 */
		public StarType(String name, StarTier tier, int color, int powerMax, int powerPerTick, int fuse)
		{
			this.name = name;
			id = nextStarID++;
			this.tier = tier;
			this.color = color;
			this.powerMax = this.powerStored = powerMax;
			this.powerPerTick = powerPerTick;
			this.fuse = fuse;
		}

		/**
		 * Simply returns the name of this star
		 */
		@Override
		public String toString()
		{
			return name;
		}

		/**
		 * The automatically assigned numerical ID of this star, used for
		 * ordering purposes
		 */
		public int getID()
		{
			return id;
		}

		@Override
		public int getColor()
		{
			return color;
		}
		
		@Override
		public int getPowerStored() 
		{
			return powerStored;
		}
		
		@Override
		public int getPowerStoredMax() 
		{
			return powerMax;
		}

		@Override
		public int getPowerPerTick()
		{
			return powerPerTick;
		}

		@Override
		public int getFuse()
		{
			return fuse;
		}
		
		@Override
		public StarTier getTier()
		{
			return this.tier;
		}
		
		public int getTierOrdinal()
		{
			return this.tier.ordinal();
		}
	}
	
	public static int getNextStarID()
	{
		return nextStarID++;
	}

	public HashMap<Integer, IStar> types = new HashMap<Integer, IStar>();

	/**
	 * Register a new star type, adds it to the items and creation chance
	 * 
	 * @param type - {@link StarType} object to add
	 */
	public void registerStarType(IStar type)
	{
		types.put(type.getID(), type);
	}

	/**
	 * Gets the {@link StarType} object (must be registered) with this ID
	 */
	public IStar getTypeByID(int ID)
	{
		return types.get(ID);
	}

	/**
	 * Gets the ID of the passed {@link StarType} object
	 */
	public int getIDByType(StarType type)
	{
		return type.getID();
	}

	/**
	 * Finds a {@link StarType} object by its <code>String</code> name
	 * 
	 * @param name
	 * @return
	 */
	public IStar getTypeByName(String name)
	{
		for (IStar s : types.values())
		{
			if (s.toString().equals(name))
				return s;
		}
		return null;
	}

	public void registerDefaultStars()
	{
		// TODO balance
		registerStarType(new StarType("Yellow Dwarf", LOW, 0xCCCCAA, 10000000, 80, 600));
		registerStarType(new StarType("Red Dwarf", NORMAL, 0xCC5555, 20000000, 40, 600));
		registerStarType(new StarType("Red Giant", LOW, 0xBB2222, 5000000, 40, 400));
		registerStarType(new StarType("Blue Giant", NORMAL, 0x2222FF, 40000000, 20, 400));
		registerStarType(new StarType("Supergiant", HIGH, 0xFFFFFF, 100000000, 160, 1200));
		registerStarType(new StarType("Brown Dwarf", LOW, 0xAA5522, 2500000, 20, 2400));
		registerStarType(new StarType("White Dwarf", LOW, 0x999999, 5000000, 160, 1200));
		// TODO something awesome registerStarType(new StarType("Neutron", NORMAL, 0x555577, 0, 0)); registerStarType(new StarType("Pulsar", HIGH, 0xFF00FF, 0, 0));
		
		/*
		 *  - pulsars are neutron stars, neutrons are formed INSTEAD of black holes.
		 *  - a critical star could have a chance of forming either of these two, OR a black hole.
		 *  - the advantage of these would be a low power output for an infinite time (think RTG from IC2).
		 *  - however, due to their unstable nature, stacking either of these items would cause a 
		 *  	catastrophic explosion resulting in a black hole...probably obliterating whatever was holding the items. 
		 */
	}
}
