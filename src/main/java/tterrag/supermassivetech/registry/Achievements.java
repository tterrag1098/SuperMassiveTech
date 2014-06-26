package tterrag.supermassivetech.registry;

import static tterrag.supermassivetech.SuperMassiveTech.*;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.lib.Reference;

public class Achievements
{
    public static Achievement craftHeart;
    public static Achievement craftStar;

    public static AchievementPage pageSMT;

    public static Map<ItemStack, Achievement> eventRequired = new HashMap<ItemStack, Achievement>();

    public static void initAchievements()
    {
        Achievement craftHeart = makeItemBasedAchievement("craftHeart", 0, -5, itemRegistry.heartOfStar, itemRegistry.heartOfStar, null);
        Achievement craftStar = makeItemBasedAchievement("craftStar", 0, -3, new ItemStack(itemRegistry.star), new ItemStack(itemRegistry.star), craftHeart, true);
        Achievement getDepNetherStar = makeItemBasedAchievement("getDepletedNetherStar", 4, -3, itemRegistry.depletedNetherStar, itemRegistry.depletedNetherStar, craftStar);
 
        makeItemBasedAchievement("craftBHS", -2, -1, blockRegistry.blackHoleStorage, blockRegistry.blackHoleStorage, craftStar);
        makeItemBasedAchievement("craftBHH", 2, -1, blockRegistry.blackHoleHopper, blockRegistry.blackHoleHopper, craftStar);
        
        Achievement craftSH = makeItemBasedAchievement("craftStarHarvester", 0, 1, blockRegistry.starHarvester, blockRegistry.starHarvester, craftStar);
        Achievement craftContainer = makeItemBasedAchievement("craftContainer", 0, 3, itemRegistry.starContainer, itemRegistry.starContainer, craftSH);
        makeItemBasedAchievement("starPower", 0, 5, new ItemStack(itemRegistry.starSpecial), new ItemStack(itemRegistry.star), craftContainer, true);
        
        makeItemBasedAchievement("craftWaypoint", -4, -2, blockRegistry.waypoint, blockRegistry.waypoint, craftStar);
        
        makeItemBasedAchievement("craftGravHelm", 4, 0, itemRegistry.gravityHelm, itemRegistry.gravityHelm, getDepNetherStar);
        makeItemBasedAchievement("craftGravChest", 5, 0, itemRegistry.gravityChest, itemRegistry.gravityChest, getDepNetherStar);
        makeItemBasedAchievement("craftGravLegs", 6, 0, itemRegistry.gravityLegs, itemRegistry.gravityLegs, getDepNetherStar);
        makeItemBasedAchievement("craftGravBoots", 7, 0, itemRegistry.gravityBoots, itemRegistry.gravityBoots, getDepNetherStar);

        pageSMT = new AchievementPage(Reference.MOD_NAME, eventRequired.values().toArray(new Achievement[] {}));
        AchievementPage.registerAchievementPage(pageSMT);
    }

    private static Achievement makeItemBasedAchievement(String basicName, int x, int y, ItemStack display, ItemStack toCraft, Achievement req, boolean special)
    {
        Achievement ret = new Achievement("achievement." + basicName, basicName, x, y, display, req);
        if (special)
            ret.setSpecial();
        eventRequired.put(toCraft, ret);
        return ret;
    }
    
    private static Achievement makeItemBasedAchievement(String basicName, int x, int y, ItemStack display, ItemStack toCraft, Achievement req)
    {
        return makeItemBasedAchievement(basicName, x, y, display, toCraft, req, false);
    }

    private static Achievement makeItemBasedAchievement(String basicName, int x, int y, Item display, Item toCraft, Achievement req)
    {
        return makeItemBasedAchievement(basicName, x, y, new ItemStack(display), new ItemStack(toCraft), req);
    }

    private static Achievement makeItemBasedAchievement(String basicName, int x, int y, Block display, Block toCraft, Achievement req)
    {
        return makeItemBasedAchievement(basicName, x, y, new ItemStack(display), new ItemStack(toCraft), req);
    }

    public static void fireCraftingRecipeFor(ItemStack stack, EntityPlayerMP player)
    {
        ItemStack i = getValidItemStack(stack);
        unlock(i, player);
    }

    public static ItemStack getValidItemStack(ItemStack stack)
    {
        for (ItemStack i : eventRequired.keySet())
        {
            if (i.getItem() == stack.getItem() && i.getItemDamage() == i.getItemDamage()) { return i; }
        }
        return null;
    }

    /**
     * ItemStack must have been checked with getValidItemStack prior
     */
    public static void unlock(ItemStack i, EntityPlayerMP player)
    {
        if (i == null)
        {
            return;
        }
        else
        {
            Achievement ach = eventRequired.get(i);

            boolean had = player.func_147099_x().hasAchievementUnlocked(ach);
            player.addStat(ach, 1);
            boolean has = player.func_147099_x().hasAchievementUnlocked(ach);

            if (!had && has)
            {
                doFireworkDisplay(player, true);
            }
        }
    }

    public static void doFireworkDisplay(EntityPlayerMP player, boolean override)
    {
        if (ConfigHandler.betterAchievements || override)
        {
            player.getEntityData().setInteger("fireworksLeft", 5);
            player.getEntityData().setBoolean("fireworkDelay", false);
        }
    }
    
    public static void doFireworkDisplay(EntityPlayerMP player)
    {
        doFireworkDisplay(player, false);
    }
}
