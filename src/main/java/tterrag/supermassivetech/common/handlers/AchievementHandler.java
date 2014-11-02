package tterrag.supermassivetech.common.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatisticsFile;
import net.minecraftforge.event.entity.player.AchievementEvent;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.util.BlockCoord;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.registry.Achievements;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Handler
public class AchievementHandler
{
    @SubscribeEvent
    public void onCrafted(ItemCraftedEvent event)
    {
        if (!event.player.worldObj.isRemote && event.player instanceof EntityPlayerMP)
        {
            Achievements.unlock(Achievements.getValidItemStack(event.crafting), (EntityPlayerMP) event.player);

            if (event.crafting.getItem() == SuperMassiveTech.itemRegistry.heartOfStar)
            {
                IInventory inv = event.craftMatrix;

                for (int i = 0; i < inv.getSizeInventory(); i++)
                {
                    ItemStack curStack = inv.getStackInSlot(i);
                    if (curStack != null && curStack.getItem() == Items.nether_star && curStack.hasTagCompound() && curStack.getTagCompound().getBoolean("wasRejuvenated"))
                    {
                        Achievements.unlock(Achievements.getValidItemStack(new ItemStack(SuperMassiveTech.itemRegistry.heartOfStar, 1, 1)), (EntityPlayerMP) event.player);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onAchievement(AchievementEvent event)
    {
        StatisticsFile file = ((EntityPlayerMP)event.entityPlayer).func_147099_x();
        if (!event.entity.worldObj.isRemote && file.canUnlockAchievement(event.achievement) && !file.hasAchievementUnlocked(event.achievement) && ConfigHandler.betterAchievements)
        {
            event.entityPlayer.getEntityData().setInteger("fireworksLeft", 5);
            event.entityPlayer.getEntityData().setBoolean("fireworkDelay", false);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        int fireworksLeft = player.getEntityData().getInteger("fireworksLeft");
        if (!event.player.worldObj.isRemote && event.phase == Phase.END && fireworksLeft > 0 && (!player.getEntityData().getBoolean("fireworkDelay") || player.worldObj.getTotalWorldTime() % 20 == 0))
        {
            Utils.spawnFireworkAround(getBlockCoord(player), player.worldObj.provider.dimensionId);
            player.getEntityData().setInteger("fireworksLeft", fireworksLeft - 1);
            player.getEntityData().setBoolean("fireworkDelay", true);

            if (fireworksLeft == 1)
            {
                for (int i = 0; i < 5; i++)
                {
                    Utils.spawnFireworkAround(getBlockCoord(player), player.worldObj.provider.dimensionId);
                }
            }
        }
    }

    private BlockCoord getBlockCoord(EntityPlayer player)
    {
        return new BlockCoord((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
    }
}
