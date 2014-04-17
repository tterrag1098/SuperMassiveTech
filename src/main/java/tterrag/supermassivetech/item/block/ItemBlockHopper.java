package tterrag.supermassivetech.item.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import tterrag.supermassivetech.item.IAdvancedTooltip;

public class ItemBlockHopper extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockHopper(Block block)
    {
        super(block);
    }

    @Override
    public String[] getHiddenLines(ItemStack stack)
    {
        return new String[] { "- Right click with an item to configure", "- Right click with empty hand\n   to check configuration", "- Shift right click with an empty hand to clear" };
    }

    @Override
    public String[] getStaticLines(ItemStack stack)
    {
        if (stack.stackTagCompound == null)
            return null;

        ItemStack cfg = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory1"));
        ItemStack stored = ItemStack.loadItemStackFromNBT(stack.stackTagCompound.getCompoundTag("inventory0"));
        List<String> strs = new ArrayList<String>();

        if (stored != null)
            strs.add(String.format("Stored: %d %s", stored.stackSize, stored.getDisplayName())); 
        if (cfg != null)
            strs.add(String.format("Configuration: %s", cfg.getDisplayName()));
        
        return strs.size() == 0 ? null : strs.toArray(new String[] {});
    }
}
