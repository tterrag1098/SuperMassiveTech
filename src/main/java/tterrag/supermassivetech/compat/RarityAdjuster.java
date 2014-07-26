package tterrag.supermassivetech.compat;

import static net.minecraftforge.common.ChestGenHooks.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import tterrag.supermassivetech.config.ConfigHandler;

public class RarityAdjuster
{
    public static void fix()
    {
        if (ConfigHandler.forceEnableLootFix || areBiomesRegistered(1))
        {
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.cactus), 0, 6, 10, 2));
            addItem(MINESHAFT_CORRIDOR, new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.cactus), 0, 6, 10, 2));
            addItem(DUNGEON_CHEST, new WeightedRandomChestContent(Items.dye, 4, 5, 5, 1));
        }
    }

    private static boolean areBiomesRegistered(int amount)
    {
        int count = 0;
        for (BiomeGenBase b : BiomeGenBase.getBiomeGenArray())
        {
            if (b != null)
            {
                count++;
            }
        }
        return count >= amount;
    }
}
