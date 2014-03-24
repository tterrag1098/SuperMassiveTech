package tterrag.supermassivetech.proxy;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.client.render.HopperItemRenderer;
import tterrag.supermassivetech.client.render.HopperRenderer;
import tterrag.supermassivetech.network.GuiHandler;
import tterrag.supermassivetech.tile.TileBlackHoleHopper;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerGuis()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SuperMassiveTech.instance, new GuiHandler());
	}

	@Override
	public void registerRenderers()
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleHopper.class, new HopperRenderer());

		SuperMassiveTech.renderIDHopper = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new HopperItemRenderer());
	}
}
