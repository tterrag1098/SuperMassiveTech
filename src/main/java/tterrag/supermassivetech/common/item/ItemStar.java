package tterrag.supermassivetech.common.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tterrag.supermassivetech.api.common.item.IAdvancedTooltip;
import tterrag.supermassivetech.api.common.item.IStarItem;
import tterrag.supermassivetech.api.common.registry.IStar;
import tterrag.supermassivetech.common.entity.item.EntityItemStar;
import tterrag.supermassivetech.common.registry.Stars;
import tterrag.supermassivetech.common.registry.Stars.StarTier;
import tterrag.supermassivetech.common.util.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemStar extends ItemSMT implements IAdvancedTooltip, IStarItem
{
    protected Stars stars = Stars.instance;

    public ItemStar(String unlocName)
    {
        this(unlocName, unlocName);
    }

    public ItemStar(String unlocName, String textureName)
    {
        super(unlocName, textureName);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (!par2World.isRemote)
            Utils.applyGravPotionEffects((EntityPlayer) par3Entity, Utils.getType(par1ItemStack).getMassLevel());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        IStar type = Utils.getType(par1ItemStack);

        if (type == null)
        {
            return this instanceof ItemStarSpecial ? Stars.instance.getTypeByName("neutron").getColor() : 0xFFFF00;
        }
        else
            return type.getColor();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (IStar t : stars.types.values())
        {
            if (t.getTier() != StarTier.SPECIAL)
                list.add(Utils.setType(new ItemStack(this), t));
        }
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return new EntityItemStar(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
                ((EntityItem) location).delayBeforeCanPickup);
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return true;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase) entity).setFire(10);
        }
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase player)
    {
        if (block.isFlammable(world, x, y - 1, z, ForgeDirection.UP))
            world.setBlock(x, y, z, Blocks.fire);

        return world.isRemote;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getHiddenLines(ItemStack stack)
    {
        IStar type = Utils.getType(stack);

        if (type == null)
        {
            return null;
        }

        double powerLeft = stack.getTagCompound().getInteger("energy"), maxPower = type.getMaxEnergyStored(stack);

        return String.format("%s|%s|%s RF %s %d RF/t|%s|",

        type.getTextColor() + type.toString(), Stars.getEnumColor(type.getTier()) + type.getTier().toString(),
                Utils.formatString(EnumChatFormatting.YELLOW + Utils.lang.localize("tooltip.outputs") + " ", "", type.getMaxEnergyStored(stack), false),
                Utils.lang.localize("tooltip.at"), type.getPowerPerTick(),
                Utils.formatString(Utils.getColorForPowerLeft(powerLeft, maxPower) + Utils.lang.localize("tooltip.powerRemaining") + ": ", " RF", (long) powerLeft, true));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getStaticLines(ItemStack stack)
    {
        IStar type = Utils.getType(stack);
        return type != null ? null : Utils.lang.localize("tooltip.anyType");
    }
}
