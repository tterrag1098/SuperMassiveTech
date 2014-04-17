package tterrag.supermassivetech.registry;

import static tterrag.supermassivetech.SuperMassiveTech.itemRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.block.container.BlockBlackHoleHopper;
import tterrag.supermassivetech.block.container.BlockBlackHoleStorage;
import tterrag.supermassivetech.block.container.BlockStarHarvester;
import tterrag.supermassivetech.item.block.ItemBlockHopper;
import tterrag.supermassivetech.item.block.ItemBlockStarHarvester;
import tterrag.supermassivetech.item.block.ItemBlockStorage;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileStarHarvester;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks
{
    public static ModBlocks instance = new ModBlocks();

    public Block blackHoleStorage;
    public Block blackHoleHopper;
    public Block starHarvester;

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
    }

    public void addRecipes()
    {
        GameRegistry.addRecipe(new ItemStack(starHarvester), "iii", "bCb", "iii",

        'i', Items.iron_ingot, 'b', Blocks.iron_bars, 'C', itemRegistry.starContainer);
    }
}
