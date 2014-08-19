package tterrag.supermassivetech.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import tterrag.supermassivetech.client.gui.button.ButtonSlider;
import tterrag.supermassivetech.common.network.PacketHandler;
import tterrag.supermassivetech.common.network.message.tile.MessageWaypointUpdate;
import tterrag.supermassivetech.common.tile.TileWaypoint;

public class GuiWaypoint extends GuiScreen
{
    private TileWaypoint tile;

    private int r, g, b;

    private ButtonSlider redSlider, greenSlider, blueSlider;
    private GuiTextField textField;

    private int offset;
    private final int maxOffset = 100;
    private final int glideSpeed = 3;

    private boolean closing = false;

    public GuiWaypoint(TileWaypoint tile)
    {
        this.tile = tile;

        Color c = tile.waypoint.getColor();

        r = c.getRed();
        g = c.getGreen();
        b = c.getBlue();

        this.mc = Minecraft.getMinecraft();

        offset = maxOffset;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        int x = this.width / 2 - 70;
        int y = this.height - 50 + offset;

        this.buttonList.add(redSlider = new ButtonSlider(0, x, y - 50, "Red").setValue((float) r / 255));
        this.buttonList.add(greenSlider = new ButtonSlider(1, x, y - 30, "Green").setValue((float) g / 255));
        this.buttonList.add(blueSlider = new ButtonSlider(2, x, y - 10, "Blue").setValue((float) b / 255));

        textField = new GuiTextField(mc.fontRenderer, this.width / 2 - 70, this.height - 30, 150, 20);
        textField.setText(this.tile.waypoint.getName());
        textField.setVisible(false);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        int x = this.width / 2 - 70 + 155;
        int y = this.height - 50 + offset;

        drawString(mc.fontRenderer, Integer.toString(r), x, y - 45, 0xFF0000);
        drawString(mc.fontRenderer, Integer.toString(g), x, y - 25, 0x00FF00);
        drawString(mc.fontRenderer, Integer.toString(b), x, y - 5, 0x0000FF);

        textField.drawTextBox();

        if (offset <= 0)
        {
            textField.setVisible(true);
        }
        else
        {
            textField.setVisible(false);
        }

        if (offset > 0 && !closing)
        {
            for (Object o : this.buttonList)
            {
                ((ButtonSlider) o).yPosition -= glideSpeed;
            }

            offset -= glideSpeed;
        }
        else if (offset < maxOffset && closing)
        {
            for (Object o : this.buttonList)
            {
                ((ButtonSlider) o).yPosition += glideSpeed;
            }

            offset += glideSpeed;
        }

        if (closing && offset >= maxOffset)
        {
            this.mc.displayGuiScreen((GuiScreen) null);
            this.mc.setIngameFocus();
        }

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        textField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void keyTyped(char par1, int par2)
    {
        if (!closing)
        {
            this.textField.textboxKeyTyped(par1, par2);
        }

        if (par2 == 1)
        {
            closing = true;
        }
    }

    @Override
    public void updateScreen()
    {
        r = (int) (redSlider.value * 255);
        g = (int) (greenSlider.value * 255);
        b = (int) (blueSlider.value * 255);

        tile.waypoint.setColor(r, g, b);
        tile.waypoint.setName(textField.getText());

        super.updateScreen();
    }

    @Override
    public void onGuiClosed()
    {
        PacketHandler.INSTANCE.sendToServer(new MessageWaypointUpdate(r, g, b, tile.waypoint.getName(), tile.xCoord, tile.yCoord, tile.zCoord));
        super.onGuiClosed();
    }
}
