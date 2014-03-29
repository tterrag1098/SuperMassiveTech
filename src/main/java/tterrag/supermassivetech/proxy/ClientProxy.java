package tterrag.supermassivetech.proxy;

import static tterrag.supermassivetech.SuperMassiveTech.blockRegistry;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDHopper;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDStarHarvester;
import static tterrag.supermassivetech.SuperMassiveTech.renderIDStorage;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.model.ModelBlackHoleStorage;
import tterrag.supermassivetech.client.render.DirectionalModelRenderer;
import tterrag.supermassivetech.client.render.ItemObjRenderer;
import tterrag.supermassivetech.network.GuiHandler;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.tile.TileStarHarvester;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy
{
	public static final DirectionalModelRenderer storage = new DirectionalModelRenderer(new ModelBlackHoleStorage(), new ResourceLocation("supermassivetech", "textures/models/storage.png"));
	public static ItemObjRenderer storageItem;

	public static final DirectionalModelRenderer hopper = new DirectionalModelRenderer(new ResourceLocation("supermassivetech", "models/hopper.obj"), new ResourceLocation("supermassivetech", "textures/models/hopper.png"));
	public static ItemObjRenderer hopperItem;

	public static final DirectionalModelRenderer starHarvester = new DirectionalModelRenderer(new ResourceLocation("supermassivetech", "models/starHarvester.obj"), new ResourceLocation("supermassivetech", "textures/models/starHarvester.png"));
	public static ItemObjRenderer starHarvesterItem;

	@Override
	public void registerGuis()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SuperMassiveTech.instance, new GuiHandler());
	}

	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleStorage.class, storage);
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleHopper.class, hopper);
		ClientRegistry.bindTileEntitySpecialRenderer(TileStarHarvester.class, starHarvester);

		renderIDStorage = RenderingRegistry.getNextAvailableRenderId();
		storageItem = new ItemObjRenderer(renderIDStorage, storage);
		RenderingRegistry.registerBlockHandler(storageItem);

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
		storageItem.init(blockRegistry.blackHoleStorage);
		hopperItem.init(blockRegistry.blackHoleHopper);
		starHarvesterItem.init(blockRegistry.starHarvester);
	}
}
