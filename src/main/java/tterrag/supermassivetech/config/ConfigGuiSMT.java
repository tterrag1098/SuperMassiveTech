package tterrag.supermassivetech.config;

import static tterrag.supermassivetech.config.ConfigHandler.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

@SuppressWarnings("rawtypes")
public class ConfigGuiSMT extends GuiConfig
{
    public ConfigGuiSMT(GuiScreen parentScreen)
    {
        super(parentScreen, getConfigElements(parentScreen), Reference.MODID, false, false, Utils.localize("config.title", true));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent)
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = Reference.LOCALIZING + ".config.";
       
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionArmor.toLowerCase()).setLanguageKey(prefix + sectionArmor.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionEnchants.toLowerCase()).setLanguageKey(prefix + sectionEnchants.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionGravity.toLowerCase()).setLanguageKey(prefix + sectionGravity.toLowerCase().replace(" ", ""))));
        list.add(new ConfigElement<ConfigCategory>(config.getCategory(sectionMisc.toLowerCase()).setLanguageKey(prefix + sectionMisc.toLowerCase().replace(" ", ""))));

        return list;
    }
}
