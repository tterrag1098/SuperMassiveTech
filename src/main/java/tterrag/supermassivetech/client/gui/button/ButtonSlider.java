package tterrag.supermassivetech.client.gui.button;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import org.lwjgl.opengl.GL11;

public class ButtonSlider extends GuiButton
{
    private boolean dragging;

    public float value;

    public ButtonSlider(int id, int x, int y, String text)
    {
        this(id, x, y, 150, 20, text);
    }

    public ButtonSlider(int id, int x, int y, int width, int height, String text)
    {
        super(id, x, y, width, height, text);
    }

    public ButtonSlider setValue(float def)
    {
        this.value = def;
        return this;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (dragging)
            {
                updateValue(mouseX);
            }

            GL11.glColor3f(1, 1, 1);
            this.drawTexturedModalRect(xPosition + (int) (value * (width - 8)), yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(xPosition + (int) (value * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            updateValue(mouseX);

            dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    private void updateValue(int mouseX)
    {
        value = (float) (mouseX - (xPosition + 4)) / (width - 8);

        if (value < 0)
        {
            value = 0;
        }

        if (value > 1)
        {
            value = 1;
        }
    }

    @Override
    public void mouseReleased(int par1, int par2)
    {
        this.dragging = false;
    }

    @Override
    public int getHoverState(boolean whatever)
    {
        return 0;
    }
}
