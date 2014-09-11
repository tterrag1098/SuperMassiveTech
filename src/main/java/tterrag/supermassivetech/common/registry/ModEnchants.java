package tterrag.supermassivetech.common.registry;

import java.util.HashMap;

import net.minecraft.enchantment.Enchantment;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.enchant.EnchantGravity;

public class ModEnchants
{
    public static final ModEnchants instance = new ModEnchants();
    private ModEnchants() {}
    
    public EnchantGravity gravity;

    private HashMap<Integer, Enchantment> enchants;

    public void init()
    {
        enchants = new HashMap<Integer, Enchantment>();

        gravity = new EnchantGravity();
        enchants.put(ConfigHandler.gravEnchantID, gravity);
    }

    public Enchantment getEnchantByID(int id)
    {
        return enchants.get(id);
    }
}
