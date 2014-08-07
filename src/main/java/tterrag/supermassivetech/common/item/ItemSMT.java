package tterrag.supermassivetech.common.item;

import net.minecraft.item.Item;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.SuperMassiveTech;

public class ItemSMT extends Item
{
    public ItemSMT(String unlocName, String textureName)
    {
        super();

        setCreativeTab(SuperMassiveTech.tabSMT);
        setUnlocalizedName(unlocName);
        setTextureName(ModProps.MODID + ":" + textureName);
    }
}
