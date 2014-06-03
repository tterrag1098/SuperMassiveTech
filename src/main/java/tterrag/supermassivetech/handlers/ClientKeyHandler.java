package tterrag.supermassivetech.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.network.PacketHandler;
import tterrag.supermassivetech.network.message.MessageJumpUpdate;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor.PowerUps;
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
                PacketHandler.INSTANCE.sendToServer(new MessageJumpUpdate(jumpState));
            }

            ItemStack chest = mc.thePlayer.inventory.armorInventory[2];
            if (toolPicker.isPressed() && chest != null && chest.getItem() instanceof ItemGravityArmor)
            {
                boolean to = !chest.stackTagCompound.getBoolean("toolpickeractive");
                chest.stackTagCompound.setBoolean(PowerUps.TOOLPICKER.toString(), to);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                HelmetOverlayHandler.textToRender.add(Utils.localize("keybind.toolpicker.info", true) + ": " + (!to ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"));
                PacketHandler.INSTANCE.sendToServer(new MessageUpdateGravityArmor(PowerUps.TOOLPICKER, to, (byte) 2));
            }
        }
    }
}
