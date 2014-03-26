package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.util.EnumColor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHopper extends ItemBlockGravity
{

	public ItemBlockHopper(Block block)
	{
		super(block);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer par2EntityPlayer, List list, boolean par4)
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			list.add("Right click with an item to configure");
			list.add("Right click with empty hand to check configuration");
			list.add("Shift right click with an empty hand to clear");
		}
		else
		{
			list.add("Hold" + EnumColor.AQUA + " -Shift- " + EnumColor.WHITE + "for more info");
		}
				
		if (stack.stackTagCompound != null)
		{
			list.add("");

			ItemStack cfg = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory1"));
			ItemStack stored = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory0"));

			if (stored != null)
				list.add(String.format("Stored: %d %s", stored.stackSize, StatCollector.translateToLocal(stored.getUnlocalizedName() + ".name")));
			if (cfg != null)
				list.add(String.format("Configuration: %s", StatCollector.translateToLocal(cfg.getUnlocalizedName() + ".name")));
		}
	}
}
