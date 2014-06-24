package tterrag.supermassivetech.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.item.EntityItemStarHeart;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStarHeart extends ItemSMT implements IAdvancedTooltip
{
    public ItemStarHeart(String unlocName)
    {
        super(unlocName, "starHeart");
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return new EntityItemStarHeart(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY,
                location.motionZ, ((EntityItem) location).delayBeforeCanPickup);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.starHeart", true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        return null;
    }
}
