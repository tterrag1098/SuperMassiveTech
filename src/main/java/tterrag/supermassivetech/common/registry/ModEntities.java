package tterrag.supermassivetech.common.registry;

import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.common.entity.EntityDyingBlock;
import tterrag.supermassivetech.common.entity.EntityFormingStar;
import tterrag.supermassivetech.common.entity.item.EntityItemDepletedNetherStar;
import tterrag.supermassivetech.common.entity.item.EntityItemIndestructible;
import tterrag.supermassivetech.common.entity.item.EntityItemStar;
import tterrag.supermassivetech.common.entity.item.EntityItemStarHeart;
import cpw.mods.fml.common.registry.EntityRegistry;

public class ModEntities
{
    public static final ModEntities instance = new ModEntities();

    private ModEntities()
    {}

    public void init()
    {
        int id = 0;

        EntityRegistry.registerModEntity(EntityItemDepletedNetherStar.class, "tterrag.smt.entityDepletedNetherStar", id++, SuperMassiveTech.instance, 80, 80, true);
        EntityRegistry.registerModEntity(EntityItemStar.class, "tterrag.smt.entitySpecialStar", id++, SuperMassiveTech.instance, 80, 80, true);
        EntityRegistry.registerModEntity(EntityItemStarHeart.class, "tterrag.smt.entityStarHeart", id++, SuperMassiveTech.instance, 80, 80, true);
        EntityRegistry.registerModEntity(EntityItemIndestructible.class, "tterrag.smt.entityItemIndestructible", id++, SuperMassiveTech.instance, 80, 80, true);
        EntityRegistry.registerModEntity(EntityFormingStar.class, "tterrag.smt.entityFormingStar", id++, SuperMassiveTech.instance, 80, 3, false);
        EntityRegistry.registerModEntity(EntityDyingBlock.class, "tterrag.smt.entityDyingBlock", id++, SuperMassiveTech.instance, 80, 3, true);
    }
}
