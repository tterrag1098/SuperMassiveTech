package tterrag.supermassivetech.item;

import java.util.List;

import tterrag.supermassivetech.entity.item.EntityItemSpecialStar;
import tterrag.supermassivetech.registry.IStar;
import tterrag.supermassivetech.registry.Stars.StarTier;
import tterrag.supermassivetech.util.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemStarSpecial extends ItemStar implements IAdvancedTooltip
{
    public ItemStarSpecial(String unlocName)
    {
        super(unlocName, "star");
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean par5)
    {
        super.onUpdate(stack, world, entity, slot, par5);

        if (!world.isRemote && stack.stackSize > 1 && entity instanceof EntityPlayer)
        {
            ((EntityPlayer)entity).inventory.setInventorySlotContents(slot, null);
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
    public String getItemStackDisplayName(ItemStack par1ItemStack)
    {
        return EnumChatFormatting.AQUA + super.getItemStackDisplayName(par1ItemStack) + EnumChatFormatting.RESET;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return new EntityItemSpecialStar(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
                ((EntityItem) location).delayBeforeCanPickup);
    }
    
    @Override
    public String getStaticLines(ItemStack stack)
    {
        return EnumChatFormatting.RED + Utils.localize("tooltip.warning", true) + ": " + EnumChatFormatting.WHITE + Utils.localize("tooltip.warningText", true);
    }
}
