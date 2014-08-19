package tterrag.supermassivetech.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlackHoleEnergyRegistry
{
    private static class BlackHoleEnergyEntry
    {
        private ItemStack entryKey;
        private int energyOut;
        
        private boolean checkNBT = false;
        private boolean checkDamage = true;
        
        private BlackHoleEnergyEntry(ItemStack key, int energy)
        {
            entryKey = key;
            energyOut = energy;
        }
        
        private BlackHoleEnergyEntry setCheckNBT(boolean set)
        {
            checkNBT = set;
            return this;
        }
        
        private BlackHoleEnergyEntry setCheckDamage(boolean set)
        {
            checkDamage = set;
            return this;
        }
        
        private boolean matches(ItemStack stack)
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
        
        private int getEnergyFor(ItemStack stack)
        {
            return energyOut * stack.stackSize;
        }
    }
    
    public static final BlackHoleEnergyRegistry INSTANCE = new BlackHoleEnergyRegistry();
    
    private List<BlackHoleEnergyEntry> entries = new ArrayList<BlackHoleEnergyEntry>();
    
    public int getEnergyFor(ItemStack stack)
    {
        for (BlackHoleEnergyEntry entry : entries)
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
    public void registerBlackHoleEnergy(ItemStack input, int energy)
    {
        registerBlackHoleEnergy(input, energy, false, false);
    }
    
    /**
     * Defaults checKNBT to false
     */
    public void registerBlackHoleEnergy(ItemStack input, int energy, boolean checkNBT)
    {
        registerBlackHoleEnergy(input, energy, checkNBT, false);
    }
    
    public void registerBlackHoleEnergy(ItemStack input, int energy, boolean checkNBT, boolean checkDamage)
    {
        entries.add(new BlackHoleEnergyEntry(input, energy).setCheckNBT(checkNBT).setCheckDamage(checkDamage));
    }
    
    public void registerDefaults()
    {
        registerBlackHoleEnergy(new ItemStack(Items.diamond), 10);
        registerBlackHoleEnergy(new ItemStack(Item.getItemFromBlock(Blocks.diamond_block)), 90);
    }
}
