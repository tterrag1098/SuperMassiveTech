package tterrag.supermassivetech.client.config;

import net.minecraft.client.gui.GuiScreen;
import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.common.config.ConfigHandler;
import tterrag.supermassivetech.common.util.Utils;

import com.enderio.core.api.common.config.IConfigHandler;
import com.enderio.core.client.config.BaseConfigGui;

public class ConfigGuiSMT extends BaseConfigGui
{
    public ConfigGuiSMT(GuiScreen parentScreen)
    {
        super(parentScreen);
    }   
    
    @Override
    protected String getTitle()
    {
        return Utils.lang.localize("config.title");
    }
    
    @Override
    protected String getLangPrefix()
    {
        return ModProps.LOCALIZING + ".config.";
    }
    
    @Override
    protected IConfigHandler getConfigHandler()
    {
        return ConfigHandler.INSTANCE;
    }
}
