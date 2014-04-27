package tterrag.supermassivetech.item.armor;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.util.Constants;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GravityArmorHandler
{
    @SubscribeEvent
    public void doAntiGrav(PlayerTickEvent event)
    {
        if (!event.player.onGround && !event.player.capabilities.isFlying && (Keyboard.isKeyDown(Keyboard.KEY_SPACE) || (event.player.motionY < -0.2 && !event.player.isSneaking())))
        {
            double effect = getArmorMult(event.player, Constants.instance().ENERGY_DRAIN / 50);
            event.player.motionY += effect;
            event.player.fallDistance *= 1 - effect * 2;
        }
    }

    private double getArmorMult(EntityPlayer player, int drainAmount)
    {
        // no power loss in creative, still get effects
        if (player.capabilities.isCreativeMode && drainAmount != 0) return getArmorMult(player, 0);
        
        double effect = 0;
        for (ItemStack i : player.inventory.armorInventory)
        {
            if (i != null && SuperMassiveTech.itemRegistry.armors.contains(i.getItem()))
            {
                int drained = ((IEnergyContainerItem) i.getItem()).extractEnergy(i, drainAmount, false);
                effect += drained > 0  || drained == drainAmount ? .036d / 4d : 0;
            }
            else if (i != null && EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, i) != 0)
            {
                effect += .036d / 5d;
                i.damageItem(new Random().nextInt(100) < 2 && !player.worldObj.isRemote ? 1 : 0, player);
            }
        }
        return effect;
    }
}