package tterrag.supermassivetech.network;

import tterrag.supermassivetech.network.message.MessageBlackHoleStorage;
import tterrag.supermassivetech.network.message.MessageHopperParticle;
import tterrag.supermassivetech.network.message.MessageJumpUpdate;
import tterrag.supermassivetech.network.message.MessageStarHarvester;
import tterrag.supermassivetech.network.message.MessageStarHeartParticle;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor;
import tterrag.supermassivetech.network.message.MessageUpdateVenting;
import tterrag.supermassivetech.network.message.MessageWaypointUpdate;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("SMTech");

    public static void init()
    {
        INSTANCE.registerMessage(MessageBlackHoleStorage.class, MessageBlackHoleStorage.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MessageHopperParticle.class, MessageHopperParticle.class, 1, Side.CLIENT);
        INSTANCE.registerMessage(MessageJumpUpdate.class, MessageJumpUpdate.class, 2, Side.SERVER);
        INSTANCE.registerMessage(MessageStarHarvester.class, MessageStarHarvester.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageStarHeartParticle.class, MessageStarHeartParticle.class, 4, Side.CLIENT);
        INSTANCE.registerMessage(MessageWaypointUpdate.class, MessageWaypointUpdate.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateGravityArmor.class, MessageUpdateGravityArmor.class, 6, Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateVenting.class, MessageUpdateVenting.class, 7, Side.CLIENT);
    }
}
