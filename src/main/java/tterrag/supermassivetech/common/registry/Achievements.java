package tterrag.supermassivetech.common.registry;

import static tterrag.supermassivetech.SuperMassiveTech.*;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import tterrag.supermassivetech.ModProps;

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
        Achievement getDepNetherStar = makeItemBasedAchievement("getDepletedNetherStar", 4, -2, itemRegistry.depletedNetherStar, itemRegistry.depletedNetherStar, craftStar);

        makeItemBasedAchievement("craftBHS", -2, -1, blockRegistry.blackHoleStorage, blockRegistry.blackHoleStorage, craftStar);
        makeItemBasedAchievement("craftBHH", 2, -1, blockRegistry.blackHoleHopper, blockRegistry.blackHoleHopper, craftStar);

        Achievement craftSH = makeItemBasedAchievement("craftStarHarvester", 0, 1, blockRegistry.starHarvester, blockRegistry.starHarvester, craftStar);
        Achievement craftContainer = makeItemBasedAchievement("craftContainer", 0, 3, itemRegistry.starContainer, itemRegistry.starContainer, craftSH);
        makeItemBasedAchievement("starPower", 0, 5, new ItemStack(itemRegistry.starSpecial), new ItemStack(itemRegistry.star, 1, 1), craftContainer, true);

        makeItemBasedAchievement("craftWaypoint", -4, -2, blockRegistry.waypoint, blockRegistry.waypoint, craftStar);

        int max = itemRegistry.gravityHelm.getMaxDamage();
        makeItemBasedAchievement("craftGravHelm", 5, 0, new ItemStack(itemRegistry.gravityHelm), new ItemStack(itemRegistry.gravityHelm, 1, max), getDepNetherStar);
        makeItemBasedAchievement("craftGravChest", 5, 1, new ItemStack(itemRegistry.gravityChest), new ItemStack(itemRegistry.gravityChest, 1, max), getDepNetherStar);
        makeItemBasedAchievement("craftGravLegs", 5, 2, new ItemStack(itemRegistry.gravityLegs), new ItemStack(itemRegistry.gravityLegs, 1, max), getDepNetherStar);
        makeItemBasedAchievement("craftGravBoots", 5, 3, new ItemStack(itemRegistry.gravityBoots), new ItemStack(itemRegistry.gravityBoots, 1, max), getDepNetherStar);

        Achievement rejuvenate = makeItemBasedAchievement("rejuvenateNetherStar", 4, -5, Items.nether_star, Items.nether_star, getDepNetherStar);
        makeItemBasedAchievement("comeFullCircle", 2, -5, new ItemStack(itemRegistry.heartOfStar, 1, 1), new ItemStack(itemRegistry.heartOfStar, 1, 1), rejuvenate);

        pageSMT = new AchievementPage(ModProps.MOD_NAME, eventRequired.values().toArray(new Achievement[] {}));
        AchievementPage.registerAchievementPage(pageSMT);
    }

    private static Achievement makeItemBasedAchievement(String basicName, int x, int y, ItemStack display, ItemStack toCraft, Achievement req, boolean special)
    {
        basicName = "SMT." + basicName;
        Achievement ret = new Achievement("achievement." + basicName, basicName, x, y, display, req);
        if (special)
            ret.setSpecial();
        eventRequired.put(toCraft, ret);
        ret.registerStat();
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
            if (i.isItemEqual(stack))
            {
                return i;
            }
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
            player.addStat(ach, 1);
        }
    }

    public static void unlockForced(Achievement ach, EntityPlayerMP player)
    {
        player.addStat(ach, 1);
    }
}
