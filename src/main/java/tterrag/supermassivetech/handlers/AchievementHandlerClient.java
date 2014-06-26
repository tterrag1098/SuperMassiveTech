package tterrag.supermassivetech.handlers;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import tterrag.supermassivetech.network.PacketHandler;
import tterrag.supermassivetech.network.message.MessageAchievementToServer;
import tterrag.supermassivetech.registry.Achievements;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AchievementHandlerClient
{
    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent event)
    {
        String text = event.message.getUnformattedText();
        if (text.contains(StatCollector.translateToLocal("chat.type.achievement").replace("%s", "").replace("  ", " ").trim()))
        {
            for (int i = 0; i < AchievementList.achievementList.size(); i++)
            {
                Achievement a = (Achievement) AchievementList.achievementList.get(i);
                String localizeableAchName = a.toString().substring(a.toString().indexOf("key='") + 5, a.toString().indexOf("', args"));
                String localizedName = StatCollector.translateToLocal(localizeableAchName);
                if (text.contains(localizedName) && !Achievements.eventRequired.values().contains(a))
                {
                    PacketHandler.INSTANCE.sendToServer(new MessageAchievementToServer());
                }
            }
        }
    }
}
