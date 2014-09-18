package tterrag.supermassivetech.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.api.common.item.IStarItem;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.common.registry.Stars.StarTier;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStarSpecial extends ItemStar implements IAdvancedTooltip, IStarItem
{
    public ItemStarSpecial(String unlocName)
    {
        super(unlocName, "star");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5)
    {
        super.onUpdate(stack, world, entity, slot, par5);

        if (stack.stackSize > 1 && entity instanceof EntityPlayer)
        {
            ((EntityPlayer) entity).inventory.setInventorySlotContents(slot, null);
            if (!world.isRemote)
                world.newExplosion(entity, entity.posX, entity.posY, entity.posZ, 2.0f + (stack.stackSize - 1), true, true);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (IStar t : stars.types.values())
        {
            if (t.getTier() == StarTier.SPECIAL && (t.toString().equals("Pulsar") || t.toString().equals("Neutron")))
                list.add(Utils.setType(new ItemStack(this), t));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        return EnumChatFormatting.RED + Utils.lang.localize("tooltip.warning") + ": " + EnumChatFormatting.WHITE + Utils.lang.localize("tooltip.warningText");
    }

    @Override
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return true;
    }
}
