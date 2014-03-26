package tterrag.supermassivetech.item;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.EnumColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSMT extends Item
{
	public ItemSMT(String unlocName, String textureName)
	{
		super();

		setCreativeTab(SuperMassiveTech.tabSMT);
		setUnlocalizedName(unlocName);
		setTextureName(Reference.MODID + ":" + textureName);
	}
	
	private boolean green = true;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,	EntityPlayer player, List list, boolean held) 
	{
		if (this instanceof IAdvancedTooltip)
		{
			IAdvancedTooltip item = (IAdvancedTooltip) this;
			
			if (item.getHiddenLines(stack) != null)
			{
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
				{
					for (String s : item.getHiddenLines(stack))
					{
						list.add(green ? EnumColor.BRIGHT_GREEN : EnumColor.WHITE + s);
						green = green ? false : true;
					}
				}
				else
				{
					list.add(EnumColor.RED + "Hold" + EnumColor.YELLOW + " -Shift- " + EnumColor.RED + "for more info");
				}
			}
			
			if (item.getStaticLines(stack) != null)
			{
				list.add("");
				
				for (String s : item.getStaticLines(stack))
					list.add(s);
			}
		}
	}
	
	/**
	 * Implementation to avoid redundancy in subclasses. {@link ItemSMT} does NOT implement {@link IAdvancedTooltip}
	 */
	public boolean colorHiddenLines(ItemStack stack) 
	{
		return true;
	}
}
