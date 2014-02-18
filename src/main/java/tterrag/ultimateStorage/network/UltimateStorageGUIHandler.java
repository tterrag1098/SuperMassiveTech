package tterrag.ultimateStorage.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tterrag.ultimateStorage.client.GuiStorageBlock;
import tterrag.ultimateStorage.container.ContainerStorageBlock;
import tterrag.ultimateStorage.tile.TileStorageBlock;
import cpw.mods.fml.common.network.IGuiHandler;

public class UltimateStorageGUIHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return new ContainerStorageBlock(player.inventory, (TileStorageBlock) world.getTileEntity(x, y, z));
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return new GuiStorageBlock(player.inventory, (TileStorageBlock) world.getTileEntity(x, y, z));
	}
}
