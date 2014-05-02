package tterrag.supermassivetech.registry;

import java.util.ArrayList;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.ItemDepletedNetherStar;
import tterrag.supermassivetech.item.ItemSMT;
import tterrag.supermassivetech.item.ItemStar;
import tterrag.supermassivetech.item.ItemStarHeart;
import tterrag.supermassivetech.item.ItemStarSpecial;
import tterrag.supermassivetech.item.armor.ItemGravityArmor;
import tterrag.supermassivetech.item.armor.ItemGravityArmor.ArmorType;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems
{
    public static ModItems instance = new ModItems();

    public Item star, starSpecial;
    public Item heartOfStar;
    
    public ItemDepletedNetherStar depletedNetherStar;

    public Item starContainer;

    public Item gravityHelm, gravityChest, gravityLegs, gravityBoots;

    public ArrayList<IEnergyContainerItem> armors = new ArrayList<IEnergyContainerItem>();

    public void register()
    {
        heartOfStar = new ItemStarHeart("starHeart");
        star = new ItemStar("star");
        starSpecial = new ItemStarSpecial("starSpecial");        
        starContainer = new ItemSMT("starContainer", "starContainer");
        depletedNetherStar = new ItemDepletedNetherStar();

        gravityHelm = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.HELMET);
        gravityChest = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.CHESTPLATE);
        gravityLegs = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.LEGS);
        gravityBoots = new ItemGravityArmor(ArmorMaterial.DIAMOND, ArmorType.BOOTS);

        GameRegistry.registerItem(star, "star");
        GameRegistry.registerItem(starSpecial, "starSpecial");
        GameRegistry.registerItem(heartOfStar, "starHeart");
        GameRegistry.registerItem(starContainer, "starContainer");
        GameRegistry.registerItem(depletedNetherStar, "depletedNetherStar");

        GameRegistry.registerItem(gravityHelm, "gravityHelmet");
        GameRegistry.registerItem(gravityChest, "gravityChestplate");
        GameRegistry.registerItem(gravityLegs, "gravityLeggings");
        GameRegistry.registerItem(gravityBoots, "gravityBoots");

        armors.add((IEnergyContainerItem) gravityHelm);
        armors.add((IEnergyContainerItem) gravityChest);
        armors.add((IEnergyContainerItem) gravityLegs);
        armors.add((IEnergyContainerItem) gravityBoots);
    }

    public void addRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(heartOfStar), "GRG", "RSR", "GRG",

        'G', Items.glowstone_dust, 'R', Items.redstone, 'S', Items.nether_star);

        GameRegistry.addRecipe(new ItemStack(starContainer), "iOi", "d d", "iOi",

        'i', Items.iron_ingot, 'O', Blocks.obsidian, 'd', Items.diamond);
    }
}
