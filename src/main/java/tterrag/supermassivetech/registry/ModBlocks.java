package tterrag.supermassivetech.registry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.block.container.BlockBlackHoleHopper;
import tterrag.supermassivetech.block.container.BlockBlackHoleStorage;
import tterrag.supermassivetech.block.container.BlockStarHarvester;
import tterrag.supermassivetech.block.waypoint.BlockWaypoint;
import tterrag.supermassivetech.item.block.ItemBlockHopper;
import tterrag.supermassivetech.item.block.ItemBlockStarHarvester;
import tterrag.supermassivetech.item.block.ItemBlockStorage;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileStarHarvester;
import tterrag.supermassivetech.tile.TileWaypoint;
import cpw.mods.fml.common.registry.GameRegistry;
import static tterrag.supermassivetech.SuperMassiveTech.*;

public class ModBlocks
{
    public static ModBlocks instance = new ModBlocks();

    public Block blackHoleStorage;
    public Block blackHoleHopper;
    public Block starHarvester;
    public Block waypoint;

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
    }

    public void addRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(starHarvester), "iii", "b b", "iii",

        'i', Items.iron_ingot, 'b', Blocks.iron_bars);
        
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
    }
}
