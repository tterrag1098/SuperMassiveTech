package tterrag.supermassivetech.item;

import java.text.NumberFormat;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.item.EntityItemDepletedNetherStar;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDepletedNetherStar extends ItemSMT implements IAdvancedTooltip
{
    public final int maxDamage = 100;

    public ItemDepletedNetherStar()
    {
        super("depletedNetherStar", "depletedNetherStar");
        hasSubtypes = true;
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = ObfuscationReflectionHelper.getPrivateValue(Item.class, Items.nether_star, "itemIcon", "field_77791_bV");
    }

    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        float percent = .20f + (((float) par1ItemStack.getItemDamage()) / ((float) maxDamage) * .80f);
        int color = (int) (0xFF * percent);

        return Utils.toHex(color, color, color);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        EntityItemDepletedNetherStar entity = new EntityItemDepletedNetherStar(world, location.posX, location.posY, location.posZ, itemstack, location.motionX,
                location.motionY, location.motionZ, ((EntityItem) location).delayBeforeCanPickup);
        entity.func_145799_b(world.getClosestPlayerToEntity(location, -1).getCommandSenderName());
        return entity;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.depletedNetherStar", true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        return "" + EnumChatFormatting.DARK_GRAY + EnumChatFormatting.ITALIC
                + NumberFormat.getPercentInstance().format(((float) stack.getItemDamage()) / ((float) maxDamage)) + " Recharged";
    }
}
