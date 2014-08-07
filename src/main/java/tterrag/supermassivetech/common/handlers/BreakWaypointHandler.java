package tterrag.supermassivetech.common.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import tterrag.supermassivetech.common.tile.TileWaypoint;
import tterrag.supermassivetech.common.util.Handler;
import tterrag.supermassivetech.common.util.Handler.HandlerType;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(types = HandlerType.FORGE)
public class BreakWaypointHandler
{
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event)
    {
        TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
        if (te != null && te instanceof TileWaypoint)
        {
            if (((TileWaypoint) te).waypoint.players.contains(event.getPlayer().getCommandSenderName())
                    || (event.getPlayer().capabilities.isCreativeMode && MinecraftServer.getServer().getConfigurationManager()
                            .func_152596_g(event.getPlayer().getGameProfile())))
                return;
        }
        else
        {
            return;
        }

        event.setCanceled(true);
    }
}
