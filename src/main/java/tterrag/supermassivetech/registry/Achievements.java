package tterrag.supermassivetech.registry;

import static tterrag.supermassivetech.SuperMassiveTech.*;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.BlockCoord;
import tterrag.supermassivetech.util.Utils;

public class Achievements
{
    public static Achievement craftHeart;
    public static Achievement craftStar;

    public static AchievementPage pageSMT;

    public static Map<ItemStack, Achievement> eventRequired = new HashMap<ItemStack, Achievement>();

    public static void initAchievements()
    {
        Achievement craftHeart = makeCraftingAchievement("craftHeart", 0, -2, itemRegistry.heartOfStar, itemRegistry.heartOfStar, null);
        makeCraftingAchievement("craftStar", 0, 0, itemRegistry.star, itemRegistry.star, craftHeart);

        pageSMT = new AchievementPage(Reference.MOD_NAME, eventRequired.values().toArray(new Achievement[] {}));
        AchievementPage.registerAchievementPage(pageSMT);
    }

    private static Achievement makeCraftingAchievement(String basicName, int x, int y, ItemStack display, ItemStack toCraft, Achievement req)
    {
        Achievement ret = new Achievement("achievement." + basicName, basicName + "Achievement", x, y, display, req);
        eventRequired.put(toCraft, ret);
        return ret;
    }

    private static Achievement makeCraftingAchievement(String basicName, int x, int y, Item display, Item toCraft, Achievement req)
    {
        return makeCraftingAchievement(basicName, x, y, new ItemStack(display), new ItemStack(toCraft), req);
    }

    @SuppressWarnings("unused")
    private static Achievement makeCraftingAchievement(String basicName, int x, int y, ItemStack display, Item toCraft, Achievement req)
    {
        return makeCraftingAchievement(basicName, x, y, display, new ItemStack(toCraft), req);
    }

    @SuppressWarnings("unused")
    private static Achievement makeCraftingAchievement(String basicName, int x, int y, Item display, ItemStack toCraft, Achievement req)
    {
        return makeCraftingAchievement(basicName, x, y, new ItemStack(display), toCraft, req);
    }

    private static Achievement makeCraftingAchievement(String basicName, int x, int y, Block display, ItemStack toCraft, Achievement req)
    {
        return makeCraftingAchievement(basicName, x, y, new ItemStack(display), toCraft, req);
    }

    @SuppressWarnings("unused")
    private static Achievement makeCraftingAchievement(String basicName, int x, int y, Block display, Block toCraft, Achievement req)
    {
        return makeCraftingAchievement(basicName, x, y, display, new ItemStack(toCraft), req);
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
            player.addStat(ach, 1);
            System.out.println(player + " " + ach);

            if (player.func_147099_x().hasAchievementUnlocked(ach))
            {
                Utils.spawnRandomFirework(new BlockCoord((int) player.posX, (int) player.posY, (int) player.posZ),
                        player.worldObj.provider.dimensionId);
            }
        }
    }
}
