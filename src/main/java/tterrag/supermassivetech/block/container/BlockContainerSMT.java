package tterrag.supermassivetech.block.container;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.util.Utils;

public abstract class BlockContainerSMT extends BlockContainer
{

	Class<? extends TileEntity> teClass;
	private String unlocName;
	private int renderID;

	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te)
	{
		this(unlocName, mat, type, hardness, te, 0);
	}

	protected BlockContainerSMT(String unlocName, Material mat, SoundType type, float hardness, Class<? extends TileEntity> te, int renderID)
	{
		super(mat);
		setStepSound(type);
		setHardness(hardness);
		setCreativeTab(SuperMassiveTech.tabSMT);
		
		String toolLevel = Utils.getToolClassFromMaterial(mat);
		
		if (!toolLevel.equals("none"))
			setHarvestLevel("pickaxe", Utils.getToolLevelFromMaterial(mat));
		
		this.unlocName = unlocName;
		this.teClass = te;
		this.unlocName = unlocName;
		this.renderID = renderID;
	}

	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		this.blockIcon = register.registerIcon("supermassivetech:" + unlocName.substring(unlocName.indexOf(".") + 1, unlocName.length()));
	}
	
	@Override
	public String getUnlocalizedName()
	{
		return unlocName;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2)
	{
		try
		{
			return teClass.newInstance();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		return null;
	}

	public boolean hasPlacementRotation()
	{
		return true;
	}

	public boolean hasCustomModel()
	{
		return true;
	}

	@Override
	public int onBlockPlaced(World world, int x, int y, int z, int side, float hitx, float hity, float hitz, int meta)
	{
		int opp = Facing.oppositeSide[side];

		return hasPlacementRotation() ? opp : 0;
	}

	@Override
	public int getRenderType()
	{
		return renderID;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return !hasCustomModel();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return !hasCustomModel();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
	{
		if (this.keepInventoryAsItem())
		{
			TileEntity te = world.getTileEntity(x, y, z);

			if (te != null && te.getClass() == this.teClass && stack.stackTagCompound != null && !world.isRemote)
			{
				((IKeepInventoryAsItem) this).processBlockPlace(stack.stackTagCompound, te);
			}
		}
		else 
		{
			super.onBlockPlacedBy(world, x, y, z, player, stack);
		}
	}
	
	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int p_149681_5_, EntityPlayer player)
	{
		if (this.keepInventoryAsItem() && canHarvestBlock(player, world.getBlockMetadata(x, y, z)) && !player.capabilities.isCreativeMode && !world.isRemote)
		{
			((IKeepInventoryAsItem) this).dropItem(world, ((IKeepInventoryAsItem) this).getNBTItem(world, x, y, z), x, y, z);
		}
		else
		{
			super.onBlockHarvested(world, x, y, z, p_149681_5_, player);
		}
	}
	
	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
	{
		return this.keepInventoryAsItem() ? null : super.getItemDropped(p_149650_1_, p_149650_2_, p_149650_3_);
	}

	/**
	 * Whether this block keeps its inventory in item form. If this ever returns true the block MUST implement IKeepInventoryAsItem
	 */
	public boolean keepInventoryAsItem()
	{
		return this instanceof IKeepInventoryAsItem;
	}

	/**
	 * Base implementation of {@link IKeepInventoryAsItem} method
	 */
	public void dropItem(World world, ItemStack item, int x, int y, int z)
	{
		Utils.spawnItemInWorldWithRandomMotion(world, item, x, y, z);
	}
}
