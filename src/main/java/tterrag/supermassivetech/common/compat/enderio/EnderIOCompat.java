package tterrag.supermassivetech.common.compat.enderio;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import tterrag.core.common.Handlers.Handler;
import tterrag.core.common.Handlers.Handler.HandlerType;
import tterrag.core.common.compat.ICompatability;
import tterrag.supermassivetech.client.util.ClientUtils;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.handlers.GravityArmorHandler;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import crazypants.enderio.item.darksteel.DarkSteelRecipeManager;
import crazypants.enderio.item.darksteel.ItemDarkSteelArmor;

@Handler(HandlerType.FML)
public class EnderIOCompat implements ICompatability
{
    public static void load()
    {
        DarkSteelRecipeManager.instance.getUpgrades().add(new GravityResistUpgrade(10, 0));
        DarkSteelRecipeManager.instance.getUpgrades().add(new GravityResistUpgrade(20, 1));
        DarkSteelRecipeManager.instance.getUpgrades().add(new GravityResistUpgrade(30, 2));
    }

    @SubscribeEvent
    public void doGravResistEnderIO(PlayerTickEvent event)
    {
        boolean isJumpKeyDown = GravityArmorHandler.isJumpKeyDown;

        if (event.player.worldObj.isRemote)
        {
            isJumpKeyDown = ClientUtils.calculateClientJumpState();
        }

        if (event.phase == Phase.END && !event.player.onGround && !event.player.capabilities.isFlying
                && (isJumpKeyDown || (event.player.motionY < -0.2 && !event.player.isSneaking())))
        {
            double effect = getArmorMult(event.player, 0.072, ConfigHandler.gravArmorDrain / 50);
            if (event.player.ridingEntity != null && event.player.posY <= 256)
            {
                event.player.ridingEntity.motionY += effect;
                event.player.ridingEntity.fallDistance *= 1 - effect * 2;
            }
            else
            {
                event.player.motionY += effect;
                event.player.fallDistance *= 1 - effect * 2;
            }
        }
    }

    private double getArmorMult(EntityPlayer player, double seed, int drainAmount)
    {
        // no power loss in creative, still get effects
        if (player.capabilities.isCreativeMode && drainAmount != 0)
            return getArmorMult(player, seed, 0);

        double effect = 0;
        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = player.inventory.armorInventory[i];
            GravityResistUpgrade upgrade = GravityResistUpgrade.loadFromItem(stack);
            if (stack != null && stack.getItem() instanceof ItemDarkSteelArmor && upgrade != null)
            {
                int drained = ((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, drainAmount, false);
                effect += drained > 0 || drained == drainAmount ? getSeed(seed, upgrade) / 4d : 0;
            }
        }
        return effect;
    }

    private double getSeed(double seed, GravityResistUpgrade upgrade)
    {
        switch (upgrade.level)
        {
        case LOW:
            return seed / 3;
        case NORMAL:
            return seed / 2;
        case HIGH:
            return seed;
        default:
            return 0;
        }
    }
}
