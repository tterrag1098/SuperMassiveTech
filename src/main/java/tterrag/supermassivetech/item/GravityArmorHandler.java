package tterrag.supermassivetech.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovementInput;
import tterrag.supermassivetech.SuperMassiveTech;
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
			if (input.jump && !hasJumped && effect != 0)
				player.motionY *= (1 + (3 * getArmorMult(player, Constants.instance().ENERGY_DRAIN)));
			hasJumped = !player.onGround;
		}
	}

	@SubscribeEvent
	public void slowFall(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		System.out.println(player.motionY);
		if (player.motionY < -0.08)
		{
			double effect = getArmorMult(player, Constants.instance().ENERGY_DRAIN / 50);
			player.motionY *= 1 - (0.1 * effect);
			player.fallDistance /= 1 + effect * 5;
			System.out.println("slowed   " + player.motionY);
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
		}
		return effect;
	}
	
	private double getArmorMult(EntityPlayer player)
	{
		return getArmorMult(player, 0);
	}
}