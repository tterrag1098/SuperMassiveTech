package tterrag.supermassivetech.common.config;

import net.minecraft.client.gui.GuiScreen;
import tterrag.core.common.config.BaseConfigFactory;
import tterrag.supermassivetech.client.config.ConfigGuiSMT;

public class ConfigFactorySMT extends BaseConfigFactory
{
    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return ConfigGuiSMT.class;
    }
}
