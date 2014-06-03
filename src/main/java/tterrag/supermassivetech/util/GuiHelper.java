package tterrag.supermassivetech.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import tterrag.supermassivetech.client.gui.GuiWaypoint;
import tterrag.supermassivetech.tile.TileWaypoint;

public class GuiHelper
{
    public static void openWaypointGui(World world, int x, int y, int z)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiWaypoint((TileWaypoint) world.getTileEntity(x, y, z)));
    }
}
