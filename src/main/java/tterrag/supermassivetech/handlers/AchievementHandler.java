package tterrag.supermassivetech.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import tterrag.supermassivetech.registry.Achievements;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class AchievementHandler
{
    @SubscribeEvent
    public void onCrafted(ItemCraftedEvent event)
    {
        if (!event.player.worldObj.isRemote)
        {
        Achievements.unlock(Achievements.getValidItemStack(event.crafting), (EntityPlayerMP) event.player);
        }
    }
}
