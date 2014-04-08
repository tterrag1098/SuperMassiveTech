package tterrag.supermassivetech.item.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.util.Constants;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GravityArmorHandler
{
	boolean hasJumped;

	@SubscribeEvent
	public void boostJump(ClientTickEvent event)
	{
		if (event.phase == Phase.END)
		{
			EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
			if (player == null)
				return;
			MovementInput input = player.movementInput;
			double effect = getArmorMult(player);
			if (effect == 0) return;
			if (input.jump && !hasJumped && effect != 0)
				player.motionY *= (1 + (3 * getArmorMult(player, Constants.instance().ENERGY_DRAIN)));
			hasJumped = !player.onGround;
		}
	}

	@SubscribeEvent
	public void slowFall(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		if (player.motionY < -0.08)
		{
			double effect = getArmorMult(player, Constants.instance().ENERGY_DRAIN / 50);
			if (effect == 0) return;
			player.motionY *= 1 - (0.08 * effect);
			player.fallDistance /= 1 + effect / 3;
		}
	}
	
	private double getArmorMult(EntityPlayer player, int drainAmount)
	{
		double effect = 0;
		for (ItemStack i : player.inventory.armorInventory)
		{
			if (i != null && SuperMassiveTech.itemRegistry.armors.contains(i.getItem()))
			{
				effect += 1d/4d;
				((IEnergyContainerItem) i.getItem()).extractEnergy(i, drainAmount, false);
			}
			else if (i != null && EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, i) != 0)
			{
				effect += 1d/8d;
			}
		}
		return effect;
	}
	
	private double getArmorMult(EntityPlayer player)
	{
		return getArmorMult(player, 0);
	}
}