package tterrag.supermassivetech.client.handlers;

import tterrag.supermassivetech.common.util.Handler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

@Handler
public class ClientRenderingHandler
{
    private static int ticksElapsed;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        if (event.phase == Phase.END)
        {
            ticksElapsed++;
        }
    }

    public static int getElapsed()
    {
        return ticksElapsed;
    }
}
