package tterrag.supermassivetech.common.registry;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tterrag.supermassivetech.common.item.ItemDepletedNetherStar;
import tterrag.supermassivetech.common.item.ItemGravityArmor;
import tterrag.supermassivetech.common.item.ItemGravityArmor.ArmorType;
import tterrag.supermassivetech.common.item.ItemSMT;
import tterrag.supermassivetech.common.item.ItemStar;
import tterrag.supermassivetech.common.item.ItemStarHeart;
import tterrag.supermassivetech.common.item.ItemStarSpecial;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems
{
    public static final ModItems instance = new ModItems();

    public Item star, starSpecial;
    public Item heartOfStar;

    public ItemDepletedNetherStar depletedNetherStar;

    public Item starContainer;

    public ItemGravityArmor gravityHelm, gravityChest, gravityLegs, gravityBoots;

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

        armors.add(gravityHelm);
        armors.add(gravityChest);
        armors.add(gravityLegs);
        armors.add(gravityBoots);
        
        OreDictionary.registerOre("itemStar", star);
        OreDictionary.registerOre("itemStar", starSpecial);
        OreDictionary.registerOre("itemStarContainer", starContainer);
        OreDictionary.registerOre("itemStarHeart", heartOfStar);
        OreDictionary.registerOre("itemDepletedNetherStar", new ItemStack(depletedNetherStar, 1, OreDictionary.WILDCARD_VALUE));
    }

    public void addRecipes()
    {
        /* @formatter:off */
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(heartOfStar), 
                "RGR", 
                "DSD", 
                "RGR",

                'G', OreDictionary.getOres("dustGold").isEmpty() ? "dustGlowstone" : "dustGold", 
                'R', "dustRedstone",
                'S', "itemNetherStar",
                'D', OreDictionary.getOres("dustDiamond").isEmpty() ? "gemDiamond" : "dustDiamond")
        );

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(starContainer), 
                "iOi", 
                "d d", 
                "iOi",

                'i', "ingotIron",
                'O', "blockObsidian", 
                'd', "gemDiamond"
        ));

        String armorMat = OreDictionary.getOres("ingotSteel").isEmpty() ? "ingotIron" : "ingotSteel";

        GameRegistry.addRecipe(new ShapedOreRecipe(gravityHelm.create(), 
                "InI", 
                "I I",

                'I', armorMat, 
                'n', "itemDepletedNetherStar"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(gravityChest.create(), 
                "I I", 
                "InI", 
                "III",

                'I', armorMat, 
                'n', "itemDepletedNetherStar"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(gravityLegs.create(), 
                "InI", 
                "I I", 
                "I I",

                'I', armorMat, 
                'n', "itemDepletedNetherStar"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(gravityBoots.create(), 
                "I I", 
                "InI",

                'I', armorMat, 
                'n', "itemDepletedNetherStar"
        ));
        /* @formatter:on */
    }
}
