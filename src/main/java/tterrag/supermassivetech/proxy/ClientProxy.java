package tterrag.supermassivetech.proxy;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.network.GuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerGuis()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(SuperMassiveTech.instance, new GuiHandler());
	}
}
