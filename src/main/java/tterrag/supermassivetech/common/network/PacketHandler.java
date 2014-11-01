package tterrag.supermassivetech.common.network;

import tterrag.supermassivetech.ModProps;
import tterrag.supermassivetech.common.network.message.MessageJumpUpdate;
import tterrag.supermassivetech.common.network.message.MessageStarHeartParticle;
import tterrag.supermassivetech.common.network.message.MessageUpdateGravityArmor;
import tterrag.supermassivetech.common.network.message.tile.MessageBlackHoleStorage;
import tterrag.supermassivetech.common.network.message.tile.MessageChargerUpdate;
import tterrag.supermassivetech.common.network.message.tile.MessageEnergyUpdate;
import tterrag.supermassivetech.common.network.message.tile.MessageHopperParticle;
import tterrag.supermassivetech.common.network.message.tile.MessageStarHarvester;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateBlackHole;
import tterrag.supermassivetech.common.network.message.tile.MessageUpdateVenting;
import tterrag.supermassivetech.common.network.message.tile.MessageWaypointUpdate;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModProps.CHANNEL);
    private static int id = 0;

    public static void init()
    {
        INSTANCE.registerMessage(MessageBlackHoleStorage.class, MessageBlackHoleStorage.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageHopperParticle.class, MessageHopperParticle.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageJumpUpdate.class, MessageJumpUpdate.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageStarHarvester.class, MessageStarHarvester.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageStarHeartParticle.class, MessageStarHeartParticle.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageWaypointUpdate.class, MessageWaypointUpdate.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateGravityArmor.class, MessageUpdateGravityArmor.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateVenting.class, MessageUpdateVenting.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageEnergyUpdate.class, MessageEnergyUpdate.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageChargerUpdate.Handler.class, MessageChargerUpdate.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageUpdateBlackHole.class, MessageUpdateBlackHole.class, id++, Side.CLIENT);
    }
}
