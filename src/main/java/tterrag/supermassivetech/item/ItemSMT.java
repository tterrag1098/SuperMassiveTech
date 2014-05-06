package tterrag.supermassivetech.item;

import net.minecraft.item.Item;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;

public class ItemSMT extends Item
{
    public ItemSMT(String unlocName, String textureName)
    {
        super();

        setCreativeTab(SuperMassiveTech.tabSMT);
        setUnlocalizedName(unlocName);
        setTextureName(Reference.MODID + ":" + textureName);
    }
}
