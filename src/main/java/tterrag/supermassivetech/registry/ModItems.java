package tterrag.supermassivetech.registry;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.item.ItemGravityArmor.ArmorType;
import tterrag.supermassivetech.item.ItemSMT;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.ItemStarHeart;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems
{
	public static ModItems instance = new ModItems();

	public Item star;
	public Item heartOfStar;
	
	public Item starContainer;
	
	public Item gravityHelm, gravityChest, gravityLegs, gravityBoots;
	
	public ArrayList<Item> armors = new ArrayList<Item>();
	
	public void register()
	{
		heartOfStar = new ItemStarHeart("starHeart");
		star = new ItemStar("star");
		starContainer = new ItemSMT("starContainer", "starContainer");
		
		gravityHelm = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.HELMET);
		gravityChest = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.CHESTPLATE);
		gravityLegs = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.LEGS);
		gravityBoots = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.BOOTS);

		GameRegistry.registerItem(star, "star");
		GameRegistry.registerItem(heartOfStar, "starHeart");
		GameRegistry.registerItem(starContainer, "starContainer");
		
		GameRegistry.registerItem(gravityHelm, "gravityHelmet");
		GameRegistry.registerItem(gravityChest, "gravityChestplate");
		GameRegistry.registerItem(gravityLegs, "gravityLeggings");
		GameRegistry.registerItem(gravityBoots, "gravityBoots");
		
		armors.add(gravityHelm);
		armors.add(gravityChest);
		armors.add(gravityLegs);
		armors.add(gravityBoots);
	}

	public void addRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(heartOfStar), 
				"GRG",
				"RSR",
				"GRG",

				'G', Items.glowstone_dust, 
				'R', Items.redstone, 
				'S', Items.nether_star
		);
		
		GameRegistry.addRecipe(new ItemStack(starContainer), 
				"iOi",
				"d d",
				"iOi",
				
				'i', Items.iron_ingot,
				'O', Blocks.obsidian,
				'd', Items.diamond
		);
	}
}
