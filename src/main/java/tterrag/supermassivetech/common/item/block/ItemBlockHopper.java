package tterrag.supermassivetech.common.item.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockHopper extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockHopper(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        return Utils.localize("tooltip.blackHoleHopper", true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound == null)
            return null;

        ItemStack cfg = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory1"));
        ItemStack stored = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory0"));
        List<String> strs = new ArrayList<String>();

        if (stored != null)
            strs.add(String.format("%s: %d %s", Utils.localize("tooltip.stored", true), stored.stackSize, stored.getDisplayName()));
        if (cfg != null)
            strs.add(String.format("%s: %s", Utils.localize("tooltip.configuration", true), cfg.getDisplayName()));

        if (strs.isEmpty())
        {
            return null;
        }
        else
        {
            return Utils.makeTooltipString(strs);
        }
    }

}
