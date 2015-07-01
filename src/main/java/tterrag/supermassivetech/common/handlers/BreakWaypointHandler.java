package tterrag.supermassivetech.common.handlers;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;

import org.apache.commons.lang3.ArrayUtils;

import tterrag.supermassivetech.common.tile.TileWaypoint;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Handler(HandlerType.FORGE)
public class BreakWaypointHandler
{
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event)
    {
        TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);
        if (te != null && te instanceof TileWaypoint)
        {
            if (((TileWaypoint) te).waypoint.players.contains(event.getPlayer().getCommandSenderName()) // belongs to player
                    || event.getPlayer().capabilities.isCreativeMode // is creative
                    || ArrayUtils.contains(MinecraftServer.getServer().getConfigurationManager().func_152606_n(), event.getPlayer().getCommandSenderName())) // is op
                return;
        }
        else
        {
            return;
        }

        event.setCanceled(true);
    }
}
