package tterrag.supermassivetech.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import tterrag.supermassivetech.container.ContainerStorageBlock;
import tterrag.supermassivetech.lib.Reference;
import tterrag.supermassivetech.tile.TileBlackHoleStorage;
import tterrag.supermassivetech.util.Utils;

public class GuiStorageBlock extends GuiContainer
{
	public long itemsStored;
	public long fluidStored;
	private String formattedItemAmount = "";
	private String formattedFluidAmount = "";
	private long max = TileBlackHoleStorage.max;

	public int fluidID;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/storageGui.png");
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;

	public GuiStorageBlock(InventoryPlayer par1InventoryPlayer, TileBlackHoleStorage tile)
	{
		super(new ContainerStorageBlock(par1InventoryPlayer, tile));
		this.xSize = 250;
		this.ySize = 202;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		formattedItemAmount = "Stored: ";
		if (itemsStored < 1000000)
			formattedItemAmount += itemsStored;
		else if (itemsStored >= 1000000)
			formattedItemAmount = Utils.formatString(formattedItemAmount, itemsStored, false, true);

		formattedFluidAmount = "Stored: ";
		if (fluidStored < 1000000)
			formattedFluidAmount += fluidStored + "mb";
		else if (fluidStored >= 1000000)
			formattedFluidAmount = Utils.formatString(formattedFluidAmount, fluidStored, true, true);

		this.mc.getTextureManager().bindTexture(new ResourceLocation(Reference.MOD_TEXTUREPATH, "textures/gui/storageGui.png"));

		int j = (this.width - this.xSize) / 2;
		int k = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(j + 13, k, 0, 0, this.xSize - 16, this.ySize);
		this.displayGauge(j, k, 12, 43, (int) ((((double) fluidStored / (double) getScaledLiquidAmount()) * 69) + 0.5), new FluidStack(FluidRegistry.getFluid(fluidID == 0 ? 1 : fluidID), 1));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		this.fontRendererObj.drawString(formattedItemAmount, (int) (this.xSize / 1.44) - ((formattedItemAmount.length() - 8) * 4), 54, 0x000000);
		this.fontRendererObj.drawString(formattedFluidAmount, this.xSize / 5 - ((formattedFluidAmount.length() - 8) * 4), 80, 0x000000);
		this.fontRendererObj.drawString("Current", this.xSize / 3, 30, 0x000000);
		this.fontRendererObj.drawString("Scale Max: ", (int) (this.xSize / 3.2), 40, 0x000000);
		this.fontRendererObj.drawString(getScaledLiquidMaxAsString(), ((int) (this.xSize / 2.5)) - Math.round(getScaledLiquidMaxAsString().length() * 2.5f), 50, 0x000000);
		this.fontRendererObj.drawString("IDQSU", (int) (this.xSize / 2.2), 7, 0x000000);
	}

	/**
	 * @author Buildcraft team
	 */
	private void displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid)
	{
		if (liquid == null) { return; }
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

				drawTexturedModelRectFromIcon(j + col + 1, k + line + 63 - x - start, liquidIcon, 23, 16 - (16 - x));
				start = start + 16;

			}
			while (x != 0 && squaled != 0);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		drawTexturedModalRect(j + col, k + line, 234, 0, 16, 60);
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

		return Utils.formatString("", l, true, l == max);
	}
}
