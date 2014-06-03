package tterrag.supermassivetech.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tterrag.supermassivetech.client.gui.GuiStorageBlock;
import tterrag.supermassivetech.container.ContainerStorageBlock;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerStorageBlock(player.inventory, (TileBlackHoleStorage) world.getTileEntity(x, y, z));
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiStorageBlock(player.inventory, (TileBlackHoleStorage) world.getTileEntity(x, y, z));
    }
}
