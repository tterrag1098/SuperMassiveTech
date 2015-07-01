package tterrag.supermassivetech.common.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.registry.Achievements;

import com.enderio.core.common.Handlers.Handler;
import com.enderio.core.common.Handlers.Handler.HandlerType;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

@Handler(HandlerType.FML)
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
}
