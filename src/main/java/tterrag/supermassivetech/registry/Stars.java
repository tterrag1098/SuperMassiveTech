package tterrag.supermassivetech.registry;

import java.util.HashMap;

public class Stars
{
	private static int nextStarID = 0;
	public static Stars instance = new Stars();

	/**
	 * Defines a new type of star, all registered star types will have their own
	 * item and a chance to be created when a star heart is used
	 * 
	 * @author Garrett Spicer-Davis
	 */
	public class StarType
	{
		private String name;
		private int id;
		public int color, maxPower, fuse;

		/**
		 * Creates a new <code>StarType</code> object
		 * 
		 * @param name - name of the star type
		 * @param color - hex color value
		 * @param maxPower - max power per tick (in RF) this provides in a star
		 *            harvester
		 * @param fuse - amount of time before exploding once it goes critical
		 */
		public StarType(String name, int color, int maxPower, int fuse)
		{
			this.name = name;
			id = nextStarID++;
			this.color = color;
			this.maxPower = maxPower;
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
	}

	public HashMap<Integer, StarType> types = new HashMap<Integer, StarType>();

	/**
	 * Register a new star type, adds it to the items and creation chance
	 * 
	 * @param type - {@link StarType} object to add
	 */
	public void registerStarType(StarType type)
	{
		types.put(type.getID(), type);
	}

	/**
	 * Gets the {@link StarType} object (must be registered) with this ID
	 */
	public StarType getTypeByID(int ID)
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
	public StarType getTypeByName(String name)
	{
		for (StarType s : types.values())
		{
			if (s.toString().equals(name))
				return s;
		}
		return null;
	}

	public void registerDefaultStars()
	{
		// TODO figure out values
		registerStarType(new StarType("Yellow Dwarf", 0xCCCCAA, 0, 0));
		registerStarType(new StarType("Red Dwarf", 0xCC5555, 0, 0));
		registerStarType(new StarType("Red Giant", 0xBB2222, 0, 0));
		registerStarType(new StarType("Blue Giant", 0x2222FF, 0, 0));
		registerStarType(new StarType("Supergiant", 0xFFFFFF, 0, 0));
		registerStarType(new StarType("Brown Dwarf", 0xAA5522, 0, 0));
		registerStarType(new StarType("White Dwarf", 0x999999, 0, 0));
		registerStarType(new StarType("Neutron", 0x555577, 0, 0));
		registerStarType(new StarType("Pulsar", 0xFF00FF, 0, 0));
	}
}
