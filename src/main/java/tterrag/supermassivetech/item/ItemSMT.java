package tterrag.supermassivetech.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
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
						String[] ss = s.split("\n");
						for (String line : ss)
						{
							list.add(green ? EnumChatFormatting.GREEN.toString() + line : EnumChatFormatting.WHITE + line);
						}
						green = green ? false : true;
					}
					green = true;
				}
				else
				{
					list.add(EnumChatFormatting.RED + "Hold" + EnumChatFormatting.YELLOW + " -Shift- " + EnumChatFormatting.RED + "for more info");
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
}
