package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
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
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
	{
		if (isGravityWell() && par3Entity instanceof EntityPlayer && !par2World.isRemote && !((EntityPlayer) par3Entity).capabilities.isCreativeMode)
		{
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 1, 3, true));
			((EntityPlayer) par3Entity).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 1, 3, true));
		}
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
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
	
	public boolean isGravityWell()
	{
		return true;
	}
}
