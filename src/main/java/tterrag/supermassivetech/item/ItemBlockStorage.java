package tterrag.supermassivetech.item;

import java.util.List;

import tterrag.supermassivetech.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class ItemBlockStorage extends ItemBlock
{
	public ItemBlockStorage(Block block)
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if (stack.stackTagCompound != null)
		{
			list.add("Stored: "); 
			
			if (stack.stackTagCompound.getTag("itemStack") != null)
				list.add(Utils.formatString("", stack.stackTagCompound.getLong("itemsStored"), false, true) + " " + StatCollector.translateToLocal(ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("itemStack")).getUnlocalizedName() + ".name"));
			
			if (stack.stackTagCompound.getTag("fluidStack") != null)
				list.add(Utils.formatString("", stack.stackTagCompound.getLong("fluidStored"), true, true) + " " + StatCollector.translateToLocal(FluidStack.loadFluidStackFromNBT(stack.stackTagCompound.getCompoundTag("fluidStack")).getFluid().getLocalizedName()));
		}
	}
}
