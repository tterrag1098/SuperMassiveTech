package tterrag.supermassivetech.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;

import tterrag.supermassivetech.item.ItemGravityArmor;
import tterrag.supermassivetech.network.PacketHandler;
import tterrag.supermassivetech.network.message.MessageJumpUpdate;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor;
import tterrag.supermassivetech.network.message.MessageUpdateGravityArmor.PowerUps;
import tterrag.supermassivetech.util.Utils;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientKeyHandler
{
    private static class ArmorPower
    {
        private KeyBinding binding;
        private PowerUps powerEnum;
        private byte[] applicableArmorSlots;
        
        private boolean pressed;
        private int pressCount;

        private ArmorPower(KeyBinding binding, PowerUps power, byte... applicable)
        {
            this.binding = binding;
            this.powerEnum = power;
            this.applicableArmorSlots = applicable;
        }

        public static ArmorPower create(int key, PowerUps power, String unloc, int... applicable)
        {
            for (int i : applicable)
            {
                if (i < 0 || i > 3)
                    throw new IllegalArgumentException("Armor slot must be between 0 and 3");
            }

            KeyBinding bind = new KeyBinding(Utils.localize(unloc, true), key, Utils.localize("keybind.section.graviArmor", true));
            ClientRegistry.registerKeyBinding(bind);
            
            byte[] info = new byte[applicable.length];
            
            for (int i = 0; i < applicable.length; i++)
            {
                info[i] = (byte) applicable[i];
            }
            
            return new ArmorPower(bind, power, info);
        }

        public KeyBinding getBinding()
        {
            return binding;
        }

        public PowerUps getPowerType()
        {
            return powerEnum;
        }

        public byte[] getArmorSlots()
        {
            return applicableArmorSlots;
        }
        
        public void press()
        {
            pressed = true;
            pressCount++;
        }

        public boolean isPressed()
        {
            if (pressed)
            {
                if (pressCount > 1)
                {
                    pressCount--;
                    return true;
                }
                else
                {
                    pressCount = 0;
                    pressed = false;
                    return true;
                }
            }
            return false;
        }
    }

    private boolean lastJumpState;
    public static ArmorPower[] powers;
    
    public ClientKeyHandler()
    {
        powers = new ArmorPower[] {

                ArmorPower.create(Keyboard.KEY_C, PowerUps.TOOLPICKER, "keybind.toolpicker", 2), 
                ArmorPower.create(Keyboard.KEY_F, PowerUps.GRAV_RESIST, "keybind.gravResist", 0, 1, 2, 3),
                ArmorPower.create(Keyboard.KEY_O, PowerUps.HUD, "keybind.hud", 3) 
        };
    }

    @SubscribeEvent
    public void onKeyPress(KeyInputEvent event)
    {
        for (ArmorPower ap : powers)
        {
            if (Keyboard.getEventKey() == ap.getBinding().getKeyCode() && Keyboard.getEventKeyState())
            {
                ap.press();
            }
        }
    }
    
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        if (event.phase != Phase.END)
            return;

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null)
        {
            boolean jumpState = mc.gameSettings.keyBindJump.getIsKeyPressed();

            if (jumpState != lastJumpState)
            {
                lastJumpState = jumpState;
                PacketHandler.INSTANCE.sendToServer(new MessageJumpUpdate(jumpState));
            }

            ItemStack[] armors = mc.thePlayer.inventory.armorInventory;

            for (ArmorPower ap : powers)
            {
                for (int i : ap.getArmorSlots())
                {
                    ItemStack armor = armors[i];
                    if (ap.isPressed() && armor != null && armor.getItem() instanceof ItemGravityArmor)
                    {
                        boolean to = !armor.stackTagCompound.getBoolean(ap.getPowerType().toString());
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                        HelmetOverlayHandler.textToRender.add(ap.getBinding().getKeyDescription() + ": " + (to ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF"));
                        PacketHandler.INSTANCE.sendToServer(new MessageUpdateGravityArmor(ap.getPowerType(), to, ap.getArmorSlots()));
                    }
                }
            }
        }
    }
}
