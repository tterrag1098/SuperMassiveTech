package tterrag.supermassivetech.handlers;

import codechicken.lib.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import tterrag.supermassivetech.registry.Achievements;
import tterrag.supermassivetech.util.BlockCoord;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

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
    
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        int fireworksLeft = player.getEntityData().getInteger("fireworksLeft");
        if (event.phase == Phase.END && fireworksLeft > 0 && (!player.getEntityData().getBoolean("fireworkDelay") || player.worldObj.getTotalWorldTime() % 20 == 0))
        {
            Utils.spawnRandomFirework(getBlockCoord(player), player.worldObj.provider.dimensionId);
            player.getEntityData().setInteger("fireworksLeft", fireworksLeft - 1);
            player.getEntityData().setBoolean("fireworkDelay", true);
            
            if (fireworksLeft == 1)
            {
                for (int i = 0; i < 5; i++)
                {
                    Utils.spawnRandomFirework(getBlockCoord(player), player.worldObj.provider.dimensionId);
                }
            }
        }
    }
    
    private BlockCoord getBlockCoord(EntityPlayer player)
    {
        return new BlockCoord(MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ));
    }
}
