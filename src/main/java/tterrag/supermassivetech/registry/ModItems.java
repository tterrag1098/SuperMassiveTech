package tterrag.supermassivetech.registry;

import tterrag.supermassivetech.item.ItemStar;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems
{
	public static ModItems instance = new ModItems();
	
	public Item star; 
	public Item heartOfStar;
	
	public void register()
	{
		star = new ItemStar("star");
		heartOfStar = new ItemStar("starHeart");
		
		GameRegistry.registerItem(star, "star");
		GameRegistry.registerItem(heartOfStar, "starHeart");
	}
	
	public void addRecipes()
	{
		
	}
}	
