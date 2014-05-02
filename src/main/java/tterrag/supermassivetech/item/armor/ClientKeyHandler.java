package tterrag.supermassivetech.item.armor;

import net.minecraft.client.Minecraft;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.network.packet.PacketJumpUpdate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientKeyHandler
{
    private boolean lastJumpState;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        
        if (mc.thePlayer != null)
        {
            boolean jumpState = mc.gameSettings.keyBindJump.getIsKeyPressed();

            if (jumpState != lastJumpState)
            {
                lastJumpState = jumpState;
                SuperMassiveTech.channelHandler.sendToAll(new PacketJumpUpdate(jumpState));
            }
        }
    }
}
