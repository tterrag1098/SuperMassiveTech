package tterrag.supermassivetech;

public class ModProps
{
    public static final String MODID = "superMassiveTech";
    public static final String MOD_TEXTUREPATH = "supermassivetech";
    public static final String MOD_NAME = "Super Massive Tech";
    public static final String LOCALIZING = "SMT";
    public static final String VERSION = "@VERSION@";

    public static final String MAIN_PACKAGE = "tterrag.supermassivetech";
    public static final String CLIENT_PROXY_CLASS = MAIN_PACKAGE + ".client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = MAIN_PACKAGE + ".common.CommonProxy";
    public static final String GUI_FACTORY_CLASS = MAIN_PACKAGE + ".common.config.ConfigFactorySMT";

    public static final String CHANNEL = "SMTech";

    // for dependencies

    public static final String DEPENDENCIES = "after:endercore;after:EnderIO"; // replaced by build file
}
