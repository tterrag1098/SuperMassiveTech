package tterrag.supermassivetech.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStar extends ItemSMT implements IAdvancedTooltip
{
	private Stars stars = Stars.instance;

	public ItemStar(String unlocName)
	{
		super(unlocName, unlocName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		IStar type = Utils.getType(par1ItemStack);

		if (type == null)
			return 0;
		else
			return type.getColor();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for (IStar t : stars.types.values())
		{
			list.add(Utils.setType(new ItemStack(this), t));
		}
	}
	
	private EnumChatFormatting getColorForPowerLeft(double power, double powerMax)
	{
		if (power / powerMax <= .25)
			return EnumChatFormatting.GOLD;
		else if (power / powerMax <= .1)
			return EnumChatFormatting.RED;
		
		return EnumChatFormatting.GREEN;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack itemstack)
	{
		return new EntityItemIndestructible(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
				((EntityItem) location).delayBeforeCanPickup);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		if (entity instanceof EntityLivingBase)
		{
			((EntityLivingBase) entity).setFire(10);
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player)
	{
		if (block.isFlammable(world, x, y - 1, z, ForgeDirection.UP))
			world.setBlock(x, y, z, Blocks.fire);

		return world.isRemote;
	}

	@Override
	public String[] getHiddenLines(ItemStack stack) 
	{
		IStar type = Utils.getType(stack);
		double powerLeft = stack.getTagCompound().getInteger("energy"), maxPower = type.getPowerStoredMax();

		return new String[]{
				type.getTextColor() + type.toString(),
				Stars.getEnumColor(type.getTier()) + type.getTier().toString(),
				Utils.formatString(EnumChatFormatting.YELLOW + "Outputs ", " RF", type.getPowerStoredMax(), false) + " at " + type.getPowerPerTick() + " RF/t",
				Utils.formatString(getColorForPowerLeft(powerLeft, maxPower) + "Power Remaining: ", " RF", (long) powerLeft, true)
		};
	}
	
	@Override
	public String[] getStaticLines(ItemStack stack) 
	{
		return null;
	}
}
