package tterrag.supermassivetech.entity;

import java.util.LinkedList;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.registry.Stars.StarType;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;

public class EntityStarHeart extends EntityItemIndestructible
{
	public class BlockCoord
	{
		public int x, y, z;
		
		public BlockCoord(int x, int y, int z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public boolean equals(Object obj) 
		{
			if (obj instanceof BlockCoord)
			{
				BlockCoord coord = (BlockCoord) obj;
				
				return coord.x == this.x && coord.y == this.y && coord.z == this.z;
			}
			
			return false;
		}
	}
	
	public EntityStarHeart(World world, double posX, double posY, double posZ, ItemStack itemstack, double motionX, double motionY, double motionZ, int delay)
	{
		super(world, posX, posY, posZ, itemstack, motionX, motionY, motionZ, delay);
	}

	private boolean ready;
	private int explodeTimer = -1, particlesLeft = 0;
	private final int TIMER_MAX = 60;
	private BlockCoord toRemove = null;
	
	private LinkedList<BlockCoord> fire = new LinkedList<BlockCoord>();
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (explodeTimer < 0)
		{	
			if (this.isBurning() && ready)
			{
				explodeTimer = TIMER_MAX;
			}
		
			ready = !this.isBurning() && isInValidState();
		}
		else if (explodeTimer == TIMER_MAX)
		{
			getFire();
			explodeTimer--;
		}
		else if (explodeTimer == 0)
		{
			if (!fire.isEmpty())
				explodeTimer++;
			else
				changeToStar();
		}
		
		if (explodeTimer > 0 && explodeTimer < TIMER_MAX)
		{
			if (fire.size() > 0/* && explodeTimer % 4 == 0*/)
			{
				if (particlesLeft <= 0)
				{
					toRemove = fire.remove(new Random().nextInt(fire.size()));
					if (worldObj.getBlock(toRemove.x, toRemove.y, toRemove.z) == Blocks.fire)
					{
						worldObj.setBlockToAir(toRemove.x, toRemove.y, toRemove.z);
						particlesLeft = 6 + new Random().nextInt(2) - 1;
					}
					explodeTimer--;
				}
				else if (particlesLeft > 0)
				{
					spawnInwardParticle(toRemove.x, toRemove.y, toRemove.z);
					particlesLeft--;
				}
				else explodeTimer--;
			}
			else explodeTimer--;
			explodeTimer = Math.max(0, explodeTimer);
		}
	}
	
	private void changeToStar()
	{
		ItemStack star = new ItemStack(SuperMassiveTech.itemRegistry.star, this.getEntityItem().stackSize);

		// Sets the type of the star to a random type
		Utils.setType(star, (StarType) SuperMassiveTech.starRegistry.types.values().toArray()[new Random().nextInt(SuperMassiveTech.starRegistry.types.values().size())]);

		worldObj.newExplosion(this, posX, posY, posZ, 3.0f + (this.getEntityItem().stackSize), true, true);

		worldObj.spawnEntityInWorld(new EntityItem(worldObj, posX, posY, posZ, star));
		
		this.setDead();
	}

	@Override
	public boolean isBurning()
	{
		boolean flag = this.worldObj != null && this.worldObj.isRemote;
		// TODO PR forge or AT
		Integer fire = ObfuscationReflectionHelper.getPrivateValue(Entity.class, this, "fire", "field_70151_c");
		return (fire > 0 || flag && this.getFlag(0));
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
	{
		if (par1DamageSource.isFireDamage())
			return false;
		else
			return super.attackEntityFrom(par1DamageSource, par2);
	}
	
	private boolean isInValidState()
	{
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i != 0)
				{
					if (worldObj.getBlock((int) posX + i, (int) posY, (int) posZ + j) != Blocks.fire)
						return false;
				}
			}
		}
		
		return true;
	}
	
	private void getFire()
	{
		for (int x = -5; x <= 5; x++)
		{
			for (int y = -5; y <= 5; y++)
			{
				for (int z = -5; z <= 5; z++)
				{
					if (worldObj.getBlock((int) posX + x, (int) posY + y , (int) posZ + z) == Blocks.fire)
					{
						fire.add(new BlockCoord((int) posX + x, (int) posY + y, (int) posZ + z));
					}
				}
			}
		}	
	}
	
	private void spawnInwardParticle(int x, int y, int z) 
	{
		if (Minecraft.getMinecraft().thePlayer != null && FMLClientHandler.instance().getClient().thePlayer.worldObj != null)
		{
			System.out.println("particle");
			FMLClientHandler.instance().getClient().thePlayer.worldObj.spawnParticle("flame", x + 0.5, y + 0.5, z + 0.5, (posX - x - 0.5) / 13, (posY - y - 0.5) / 13, (posZ - z - 0.5) / 13);
		}
	}

}
