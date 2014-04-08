package tterrag.supermassivetech.registry;

import tterrag.supermassivetech.enchant.EnchantGravity;
import net.minecraft.enchantment.Enchantment;

public class ModEnchants
{
	public static ModEnchants instance = new ModEnchants();
	
	public Enchantment gravity;
	
	public void init()
	{
		gravity = new EnchantGravity();
	}
}
