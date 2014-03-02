package tterrag.ultimateStorage.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import tterrag.ultimateStorage.container.ContainerStorageBlock;
import tterrag.ultimateStorage.tile.TileStorageBlock;
import tterrag.ultimateStorage.tile.UltimateFluidTank;

public class GuiStorageBlock extends GuiContainer
{
	public long itemsStored;
	public long fluidStored;
	private String formattedItemAmount = "";
	private String formattedFluidAmount = "";
	private long max = 50000; //TileStorageBlock.max;
	
	public int fluidID;
	
	private static final ResourceLocation TEXTURE = new ResourceLocation("ultimatestorage", "textures/gui/storageGui.png");
	private static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
	
	private int k, l;
	
	public GuiStorageBlock(InventoryPlayer par1InventoryPlayer, TileStorageBlock tile)
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
			formattedItemAmount = formatString(formattedItemAmount, itemsStored, false);
		
		formattedFluidAmount = "Stored: ";
		if (fluidStored < 1000000)
			formattedFluidAmount += fluidStored + "mb";
		else if (fluidStored >= 1000000)
			formattedFluidAmount = formatString(formattedFluidAmount, fluidStored, true);
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation("ultimatestorage", "textures/gui/storageGui.png"));
	
		k = (this.width - this.xSize) / 2;
		l = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(k + 13, l, 0, 0, this.xSize - 16, this.ySize);
	}
	
	private String formatString(String s, long amnt, boolean isFluid)
	{
		switch (Long.toString(amnt).length())
		{
		case 7:
			s += Long.toString(amnt).substring(0, 1) + "." + Long.toString(amnt).substring(1, 3) + (isFluid ? "kB" : "M");
			return s;
		case 8:
			s += Long.toString(amnt).substring(0, 2) + "." + Long.toString(amnt).substring(2, 4) + (isFluid ? "kB" : "M");
			return s;
		case 9:
			s += Long.toString(amnt).substring(0, 3) + "." + Long.toString(amnt).substring(3, 5) + (isFluid ? "kB" : "M");
			return s;
		case 10:
			s += Long.toString(amnt).substring(0, 1) + "." + Long.toString(amnt).substring(1, 3) + (isFluid ? "MB" : "B");
			return s;
		case 11:
			s += Long.toString(amnt).substring(0, 2) + "." + Long.toString(amnt).substring(2, 4) + (isFluid ? "MB" : "B");
			return s;
		case 12:
			s += Long.toString(amnt).substring(0, 3) + "." + Long.toString(amnt).substring(3, 5) + (isFluid ? "MB" : "B");
			return s;
		case 13:
			s += Long.toString(amnt).substring(0, 1) + "." + Long.toString(amnt).substring(1, 3) + (isFluid ? "GB" : "T");
			return s;
		default:
			s += "NaN";
			return s;
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		
		this.fontRendererObj.drawString(formattedItemAmount, (int) (this.xSize / 1.44) - ((formattedItemAmount.length() - 8) * 4), 54, 0x000000);
		this.fontRendererObj.drawString(formattedFluidAmount, (int) (this.xSize / 5) - ((formattedFluidAmount.length() - 8) * 4), 80, 0x000000);
		this.fontRendererObj.drawString("IDQSU", (int) (this.xSize / 2.2), 7, 0x000000);
		this.displayGauge(j, k, -7, -45, (int) ((((float) fluidStored / (float) max) * 69) + 0.5), new FluidStack(FluidRegistry.getFluid(fluidID == 0 ? 1 : fluidID), 1));
		System.out.println((((float) fluidStored / (float) max) * 69) + 0.5);
	}
	
	/**
	 * @author Buildcraft team
	 */
	private void displayGauge(int j, int k, int line, int col, int squaled, FluidStack liquid) {
		if (liquid == null) {
			return;
		}
		int start = 0;

		IIcon liquidIcon = null;
		Fluid fluid = liquid.getFluid();
		if (fluid != null && fluid.getStillIcon() != null) {
			liquidIcon = fluid.getStillIcon();
		}
		mc.renderEngine.bindTexture(BLOCK_TEXTURE);
		setGLColorFromInt(0xFFFFFF);

		int x;

		if (liquidIcon != null) {
			do{
				if (squaled > 16) {
					x = 16;
					squaled -= 16;
				} else {
					x = squaled;
					squaled = 0;
				}

				drawTexturedModelRectFromIcon(j + col + 1, k + line + 63 - x - start, liquidIcon, 23, 16 - (16 - x));
				start = start + 16;

			} while (x != 0 && squaled != 0);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		drawTexturedModalRect(j + col, k + line, 234, 0, 16, 60);
	}
	
	/**
	 * @author Buildcraft team
	 */
	public static void setGLColorFromInt(int color) {
		float red = (float) (color >> 16 & 255) / 255.0F;
		float green = (float) (color >> 8 & 255) / 255.0F;
		float blue = (float) (color & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, 1.0F);
	}
}
