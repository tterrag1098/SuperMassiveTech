package tterrag.supermassivetech.compat;

import net.minecraft.init.Items;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictRegistrations
{
    public static void load()
    {
        if (OreDictionary.getOres("ingotIron").isEmpty())
            OreDictionary.registerOre("ingotIron", Items.iron_ingot);
    }
}
