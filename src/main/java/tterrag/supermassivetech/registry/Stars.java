package tterrag.supermassivetech.registry;

import java.util.HashMap;

import tterrag.supermassivetech.registry.Stars.StarType;

public class Stars {
	
	private static int nextStarID = 0;
	public static Stars instance = new Stars();

	public class StarType
	{
		private String name;
		private int id;
		
		public StarType(String name)
		{
			this.name = name;
			id = nextStarID++;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public int getID()
		{
			return id;
		}
	}
	
	public HashMap<Integer, StarType> types = new HashMap<Integer, StarType>();
	
	public void registerStarType(StarType type)
	{
		types.put(type.getID(), type);
	}
	
	public StarType getTypeByID(int ID)
	{
		return types.get(ID);
	}
	
	public int getIDByType(StarType type)
	{
		return type.getID();
	}

	public StarType getTypeByName(String name) {
		for (StarType s : types.values())
		{
			if (s.toString().equals(name))
				return s;
		}
		return null;
	}
}
