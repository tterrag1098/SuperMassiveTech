package tterrag.supermassivetech.common.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlackHoleEnergyRegistry
{
    public interface IEnergyEntry<T>
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
            if (matches(stack))
            {
                return energyOut * stack.stackSize;
            }
            return DEFAULT_ENERGY;
        }
    }

    private static class EnergyEntryEntity implements IEnergyEntry<Entity>
    {
        private Class<? extends Entity> entityClass;
        private int energyOut;
        private boolean strictCompare;

        /**
         * Defaults strictCompare to false
         */
        private EnergyEntryEntity(Class<? extends Entity> entityClass, int energy)
        {
            this(entityClass, energy, false);
        }

        /**
         * @param entityClass
         * @param energy
         * @param strictCompare - if false will use .isAssignableFrom() comparison, if true will use .getClass() == comparison.
         */
        private EnergyEntryEntity(Class<? extends Entity> entityClass, int energy, boolean strictCompare)
        {
            this.entityClass = entityClass;
            this.energyOut = energy;
            this.strictCompare = strictCompare;
        }

        @Override
        public boolean matches(Entity entry)
        {
            if (strictCompare)
            {
                return entry.getClass() == entityClass;
            }
            else
            {
                return entityClass.isAssignableFrom(entry.getClass());
            }
        }

        @Override
        public int getEnergyFor(Entity entry)
        {
            if (matches(entry))
            {
                return energyOut;
            }
            return DEFAULT_ENERGY;
        }
    }

    public static final BlackHoleEnergyRegistry INSTANCE = new BlackHoleEnergyRegistry();
    private BlackHoleEnergyRegistry() {}

    private List<IEnergyEntry<ItemStack>> items = new ArrayList<IEnergyEntry<ItemStack>>();
    private List<IEnergyEntry<Entity>> entities = new ArrayList<IEnergyEntry<Entity>>();
    
    private static final int DEFAULT_ENERGY = 1;
    private static final int BLOCK_MULT = 4;
    private static final int ENTITY_MULT = 5;

    public int getEnergyFor(ItemStack stack)
    {
        for (IEnergyEntry<ItemStack> entry : items)
        {
            if (entry.matches(stack))
            {
                return entry.getEnergyFor(stack);
            }
        }
        return (stack.getItem() instanceof ItemBlock ? DEFAULT_ENERGY * BLOCK_MULT : DEFAULT_ENERGY) * stack.stackSize;
    }
    
    public int getEnergyFor(Entity entity)
    {
        for (IEnergyEntry<Entity> entry : entities)
        {
            if (entry.matches(entity))
            {
                return entry.getEnergyFor(entity);
            }
        }
        return DEFAULT_ENERGY * ENTITY_MULT;
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
        registerItem(new EnergyEntryItem(input, energy).setCheckNBT(checkNBT).setCheckDamage(checkDamage));
    }
    
    /**
     * Defaults check to false
     */
    public void registerEntityEnergy(Class<? extends Entity> input, int energy)
    {
        registerEntityEnergy(input, energy, false);
    }

    /**
     * This is PER POINT OF DAMAGE
     */
    public void registerEntityEnergy(Class<? extends Entity> input, int energy, boolean strictCompare)
    {
        registerEntity(new EnergyEntryEntity(input, energy, strictCompare));
    }
    
    public void registerEntity(IEnergyEntry<Entity> entry)
    {
        entities.add(entry);
    }
    
    public void registerItem(IEnergyEntry<ItemStack> entry)
    {
        items.add(entry);
    }
    
    public void registerDefaults()
    {
        registerItemEnergy(new ItemStack(Items.diamond), 8192);
        registerItemEnergy(new ItemStack(Item.getItemFromBlock(Blocks.diamond_block)), getEnergyFor(new ItemStack(Items.diamond)) * 9);
        registerItemEnergy(new ItemStack(Blocks.stone), 8000);
        registerItemEnergy(new ItemStack(Blocks.grass), 8000);
    }
}
