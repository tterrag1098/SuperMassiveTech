package tterrag.supermassivetech.common.registry;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlackHoleEnergyRegistry
{
    // there was some content here.
    // it's gone now.
    
    public static final BlackHoleEnergyRegistry INSTANCE = new BlackHoleEnergyRegistry();

    private BlackHoleEnergyRegistry()
    {}

    private static final int DEFAULT_ENERGY = 1;
    private static final int BLOCK_MULT = 4;
    private static final int ENTITY_MULT = 5;

    public int getEnergyFor(ItemStack stack)
    {
        return (stack.getItem() instanceof ItemBlock ? DEFAULT_ENERGY * BLOCK_MULT : DEFAULT_ENERGY) * stack.stackSize;
    }

    public int getEnergyFor(Entity entity)
    {
        return DEFAULT_ENERGY * ENTITY_MULT;
    }
}
