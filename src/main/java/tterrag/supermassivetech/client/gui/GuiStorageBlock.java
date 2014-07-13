package tterrag.supermassivetech.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.container.ContainerBlackHoleStorage;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.util.Utils;

public class GuiStorageBlock extends GuiContainer
{
    public long itemsStored;
    public long fluidStored;
    public FluidStack currentFluid;

    private String formattedItemAmount = "";
    private String formattedFluidAmount = "";
    private long max = TileBlackHoleStorage.max;

    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/storageGui.png");
    private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;

    public GuiStorageBlock(InventoryPlayer par1InventoryPlayer, TileBlackHoleStorage tile)
    {
        super(new ContainerBlackHoleStorage(par1InventoryPlayer, tile));
        this.xSize = 250;
        this.ySize = 202;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        formattedItemAmount = Utils.formatString("Stored: ", "", itemsStored, true, true);

        formattedFluidAmount = Utils.formatString("Stored: ", " mB", fluidStored, true);

        this.mc.getTextureManager().bindTexture(TEXTURE);

        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(j + 13, k, 0, 0, this.xSize - 16, this.ySize);
        this.displayGauge(j, k, 12, 44, (int) ((((double) fluidStored / (double) getScaledLiquidAmount()) * 68) + 0.5), currentFluid);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        this.fontRendererObj.drawString(formattedItemAmount, (int) ((this.xSize / 1.31) - (formattedItemAmount.length() * 2.5)), 54, 0xCCCCCC);
        this.fontRendererObj.drawString(formattedFluidAmount, (int) ((this.xSize / 4) - (formattedFluidAmount.length() * 2.5)), 80, 0xCCCCCC);
        this.fontRendererObj.drawString("Current", (int) (this.xSize / 3.2), 30, 0xCCCCCC);
        this.fontRendererObj.drawString("Scale Max: ", (int) (this.xSize / 3.4), 40, 0xCCCCCC);
        this.fontRendererObj.drawString(getScaledLiquidMaxAsString(), ((int) (this.xSize / 2.6)) - Math.round(getScaledLiquidMaxAsString().length() * 2.5f), 50, 0xCCCCCC);
        this.fontRendererObj.drawString("Black Hole Storage", (int) (this.xSize / 3.2), 7, 0xCCCCCC);

        List<String> ttLines = new ArrayList<String>();
        if (currentFluid != null && mouseX < j + 68 && mouseX > j + 43 && mouseY > k + 6 && mouseY < k + 76)
        {
            ttLines.add(currentFluid.getFluid().getLocalizedName(currentFluid));
            this.func_146283_a(ttLines, mouseX - j, mouseY - k);
        }
    }

    /**
     * @author Buildcraft team
     */
    private void displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid)
    {
        if (liquid != null)
        {
            int start = 0;

            IIcon liquidIcon = null;
            Fluid fluid = liquid.getFluid();
            if (fluid != null && fluid.getStillIcon() != null)
            {
                liquidIcon = fluid.getStillIcon();
            }
            mc.renderEngine.bindTexture(BLOCK_TEXTURE);
            Utils.setGLColorFromInt(0xFFFFFF);

            int x;

            if (liquidIcon != null)
            {
                do
                {
                    if (squaled > 16)
                    {
                        x = 16;
                        squaled -= 16;
                    }
                    else
                    {
                        x = squaled;
                        squaled = 0;
                    }

                    drawTexturedModelRectFromIcon(j + col + 1, k + line + 63 - x - start, liquidIcon, 22, 16 - (16 - x));
                    start = start + 16;

                }
                while (x != 0 && squaled != 0);
            }
        }
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(j + col, k + line + 1, 234, 0, 16, 60);
    }

    private long getScaledLiquidAmount()
    {
        for (long i = 1000; i < 10000000000L; i *= 10)
            if (fluidStored < i)
                return i;

        return max;
    }

    private String getScaledLiquidMaxAsString()
    {
        long l = getScaledLiquidAmount();

        return Utils.formatString("", " mB", l, l == max);
    }
}
