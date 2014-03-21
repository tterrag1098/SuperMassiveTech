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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStar extends ItemSMT
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
		NBTTagCompound tag = new NBTTagCompound();
		for (IStar t : stars.types.values())
		{
			tag = new NBTTagCompound();
			tag.setString("type", t.toString());
			ItemStack i = new ItemStack(this);
			i.setTagCompound(tag);
			list.add(i);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(Utils.getType(par1ItemStack).toString());
		par3List.add("Tier: " + Utils.getType(par1ItemStack).getTier().toString());
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
			((EntityLivingBase)entity).setFire(10);
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
}
