package tterrag.supermassivetech.registry;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.ItemStarHeart;
import cpw.mods.fml.common.registry.GameRegistry;

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
		GameRegistry.addRecipe(new ItemStack(SuperMassiveTech.itemRegistry.heartOfStar), 
				"GRG",
				"RSR",
				"GRG",
				
				'G', Items.glowstone_dust,
				'R', Items.redstone,
				'S', Items.nether_star
		);
	}
}	
