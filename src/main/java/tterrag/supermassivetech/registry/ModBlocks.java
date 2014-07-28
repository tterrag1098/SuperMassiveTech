package tterrag.supermassivetech.registry;

import static tterrag.supermassivetech.SuperMassiveTech.*;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.block.BlockBlackHole;
import tterrag.supermassivetech.block.BlockWaypoint;
import tterrag.supermassivetech.block.container.BlockBlackHoleHopper;
import tterrag.supermassivetech.block.container.BlockBlackHoleStorage;
import tterrag.supermassivetech.block.container.BlockCharger;
import tterrag.supermassivetech.block.container.BlockStarHarvester;
import tterrag.supermassivetech.item.block.ItemBlockHopper;
import tterrag.supermassivetech.item.block.ItemBlockStarHarvester;
import tterrag.supermassivetech.item.block.ItemBlockStorage;
import tterrag.supermassivetech.tile.TileBlackHole;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileWaypoint;
import tterrag.supermassivetech.tile.energy.TileCharger;
import tterrag.supermassivetech.tile.energy.TileStarHarvester;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static final ModBlocks instance = new ModBlocks();

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
    }

    public void addRecipes()
    {
        /* @formatter:off */
        GameRegistry.addRecipe(new ItemStack(starHarvester), 
                "iii", 
                "b b", 
                "iii",

                'i', Items.iron_ingot, 
                'b', Blocks.iron_bars
        );

        GameRegistry.addRecipe(new ItemStack(blackHoleStorage), 
                "iii", 
                "bsb", 
                "iii",

                'i', Blocks.iron_block, 
                'b', Blocks.iron_bars, 
                's', itemRegistry.star
        );

        GameRegistry.addRecipe(new ItemStack(blackHoleStorage), 
                "iii", 
                "bsb", 
                "iii",

                'i', Blocks.iron_block, 
                'b', Blocks.iron_bars, 
                's', itemRegistry.starSpecial
        );

        GameRegistry.addRecipe(new ItemStack(blackHoleHopper), 
                "i i", 
                "bsb", 
                "ghg",

                'i', Blocks.iron_block, 
                'b', Blocks.iron_bars, 
                's', itemRegistry.star, 
                'g', Items.iron_ingot, 
                'h', Blocks.hopper
        );

        GameRegistry.addRecipe(new ItemStack(blackHoleHopper), 
                "i i", 
                "bsb", 
                "ghg",

                'i', Blocks.iron_block, 
                'b', Blocks.iron_bars, 
                's', itemRegistry.starSpecial, 
                'g', Items.iron_ingot, 
                'h', Blocks.hopper
        );

        GameRegistry.addRecipe(new ItemStack(waypoint), 
                " g ", 
                "isi", 
                "iBi",

                'g', Blocks.glass, 
                'i', Items.iron_ingot, 
                's', itemRegistry.star, 
                'B', Blocks.iron_block
        );
        
        GameRegistry.addRecipe(new ItemStack(charger),
                "ibi",
                "brb",
                "ibi",
                
                'b', Blocks.iron_bars,
                'i', Items.iron_ingot,
                'r', Items.redstone
        );
        /* @formatter:on */
    }
}
