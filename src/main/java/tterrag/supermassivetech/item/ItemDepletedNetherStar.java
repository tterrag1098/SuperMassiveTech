package tterrag.supermassivetech.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tterrag.supermassivetech.entity.item.EntityItemDepletedNetherStar;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class ItemDepletedNetherStar extends ItemSMT
{
    public final int maxDamage = 100;
    
    public ItemDepletedNetherStar()
    {
        super("depletedNetherStar", "depletedNetherStar");
    }
    
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        this.itemIcon = ObfuscationReflectionHelper.getPrivateValue(Item.class, Items.nether_star, "itemIcon", "field_77791_bV");
    }
    
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        float percent = ((float) par1ItemStack.getItemDamage()) / ((float) maxDamage);
        int color = (int) (0xFF * percent);  
        
        System.out.println(percent + " " + color);
        int hex = 0;
        hex = hex | ((int) (color) << 16);
        hex = hex | ((int) (color) << 8);
        hex = hex | ((int) (color));
        
        return hex;
    }
    
    @Override
    public boolean hasCustomEntity(ItemStack stack)
    {
        return true;
    }
    
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack)
    {
        return new EntityItemDepletedNetherStar(world, location.posX, location.posY, location.posZ, itemstack, location.motionX, location.motionY, location.motionZ,
                ((EntityItem) location).delayBeforeCanPickup);
    }
}

