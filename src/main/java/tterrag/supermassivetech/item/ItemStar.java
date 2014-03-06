package tterrag.supermassivetech.item;

import java.util.List;

import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


public class ItemStar extends ItemSMT{

	public static enum StarType
	{
		/* normal stars */
		YELLOW_DWARF,
		RED_DWARF,
		
		/* giant stars */
		RED_GIANT,
		BLUE_GIANT,
		SUPERGIANT,
		
		/* weak, strange stars */
		BROWN_DWARF,
		WHITE_DWARF,
		NEUTRON,
		PULSAR
	}
	
	public ItemStar(String unlocName) {
		super(unlocName, "");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		StarType type = Utils.getType(par1ItemStack);
		if (type == null) return 0;
		
		switch(type.ordinal())
		{
		default:
			return 0;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		NBTTagCompound tag = new NBTTagCompound();
		for (StarType t : StarType.values())
		{
			tag = new NBTTagCompound();
			tag.setString("type", t.name());
			ItemStack i = new ItemStack(this);
			i.setTagCompound(tag);
			list.add(i);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(Utils.getType(par1ItemStack).name());
	}
}
