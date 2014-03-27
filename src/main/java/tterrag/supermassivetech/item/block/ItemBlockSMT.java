package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.item.IAdvancedTooltip;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockSMT extends ItemBlock
{
	public ItemBlockSMT(Block block)
	{
		super(block);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (isGravityWell() && par3Entity instanceof EntityPlayer && !par2World.isRemote && !((EntityPlayer) par3Entity).capabilities.isCreativeMode)
		{
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, 3, true));
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, 3, true));
		}
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}

	private boolean green = true;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held)
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

	/**
	 * Implementation to avoid redundancy in subclasses.
	 * {@link ItemBlockSMT} does NOT implement {@link IAdvancedTooltip}
	 */
	public boolean colorHiddenLines(ItemStack stack)
	{
		return true;
	}
	
	public boolean isGravityWell()
	{
		return true;
	}
}
