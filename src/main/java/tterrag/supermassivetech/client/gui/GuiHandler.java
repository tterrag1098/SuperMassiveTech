package tterrag.supermassivetech.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import tterrag.supermassivetech.common.container.ContainerBlackHoleStorage;
import tterrag.supermassivetech.common.tile.TileBlackHoleStorage;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new ContainerBlackHoleStorage(player.inventory, (TileBlackHoleStorage) world.getTileEntity(x, y, z));
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return new GuiStorageBlock(player.inventory, (TileBlackHoleStorage) world.getTileEntity(x, y, z));
    }
}
