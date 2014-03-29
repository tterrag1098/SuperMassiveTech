package tterrag.supermassivetech.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSMT extends Item
{
	public ItemSMT(String unlocName, String textureName)
	{
		super();

		setCreativeTab(SuperMassiveTech.tabSMT);
		setUnlocalizedName(unlocName);
		setTextureName(Reference.MODID + ":" + textureName);
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,	EntityPlayer player, List list, boolean held) 
	{
		if (this instanceof IAdvancedTooltip)
		{
			IAdvancedTooltip item = (IAdvancedTooltip) this;
			Utils.formAdvancedTooltip(list, stack, item);
		}
	}
}
