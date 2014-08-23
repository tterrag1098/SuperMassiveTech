package tterrag.supermassivetech;

public class ModProps
{
    public static final String MODID = "superMassiveTech";
    public static final String MOD_TEXTUREPATH = "supermassivetech";
    public static final String MOD_NAME = "Super Massive Tech";
    public static final String LOCALIZING = "SMT";
    public static final String VERSION = "0.2.0-alpha";
    
    public static final String MAIN_PACKAGE = "tterrag.supermassivetech";
    public static final String CLIENT_PROXY_CLASS = MAIN_PACKAGE + ".client.ClientProxy";
    public static final String SERVER_PROXY_CLASS = MAIN_PACKAGE + ".common.CommonProxy";
    public static final String GUI_FACTORY_CLASS = MAIN_PACKAGE + ".common.config.ConfigFactorySMT";
    
    public static final String CHANNEL = "SMTech";
    
    // for dependencies
    public static final String TTCORE_VERSION = "1.7.10-0.0.2-2,)";
    public static final String FORGE_VERSION = "10.13.0.1171,)";
    public static final String ENDERIO_VERSION = "1.7.10-2.0.186_beta,)";
    
    public static final String DEPENDENCIES = "required-after:Forge@" + FORGE_VERSION + ";"
                                            + "required-after:ttCore@" + TTCORE_VERSION + ";"
                                            + "after:EnderIO@" + ENDERIO_VERSION + ";"
                                            + "after:Waila;";
}
