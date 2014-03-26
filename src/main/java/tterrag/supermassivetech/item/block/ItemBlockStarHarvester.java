package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.util.EnumColor;
import tterrag.supermassivetech.util.Utils;

public class ItemBlockStarHarvester extends ItemBlockGravity
{

	public ItemBlockStarHarvester(Block block)
	{
		super(block);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add(EnumColor.BRIGHT_GREEN + "- Harvests the power of stars!");
			list.add(EnumColor.WHITE + "- Outputs at the rate of the star inside.");
			list.add(EnumColor.BRIGHT_GREEN + "- Once the star is out of power");
			list.add(EnumColor.BRIGHT_GREEN + "   it will go critical. Better run!");
		}
		else
		{
			list.add(EnumColor.RED + "Hold" + EnumColor.YELLOW + " -Shift- " + EnumColor.RED + "for more info");
		}
		
		if (stack.stackTagCompound != null)
		{
			if (stack.stackTagCompound.getTag("inventory") != null)
			{
				list.add("");
				ItemStack star = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory"));
				list.add(String.format("Stored: %s", Utils.getType(star).toString()));
				list.add(String.format("Power Remaining: %s", Utils.formatString("", " RF", Utils.getStarPowerRemaining(star), false)));
			}
		}
	}
}
