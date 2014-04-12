package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.item.IAdvancedTooltip;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockSMT extends ItemBlock
{
	public ItemBlockSMT(Block block)
	{
		super(block);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
	{
		if (isGravityWell(stack) && entity instanceof EntityPlayer && !world.isRemote && !((EntityPlayer) entity).capabilities.isCreativeMode)
		{
			Utils.applyGravPotionEffects((EntityPlayer) entity, getGravStrength(stack));
		}
		super.onUpdate(stack, world, entity, par4, par5);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean held)
	{
		if (this instanceof IAdvancedTooltip)
		{
			IAdvancedTooltip item = (IAdvancedTooltip) this;
			Utils.formAdvancedTooltip(list, stack, item);
		}
	}
	
	public boolean isGravityWell(ItemStack stack)
	{
		return true;
	}

	public int getGravStrength(ItemStack stack)
	{
		return 3;
	}
}
