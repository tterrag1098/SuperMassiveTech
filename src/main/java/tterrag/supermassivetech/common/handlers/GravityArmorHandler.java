package tterrag.supermassivetech.common.handlers;

import static tterrag.supermassivetech.common.util.Utils.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.command.IEntitySelector;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.util.ClientUtils;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.network.message.MessageUpdateGravityArmor.PowerUps;
import tterrag.supermassivetech.common.util.BlockCoord;
import tterrag.supermassivetech.common.util.Constants;
import tterrag.supermassivetech.common.util.Handler;
import tterrag.supermassivetech.common.util.Handler.HandlerType;
import tterrag.supermassivetech.common.util.Utils;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

@Handler(types = HandlerType.FML)
public class GravityArmorHandler
{
    public static boolean isJumpKeyDown;
    private Field isHittingBlock;

    private final int fieldRange = ConfigHandler.fieldRange;
    private final double fieldScalingPower = ConfigHandler.fieldUsageBase;

    // String constants for armor power states
    public static final String ON = Utils.localize("tooltip.on", true);
    public static final String OFF = Utils.localize("tooltip.off", true);

    public static final String ANTI_GRAV = Utils.localize("armorPower.antiGrav", true);
    public static final String REPULSOR = Utils.localize("armorPower.repulsor", true);
    public static final String ATTRACTOR = Utils.localize("armorPower.attractor", true);

    public static final String COMPASS_ONLY = Utils.localize("armorPower.compassOnly", true);
    public static final String TEXT_ONLY = Utils.localize("armorPower.textOnly", true);

    private static class NoPlayersSelector implements IEntitySelector
    {
        private static final NoPlayersSelector instance = new NoPlayersSelector();

        @Override
        public boolean isEntityApplicable(Entity entity)
        {
            return !(entity instanceof EntityPlayer);
        }
    }

    @SubscribeEvent
    public void doGravResist(PlayerTickEvent event)
    {
        if (event.player.worldObj.isRemote)
        {
            isJumpKeyDown = ClientUtils.calculateClientJumpState();
        }

        if (event.phase == Phase.END && !event.player.onGround && !event.player.capabilities.isFlying
                && (isJumpKeyDown || (event.player.motionY < -0.2 && !event.player.isSneaking())))
        {
            double effect = getArmorMult(event.player, 0.072, Constants.instance().getEnergyDrain() / 50);
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
            if (stack != null && SuperMassiveTech.itemRegistry.armors.contains(stack.getItem()) && doStatesMatch(player, PowerUps.GRAV_RESIST, i, ON))
            {
                int drained = ((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, drainAmount, false);
                effect += drained > 0 || drained == drainAmount ? seed / 4d : 0;
            }
            else if (stack != null)
            {
                int level = EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, stack);
                if (level > 0)
                {
                    effect += SuperMassiveTech.enchantRegistry.gravity.getReduction(seed / 5, level);
                    stack.damageItem(new Random().nextInt(1000) < 2 && !player.worldObj.isRemote ? 1 : 0, player);
                }
            }
        }
        return effect;
    }

