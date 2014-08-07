package tterrag.supermassivetech.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.common.util.Utils;

public class ItemBlockSMT extends ItemBlock
{
    public ItemBlockSMT(Block block)
    {
        super(block);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
        if (isGravityWell(stack) && entity instanceof EntityPlayer && !world.isRemote && !((EntityPlayer) entity).capabilities.isCreativeMode)
        {
            Utils.applyGravPotionEffects((EntityPlayer) entity, getGravStrength(stack));
        }
        super.onUpdate(stack, world, entity, par4, par5);
    }

    public boolean isGravityWell(ItemStack stack)
    {
        return true;
    }

    public int getGravStrength(ItemStack stack)
    {
        return 3;
    }
}
