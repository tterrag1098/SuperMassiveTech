package tterrag.supermassivetech.item.armor;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import tterrag.supermassivetech.SuperMassiveTech;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.packet.PacketJumpUpdate;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ClientKeyHandler
{
    private boolean lastJumpState;
    public static KeyBinding toolPicker = new KeyBinding(Utils.localize("keybind.toolpicker.info", true), Keyboard.KEY_C, Reference.LOCALIZING + ": "
            + Utils.localize("keybind.section.graviArmor", true));

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null)
        {
            boolean jumpState = mc.gameSettings.keyBindJump.getIsKeyPressed();

            if (jumpState != lastJumpState)
            {
                lastJumpState = jumpState;
                SuperMassiveTech.channelHandler.sendToServer(new PacketJumpUpdate(jumpState));
            }

            ItemStack chest = mc.thePlayer.inventory.armorInventory[2];
            if (toolPicker.isPressed() && chest != null && chest.getItem() instanceof ItemGravityArmor)
            {
                boolean cur = chest.stackTagCompound.getBoolean("toolpickeractive");
                chest.stackTagCompound.setBoolean("toolpickeractive", !cur);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                HelmetOverlayHandler.textToRender.add(Utils.localize("keybind.toolpicker.info", true) + ": " + (!cur ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"));
            }
        }
    }
}