    /**
     * Handles the Tool-Picker powerup
     */
    @SubscribeEvent
    public void pickCorrectTool(PlayerTickEvent event) throws Exception
    {
        if (event.player.worldObj.isRemote && event.phase == Phase.END)
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
            World world = player.worldObj;

            if (player == null || world == null)
                return;

            if (player.getCurrentEquippedItem() != null && doStatesMatch(player, PowerUps.TOOLPICKER, 2, ON)
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
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void doField(PlayerTickEvent event)
    {
        if (event.phase != Phase.END)
            return;

        EntityPlayer player = event.player;
        ItemStack chest = player.inventory.armorInventory[2];
        if (!doStatesMatch(player, PowerUps.FIELD, 2, OFF) && Utils.armorIsGravityArmor(chest))
        {
            IEnergyContainerItem chestEnergy = (IEnergyContainerItem) chest.getItem();
            World world = player.worldObj;

            double pX = player.posX, pY = player.posY, pZ = player.posZ;
            double powerScale = fieldScalingPower;

            if (chestEnergy.extractEnergy(chest, (int) powerScale + 1, true) < powerScale)
                return;

            int r = fieldRange;

            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(pX - r, pY - r, pZ - r, pX + r, pY + r, pZ + r);

            BlockCoord baseBlock = new BlockCoord((int) player.posX, (int) player.posY, (int) player.posZ);

            List<Entity> entities = null;

            final double defaultEffect = 0.09;
            double effect = getArmorMult(player, defaultEffect, 0);

            if (ConfigHandler.fieldIgnorePlayers)
            {
                entities = world.getEntitiesWithinAABBExcludingEntity(player, bb, NoPlayersSelector.instance);
            }
            else
            {
                entities = world.getEntitiesWithinAABBExcludingEntity(player, bb);
            }

            if (doStatesMatch(player, PowerUps.FIELD, 2, ANTI_GRAV))
            {
                processBlocks(baseBlock, world);

                for (Entity e : entities)
                {
                    if (e.posY > 256)
                        continue;

                    if (!ConfigHandler.fieldIgnorePlayers && e instanceof EntityPlayer)
                    {
                        if (doStatesMatch((EntityPlayer) e, PowerUps.FIELD, 2, ANTI_GRAV))
                        {
                            e.motionY = 0.5;

                            double distX = e.posX - player.posX;
                            double distZ = e.posZ - player.posZ;

                            e.motionX += MathHelper.clamp_double(1 / distX, -2, 2);
                            e.motionZ += MathHelper.clamp_double(1 / distZ, -2, 2);

                            if (world.isRemote)
                                ClientUtils.spawnConflictParticles(e, player);
                        }
                        else
                        {
                            effect -= getArmorMult(player, effect, 0);
                        }
                    }

                    e.motionY += effect;
                    e.motionY = Math.min(0.75, e.motionY);
                    e.fallDistance = 0;

                    if (!world.isRemote)
                    {
                        int powerUse = (int) Math.max(1, Math.pow(e.width + e.height, powerScale));
                        chestEnergy.extractEnergy(chest, powerUse, false);
                    }
                }
            }
            else
            {
                for (Entity e : entities)
                {
                    double x = pX - e.posX;
                    double y = pY - e.posY;
                    double z = pZ - e.posZ;
                    double dist = Math.sqrt(x * x + y * y + z * z);
                    double speed = 0.1;

                    if (e instanceof EntityPlayer)
                    {
                        speed *= 1 - Utils.getGravResist((EntityPlayer) e);
                    }

                    if (doStatesMatch(player, PowerUps.FIELD, 2, ATTRACTOR))
                    {
                        if (dist < 1.25)
                        {
                            e.onCollideWithPlayer(player);
                        }

                        e.motionX += x / dist * speed;
                        e.motionY += y / dist * speed;
                        e.motionZ += z / dist * speed;

                        if (e instanceof EntityItem)
                        {
                            ((EntityItem) e).delayBeforeCanPickup = 0;
                            if (y > -0.5)
                            {
                                e.motionY = 0.15;
                            }
                        }

                        if (!world.isRemote)
                        {
                            int powerUse = (int) (Math.max(1, Math.pow(e.width + e.height, powerScale)) / ((fieldRange - dist) * 2));
                            chestEnergy.extractEnergy(chest, powerUse, false);
                        }
                    }
                    else
                    {
                        e.motionX -= x / dist * speed;
                        e.motionY -= y / dist * speed;
                        e.motionZ -= z / dist * speed;

                        if (!world.isRemote)
                        {
                            int powerUse = (int) (Math.max(1, Math.pow(e.width + e.height, powerScale)) / (dist * 2));
                            chestEnergy.extractEnergy(chest, powerUse, false);
                        }
                    }
                }
            }
        }
    }

    private void processBlocks(BlockCoord baseBlock, World world)
    {
        for (int bx = baseBlock.x - fieldRange; bx < baseBlock.x + fieldRange; bx++)
        {
            for (int by = baseBlock.y - fieldRange; by < baseBlock.y + fieldRange; by++)
            {
                for (int bz = baseBlock.z - fieldRange; bz < baseBlock.z + fieldRange; bz++)
                {
                    BlockCoord b = new BlockCoord(bx, by, bz);
                    Block block = b.getBlock(world);
                    if (block instanceof BlockFalling)
                    {
                        createFallingSandChance(world, b, (BlockFalling) block);
                    }
                }
            }
        }
    }

    private void createFallingSandChance(World world, BlockCoord b, BlockFalling block)
    {
        if (!world.isRemote && Utils.rand.nextInt(99) == 0)
        {
            EntityFallingBlock entity = new EntityFallingBlock(world, b.x + 0.5, b.y + 0.6, b.z + 0.5, block);
            world.spawnEntityInWorld(entity);
        }
    }
}