package tterrag.supermassivetech.registry;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.entity.EntityFormingStar;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static final ModEntities instance = new ModEntities();
    
    public void init()
    {
        EntityRegistry.registerModEntity(EntityFormingStar.class, "tterrag.smt.entityFormingStar", 0, SuperMassiveTech.instance, 80, 3, false);
    }
}
