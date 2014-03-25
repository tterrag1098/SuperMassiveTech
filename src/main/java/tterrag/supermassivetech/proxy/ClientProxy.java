package tterrag.supermassivetech.proxy;

import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.render.ItemObjRenderer;
import tterrag.supermassivetech.client.render.DirectionalObjRenderer;
import tterrag.supermassivetech.network.GuiHandler;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileStarHarvester;
import static tterrag.supermassivetech.SuperMassiveTech.*;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy
{
	public static final DirectionalObjRenderer hopper = new DirectionalObjRenderer(new ResourceLocation("supermassivetech", "models/hopper.obj"), new ResourceLocation("supermassivetech", "textures/models/hopper.png"));
	public static ItemObjRenderer hopperItem;

	public static final DirectionalObjRenderer starHarvester = new DirectionalObjRenderer(new ResourceLocation("supermassivetech", "models/starHarvester.obj"), new ResourceLocation("supermassivetech", "textures/models/hopperBlob.png"));
	public static ItemObjRenderer starHarvesterItem;

	@Override
	public void registerGuis()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SuperMassiveTech.instance, new GuiHandler());
	}

	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleHopper.class, hopper);
		ClientRegistry.bindTileEntitySpecialRenderer(TileStarHarvester.class, starHarvester);
		
		renderIDHopper = RenderingRegistry.getNextAvailableRenderId();
		hopperItem = new ItemObjRenderer(renderIDHopper, hopper);
		RenderingRegistry.registerBlockHandler(hopperItem);
		
		renderIDStarHarvester = RenderingRegistry.getNextAvailableRenderId();
		starHarvesterItem = new ItemObjRenderer(renderIDStarHarvester, starHarvester);
		RenderingRegistry.registerBlockHandler(starHarvesterItem);
	}
	
	@Override
	public void init()
	{
		hopperItem.init(blockRegistry.blackHoleHopper);
		starHarvesterItem.init(blockRegistry.starHarvester);
	}
}
