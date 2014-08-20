package tterrag.supermassivetech.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlackHoleEnergyRegistry
{
    private interface IEnergyEntry<T>
    {
        boolean matches(T entry);
        int getEnergyFor(T entry);
    }
    
    private static class EnergyEntryItem implements IEnergyEntry<ItemStack>
    {
        private ItemStack entryKey;
        private int energyOut;
        
        private boolean checkNBT = false;
        private boolean checkDamage = true;
        
        private EnergyEntryItem(ItemStack key, int energy)
        {
            entryKey = key;
            energyOut = energy;
        }
        
        private EnergyEntryItem setCheckNBT(boolean set)
        {
            checkNBT = set;
            return this;
        }
        
        private EnergyEntryItem setCheckDamage(boolean set)
        {
            checkDamage = set;
            return this;
        }
        
        public boolean matches(ItemStack stack)
        {
            boolean matches = true;
            
            if (checkNBT)
            {
                matches &= ItemStack.areItemStackTagsEqual(entryKey, stack);
            }
            
            if (checkDamage)
            {
                matches &= entryKey.getItemDamage() == stack.getItemDamage();
            }
            
            matches &= entryKey.getItem() == stack.getItem();
            
            return matches;
        }
        
        public int getEnergyFor(ItemStack stack)
        {
            return energyOut * stack.stackSize;
        }
    }
    
    public static final BlackHoleEnergyRegistry INSTANCE = new BlackHoleEnergyRegistry();
    
    private List<IEnergyEntry<ItemStack>> items = new ArrayList<IEnergyEntry<ItemStack>>();
    
    public int getEnergyFor(ItemStack stack)
    {
        for (IEnergyEntry<ItemStack> entry : items)
        {
            if (entry.matches(stack))
            {
                return entry.getEnergyFor(stack);
            }
        }
        return 1;
    }
    
    /**
     * Defaults both checks to false
     */
    public void registerItemEnergy(ItemStack input, int energy)
    {
        registerItemEnergy(input, energy, false, false);
    }
    
    /**
     * Defaults checKNBT to false
     */
    public void registerItemEnergy(ItemStack input, int energy, boolean checkNBT)
    {
        registerItemEnergy(input, energy, checkNBT, false);
    }
    
    public void registerItemEnergy(ItemStack input, int energy, boolean checkNBT, boolean checkDamage)
    {
        items.add(new EnergyEntryItem(input, energy).setCheckNBT(checkNBT).setCheckDamage(checkDamage));
    }
    
    public void registerDefaults()
    {
        registerItemEnergy(new ItemStack(Items.diamond), 10);
        registerItemEnergy(new ItemStack(Item.getItemFromBlock(Blocks.diamond_block)), 90);
    }
}
