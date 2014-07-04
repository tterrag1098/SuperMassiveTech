package tterrag.supermassivetech.compat;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictRegistrations
{
    public static void load()
    {
        safeRegister("ingotIron", Items.iron_ingot);
    }
    
    private static void safeRegister(String name, Item item)
    {
        if (OreDictionary.getOres(name).isEmpty())
            OreDictionary.registerOre(name, item);
    }
}
