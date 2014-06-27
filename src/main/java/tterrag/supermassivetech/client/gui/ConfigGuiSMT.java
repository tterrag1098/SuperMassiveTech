package tterrag.supermassivetech.client.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import tterrag.supermassivetech.config.ConfigHandler;
import tterrag.supermassivetech.lib.Reference;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConfigGuiSMT extends GuiConfig
{
    private static Map<Class<? extends SMTEntry>, String> sections = new HashMap<Class<? extends SMTEntry>, String>();

    public ConfigGuiSMT(GuiScreen parentScreen)
    {
        super(parentScreen, getConfigElements(), "Forge", false, false, I18n.format("SMT.config.title"));
    }

    private static List<IConfigElement> getConfigElements()
    {
        sections.put(GravityEntry.class, ConfigHandler.sectionGravity);
        sections.put(EnchantsEntry.class, ConfigHandler.sectionEnchants);
        sections.put(MiscEntry.class, ConfigHandler.sectionMisc);
        sections.put(ArmorEntry.class, ConfigHandler.sectionArmor);

        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.add(new DummyCategoryElement(sections.get(GravityEntry.class), "SMT.config.section.gravity", GravityEntry.class));
        list.add(new DummyCategoryElement(sections.get(EnchantsEntry.class), "SMT.config.section.enchant", EnchantsEntry.class));
        list.add(new DummyCategoryElement(sections.get(MiscEntry.class), "SMT.config.section.misc", MiscEntry.class));
        list.add(new DummyCategoryElement(sections.get(ArmorEntry.class), "SMT.config.section.armor", ArmorEntry.class));

        return list;
    }

    private static class SMTEntry extends CategoryEntry
    {
        public SMTEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
        {
            super(owningScreen, owningEntryList, configElement);
        }

        @Override
        protected GuiScreen buildChildScreen()
        {
            String category = sections.get(this.getClass());
            return new GuiConfig(this.owningScreen,
                    (new ConfigElement(ConfigHandler.config.getCategory(category.toLowerCase()))).getChildElements(), Reference.MODID,
                    category, this.configElement.requiresWorldRestart() || this.owningScreen.allRequireWorldRestart,
                    this.configElement.requiresMcRestart() || this.owningScreen.allRequireMcRestart,
                    GuiConfig.getAbridgedConfigPath(ConfigHandler.config.getConfigFile().getAbsolutePath()));
        }
    }

    public static class GravityEntry extends SMTEntry
    {
        public GravityEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
        {
            super(owningScreen, owningEntryList, configElement);
        }
    }

    public static class ArmorEntry extends SMTEntry
    {
        public ArmorEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
        {
            super(owningScreen, owningEntryList, configElement);
        }
    }

    public static class MiscEntry extends SMTEntry
    {
        public MiscEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
        {
            super(owningScreen, owningEntryList, configElement);
        }
    }

    public static class EnchantsEntry extends SMTEntry
    {
        public EnchantsEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
        {
            super(owningScreen, owningEntryList, configElement);
        }
    }
}
