package tterrag.ultimateStorage.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tterrag.ultimateStorage.container.ContainerStorageBlock;
import tterrag.ultimateStorage.tile.TileStorageBlock;

public class GuiStorageBlock extends GuiContainer
{
	public long itemsStored;
	public long fluidStored;
	private String formattedItemAmount = "";
	private String formattedFluidAmount = "";
	
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
	
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
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
		this.fontRendererObj.drawString(formattedItemAmount, (int) (this.xSize / 1.47) - ((formattedItemAmount.length() - 8) * 4), 54, 0x000000);
		this.fontRendererObj.drawString(formattedFluidAmount, (int) (this.xSize / 6) - ((formattedFluidAmount.length() - 8) * 4), 80, 0x000000);
		this.fontRendererObj.drawString("IDQSU", (int) (this.xSize / 2.2), 7, 0x000000);
	}
}
