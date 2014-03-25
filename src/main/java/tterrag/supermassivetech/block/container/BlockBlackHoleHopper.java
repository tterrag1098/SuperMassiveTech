package tterrag.supermassivetech.block.container;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;

public class BlockBlackHoleHopper extends BlockContainerSMT
{
	public BlockBlackHoleHopper()
	{
		super("tterrag.blackHoleHopper", Material.iron, soundTypeMetal, 30.0f, TileBlackHoleHopper.class, SuperMassiveTech.renderIDHopper);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		TileBlackHoleHopper te = (TileBlackHoleHopper) world.getTileEntity(x, y, z);

		if (te == null || world.isRemote)
			return true;

		ItemStack current = player.getCurrentEquippedItem();

		if (current == null)
		{
			if (player.isSneaking())
				te.clearConfig(player);
			else
				player.addChatMessage(new ChatComponentText("Current Configuration: " + te.getConfig()));
		}
		else
		{
			te.setConfig(current, player);
		}

		return true;
	}
}
