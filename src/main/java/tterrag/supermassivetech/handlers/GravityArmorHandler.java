package tterrag.supermassivetech.handlers;

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
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor.PowerUps;
import tterrag.supermassivetech.util.BlockCoord;
import tterrag.supermassivetech.util.ClientUtils;
import tterrag.supermassivetech.util.Constants;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class GravityArmorHandler
{
    public static boolean isJumpKeyDown;
    private Field isHittingBlock;

    private final int antiGravRange = ConfigHandler.antiGravRange;
    private final int antiGravUsageBase = ConfigHandler.antiGravUsageBase;

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

        if (event.phase == Phase.END && !event.player.onGround && !event.player.capabilities.isFlying && (isJumpKeyDown || (event.player.motionY < -0.2 && !event.player.isSneaking())))
        {
            double effect = getArmorMult(event.player, Constants.instance().ENERGY_DRAIN / 50);
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

    private double getArmorMult(EntityPlayer player, int drainAmount)
    {
        // no power loss in creative, still get effects
        if (player.capabilities.isCreativeMode && drainAmount != 0)
            return getArmorMult(player, 0);

        double effect = 0;
        for (int i = 0; i < 4; i++)
        {
            ItemStack stack = player.inventory.armorInventory[i];
            if (stack != null && SuperMassiveTech.itemRegistry.armors.contains(stack.getItem()) && hasPowerUpOn(player, PowerUps.GRAV_RESIST, i))
            {
                int drained = ((IEnergyContainerItem) stack.getItem()).extractEnergy(stack, drainAmount, false);
                effect += drained > 0 || drained == drainAmount ? .072d / 4d : 0;
            }
            else if (stack != null && EnchantmentHelper.getEnchantmentLevel(ConfigHandler.gravEnchantID, stack) != 0)
            {
                effect += .072d / 5d;
                stack.damageItem(new Random().nextInt(1000) < 2 && !player.worldObj.isRemote ? 1 : 0, player);
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

            ItemStack[] armor = player.inventory.armorInventory;

            if (player.getCurrentEquippedItem() != null && checkArmor(armor[2]) && armor[2].stackTagCompound.getBoolean(PowerUps.TOOLPICKER.toString())
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
    public void doAntiGravField(PlayerTickEvent event)
    {
        if (event.phase != Phase.END)
            return;

        EntityPlayer player = event.player;
        ItemStack chest = player.inventory.armorInventory[2];
        if (checkArmor(chest) && chest.stackTagCompound.getBoolean(PowerUps.FIELD.toString()))
        {
            IEnergyContainerItem chestEnergy = (IEnergyContainerItem) chest.getItem();
            World world = player.worldObj;

            double x = player.posX, y = player.posY, z = player.posZ;
            int powerScale = antiGravUsageBase;

            if (chestEnergy.extractEnergy(chest, powerScale, true) < powerScale)
                return;

            int r = antiGravRange;

            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(x - r, y - r, z - r, x + r, y + r, z + r);

            BlockCoord baseBlock = new BlockCoord((int) player.posX, (int) player.posY, (int) player.posZ);
            processBlocks(baseBlock, world);

            List<Entity> entities = null;

            final double defaultEffect = 0.09;
            double effect = defaultEffect;

            if (ConfigHandler.antiGravIgnorePlayers)
            {
                entities = world.getEntitiesWithinAABBExcludingEntity(player, bb, NoPlayersSelector.instance);
            }
            else
            {
                entities = world.getEntitiesWithinAABBExcludingEntity(player, bb);
            }

            for (Entity e : entities)
            {
                if (e.posY > 256)
                    continue;

                if (!ConfigHandler.antiGravIgnorePlayers && e instanceof EntityPlayer)
                {
                    if (hasPowerUpOn((EntityPlayer) e, PowerUps.FIELD, 2))
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
                        for (int i = 0; i < 4; i++)
                        {
                            ItemStack stack = player.inventory.armorInventory[i];
                            if (checkArmor(stack) && ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack) > 0 && hasPowerUpOn((EntityPlayer) e, PowerUps.GRAV_RESIST, i))
                            {
                                effect -= defaultEffect * 0.05;
                            }
                        }
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
    }
    
    @SubscribeEvent
    public void onCrafted(ItemCraftedEvent event)
    {
        if (event.player.getCommandSenderName().equals("Roxarthenoob") && !event.player.worldObj.isRemote)
        {            
            EntityPlayer p = event.player;
            Random rand = event.player.worldObj.rand;

            if (rand.nextInt(1000) == 999)
            {
                event.player.worldObj.createExplosion(null, p.posX, p.posY, p.posZ, 10f, false);
                return;
            }
            
            ItemStack firework = new ItemStack(Items.fireworks);
            firework.stackTagCompound = new NBTTagCompound();
            NBTTagCompound expl = new NBTTagCompound();
            expl.setBoolean("Flicker", true);
            expl.setBoolean("Trail", true);
            
            int[] colors = new int[rand.nextInt(8) + 1];
            for (int i = 0; i < colors.length; i++)
            {
                colors[i] = ItemDye.field_150922_c[rand.nextInt(16)];
            }
            expl.setIntArray("Colors", colors);
            byte type = (byte) (rand.nextInt(3) + 1);
            type = type == 3 ? 4 : type;
            expl.setByte("Type", type);

            NBTTagList explosions = new NBTTagList();
            explosions.appendTag(expl); 

            NBTTagCompound fireworkTag = new NBTTagCompound();
            fireworkTag.setTag("Explosions", explosions);
            fireworkTag.setByte("Flight", (byte) 1);
            firework.stackTagCompound.setTag("Fireworks", fireworkTag);
            
            EntityFireworkRocket e = new EntityFireworkRocket(event.player.worldObj, event.player.posX, event.player.posY, event.player.posZ, firework);
            event.player.worldObj.spawnEntityInWorld(e);
        }
    }

    private void processBlocks(BlockCoord baseBlock, World world)
    {
        int bx = baseBlock.x - antiGravRange;
        for (; bx < baseBlock.x + antiGravRange; bx++)
        {
            int by = baseBlock.y - antiGravRange;
            for (; by < baseBlock.y + antiGravRange; by++)
            {
                int bz = baseBlock.z - antiGravRange;
                for (; bz < baseBlock.z + antiGravRange; bz++)
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
        if (!world.isRemote && world.rand.nextInt(100) == 99)
        {
            EntityFallingBlock entity = new EntityFallingBlock(world, b.x + 0.5, b.y + 0.5, b.z + 0.5, block);
            world.spawnEntityInWorld(entity);
        }
    }

    private boolean hasPowerUpOn(EntityPlayer e, PowerUps power, int slot)
    {
        ItemStack armor = e.inventory.armorInventory[slot];
        return checkArmor(armor) && armor.stackTagCompound.getBoolean(power.toString());
    }

    private boolean checkArmor(ItemStack stack)
    {
        return stack != null && stack.getItem() instanceof ItemGravityArmor;
    }
}