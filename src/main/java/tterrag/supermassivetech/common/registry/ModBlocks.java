package tterrag.supermassivetech.common.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tterrag.supermassivetech.common.block.BlockBlackHole;
import tterrag.supermassivetech.common.block.BlockWaypoint;
import tterrag.supermassivetech.common.block.container.BlockBlackHoleHopper;
import tterrag.supermassivetech.common.block.container.BlockBlackHoleStorage;
import tterrag.supermassivetech.common.block.container.BlockCharger;
import tterrag.supermassivetech.common.block.container.BlockStarHarvester;
import tterrag.supermassivetech.common.item.block.ItemBlockHopper;
import tterrag.supermassivetech.common.item.block.ItemBlockStarHarvester;
import tterrag.supermassivetech.common.item.block.ItemBlockStorage;
import tterrag.supermassivetech.common.tile.TileBlackHole;
import tterrag.supermassivetech.common.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.common.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.common.tile.TileWaypoint;
import tterrag.supermassivetech.common.tile.energy.TileCharger;
import tterrag.supermassivetech.common.tile.energy.TileStarHarvester;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static final ModBlocks instance = new ModBlocks();
    private ModBlocks() {}

    public Block blackHoleStorage;
    public Block blackHoleHopper;
    public Block starHarvester;
    public Block waypoint;
    public Block charger;
    public Block blackHole;

    public void register()
    {
        blackHoleStorage = new BlockBlackHoleStorage();
        GameRegistry.registerBlock(blackHoleStorage, ItemBlockStorage.class, "blackHoleStorage");
        GameRegistry.registerTileEntity(TileBlackHoleStorage.class, "tileBlackHoleStorage");

        blackHoleHopper = new BlockBlackHoleHopper();
        GameRegistry.registerBlock(blackHoleHopper, ItemBlockHopper.class, "blackHoleHopper");
        GameRegistry.registerTileEntity(TileBlackHoleHopper.class, "tileBlackHoleHopper");

        starHarvester = new BlockStarHarvester();
        GameRegistry.registerBlock(starHarvester, ItemBlockStarHarvester.class, "starHarvester");
        GameRegistry.registerTileEntity(TileStarHarvester.class, "tileStarHarvester");

        waypoint = new BlockWaypoint();
        GameRegistry.registerBlock(waypoint, "waypoint");
        GameRegistry.registerTileEntity(TileWaypoint.class, "tileWaypoint");

        charger = new BlockCharger();
        GameRegistry.registerBlock(charger, "charger");
        GameRegistry.registerTileEntity(TileCharger.class, "tileCharger");

        blackHole = new BlockBlackHole();
        GameRegistry.registerBlock(blackHole, "blackHole");
        GameRegistry.registerTileEntity(TileBlackHole.class, "tileBlackHole");
        
        OreDictionary.registerOre("blackHole", blackHole);
    }

    public void addRecipes()
    {
        /* @formatter:off */
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(starHarvester), 
                "iii", 
                "b b", 
                "iii",

                'i', "ingotIron", 
                'b', "barsIron"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blackHoleStorage), 
                "iii", 
                "bsb", 
                "iii",

                'i', "blockIron", 
                'b', "barsIron", 
                's', "itemStar"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blackHoleHopper), 
                "i i", 
                "bsb", 
                "ghg",

                'i', "blockIron", 
                'b', "barsIron", 
                's', "itemStar", 
                'g', "ingotIron", 
                'h', "blockHopper"
        ));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(waypoint), 
                " g ", 
                "isi", 
                "iBi",

                'g', "blockGlass", 
                'i', "ingotIron", 
                's', "itemStar", 
                'B', "blockIron"
        ));
        
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(charger),
                "ibi",
                "brb",
                "ibi",
                
                'b', "barsIron",
                'i', "ingotIron",
                'r', "dustRedstone"
        ));
        /* @formatter:on */
    }
}
