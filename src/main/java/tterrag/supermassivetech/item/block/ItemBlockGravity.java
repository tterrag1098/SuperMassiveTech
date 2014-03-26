package tterrag.supermassivetech.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import tterrag.supermassivetech.item.IAdvancedTooltip;

public class ItemBlockGravity extends ItemBlock
{
	public ItemBlockGravity(Block block)
	{
		super(block);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (par3Entity instanceof EntityPlayer && !par2World.isRemote && !((EntityPlayer) par3Entity).capabilities.isCreativeMode)
		{
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, 3, true));
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, 3, true));
		}
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
	}
	
	/**
	 * Implementation to avoid redundancy in subclasses. {@link ItemBlockGravity} does NOT implement {@link IAdvancedTooltip}
	 */
	public boolean colorHiddenLines(ItemStack stack) 
	{
		return true;
	}
}
