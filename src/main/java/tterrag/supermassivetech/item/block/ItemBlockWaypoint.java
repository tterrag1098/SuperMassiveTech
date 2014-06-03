package tterrag.supermassivetech.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tterrag.supermassivetech.item.IAdvancedTooltip;

public class ItemBlockWaypoint extends ItemBlockSMT implements IAdvancedTooltip
{
    public ItemBlockWaypoint(Block block)
    {
        super(block);
    }
    
    @Override
    public String getStaticLines(ItemStack stack)
    {
        NBTTagCompound tag = stack.getTagCompound() == null ? null : stack.getTagCompound().getCompoundTag("waypoint");
        
        if (tag != null)
        {
            return tag.getString("waypointname");
        }
        else return null;
    }

    @Override
    public String getHiddenLines(ItemStack stack)
    {
        return null;
    }
    
    @Override
    public boolean isGravityWell(ItemStack stack)
    {
        return false;
    }
}
