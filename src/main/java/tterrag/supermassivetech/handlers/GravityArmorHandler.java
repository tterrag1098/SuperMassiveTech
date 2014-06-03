package tterrag.supermassivetech.handlers;

import java.lang.reflect.Field;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.util.ClientUtils;
import tterrag.supermassivetech.util.Constants;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GravityArmorHandler
{
    public static boolean isJumpKeyDown;
    private Field isHittingBlock;

    @SubscribeEvent
    public void doAntiGrav(PlayerTickEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            isJumpKeyDown = ClientUtils.calculateClientJumpState();
        }

        if (!event.player.onGround && !event.player.capabilities.isFlying && (isJumpKeyDown || (event.player.motionY < -0.2 && !event.player.isSneaking())))
        {
            double effect = getArmorMult(event.player, Constants.instance().ENERGY_DRAIN / 50);
            event.player.motionY += effect;
            event.player.fallDistance *= 1 - effect * 2;
        }
    }

    private double getArmorMult(EntityPlayer player, int drainAmount)
    {
        // no power loss in creative, still get effects
        if (player.capabilities.isCreativeMode && drainAmount != 0)
            return getArmorMult(player, 0);

        double effect = 0;
        for (ItemStack i : player.inventory.armorInventory)
        {
            if (i != null && SuperMassiveTech.itemRegistry.armors.contains(i.getItem()))
            {
                int drained = ((IEnergyContainerItem) i.getItem()).extractEnergy(i, drainAmount, false);
                effect += drained > 0 || drained == drainAmount ? .036d / 4d : 0;
            }
            else if (i != null && EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, i) != 0)
            {
                effect += .036d / 5d;
                i.damageItem(new Random().nextInt(1000) < 2 && !player.worldObj.isRemote ? 1 : 0, player);
            }
        }
        return effect;
    }

    @SubscribeEvent
    public void pickCorrectTool(PlayerTickEvent event)
    {
        if (isHittingBlock == null)
        {
            try
            {
                try
                {
                    isHittingBlock = PlayerControllerMP.class.getDeclaredField("isHittingBlock");
                }
                catch (Throwable t)
                {
                    isHittingBlock = PlayerControllerMP.class.getDeclaredField("field_78778_j");
                }
                isHittingBlock.setAccessible(true);
            }
            catch (Throwable t)
            {
                SuperMassiveTech.logger.error("Could not get player field, this could be laggy");
            }
        }

        EntityPlayer player = event.player;
        try
        {
            if (player != null && event.player.worldObj.isRemote && player.getCurrentEquippedItem() != null && player.inventory.armorInventory[2] != null
                    && player.inventory.armorInventory[2].getItem() instanceof ItemGravityArmor && player.inventory.armorInventory[2].stackTagCompound.getBoolean("toolpickeractive")
                    && isHittingBlock.getBoolean(Minecraft.getMinecraft().playerController))
            {
                MovingObjectPosition pos = ClientUtils.getMouseOver();
                Block block = player.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ);

                if (block.isAir(player.worldObj, pos.blockX, pos.blockY, pos.blockZ))
                    return;

                float speed = 0;
                int select = 0;
                for (int i = 0; i < 9; i++)
                {
                    ItemStack cur = player.inventory.mainInventory[i];
                    if (cur != null)
                    {
                        float newSpeed = cur.getItem().getDigSpeed(cur, block, player.worldObj.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ));
                        if (newSpeed > speed)
                        {
                            speed = newSpeed;
                            select = i;
                        }
                    }
                }
                player.inventory.currentItem = select;
            }
        }
        catch (Throwable t)
        {
            SuperMassiveTech.logger.error("Reflection failure " + t.getMessage());
        }
    }
}