package tterrag.supermassivetech.registry;

import tterrag.supermassivetech.entity.EntityStarHeart;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.ItemStarHeart;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems
{
	public static ModItems instance = new ModItems();
	
	public Item star; 
	public Item heartOfStar;
	
	public void register()
	{
		heartOfStar = new ItemStarHeart("starHeart");
		star = new ItemStar("star");
		
		GameRegistry.registerItem(star, "star");
		GameRegistry.registerItem(heartOfStar, "starHeart");
	}
	
	public void addRecipes()
	{
		
	}
}	
