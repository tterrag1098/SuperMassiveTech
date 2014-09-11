package tterrag.supermassivetech.common.registry;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.entity.EntityFormingStar;
import tterrag.supermassivetech.common.entity.item.EntityItemDepletedNetherStar;
import tterrag.supermassivetech.common.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.common.entity.item.EntityItemSpecialStar;
import tterrag.supermassivetech.common.entity.item.EntityItemStarHeart;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static final ModEntities instance = new ModEntities();
    private ModEntities() {}

    public void init()
    {
        EntityRegistry.registerModEntity(EntityItemDepletedNetherStar.class, "tterrag.smt.entityDepletedNetherStar", 1, SuperMassiveTech.instance, 80, 80, true);

        EntityRegistry.registerModEntity(EntityItemSpecialStar.class, "tterrag.smt.entitySpecialStar", 1, SuperMassiveTech.instance, 80, 80, true);

        EntityRegistry.registerModEntity(EntityItemStarHeart.class, "tterrag.smt.entityStarHeart", 1, SuperMassiveTech.instance, 80, 80, true);

        EntityRegistry.registerModEntity(EntityItemIndestructible.class, "tterrag.smt.entityItemIndestructible", 1, SuperMassiveTech.instance, 80, 80, true);

        EntityRegistry.registerModEntity(EntityFormingStar.class, "tterrag.smt.entityFormingStar", 0, SuperMassiveTech.instance, 80, 3, false);
    }
}
