package tterrag.supermassivetech.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import tterrag.supermassivetech.tile.TileWaypoint;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BreakWaypointHandler
{
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event)
    {
        TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
        if (te != null && te instanceof TileWaypoint)
        {
            if (((TileWaypoint) te).waypoint.players.contains(event.getPlayer().getCommandSenderName()) || (event.getPlayer().capabilities.isCreativeMode
                    && MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(event.getPlayer().getCommandSenderName())))
                return;
        }
        else
        {
            return;
        }
        
        event.setCanceled(true);
    }
}