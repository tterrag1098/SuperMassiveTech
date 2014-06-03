package tterrag.supermassivetech.item.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemBlockWaypoint extends ItemBlockSMT
{
    public ItemBlockWaypoint(Block block)
    {
        super(block);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
    {
        list.add(stack.getTagCompound().getCompoundTag("waypoint").getString("waypointname"));
    }
    
    @Override
    public boolean isGravityWell(ItemStack stack)
    {
        return false;
    }
}
