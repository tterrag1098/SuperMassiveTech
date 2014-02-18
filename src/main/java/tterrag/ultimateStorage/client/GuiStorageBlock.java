package tterrag.ultimateStorage.client;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import tterrag.ultimateStorage.container.ContainerStorageBlock;
import tterrag.ultimateStorage.tile.TileStorageBlock;

public class GuiStorageBlock extends GuiContainer
{
	private long amountStored;
	private String formattedAmount = "Testing";
	
	public GuiStorageBlock(InventoryPlayer par1InventoryPlayer, TileStorageBlock tile)
	{
		super(new ContainerStorageBlock(par1InventoryPlayer, tile));
		this.xSize = 250;
		this.ySize = 202;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		formattedAmount = "Stored: ";
		if (amountStored < 1000000)
			formattedAmount += amountStored;
		else if (amountStored >= 1000000)
			formatString();
		
		this.mc.getTextureManager().bindTexture(new ResourceLocation("ultimatestorage", "textures/gui/storageGui.png"));
	
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}
	
	private void formatString()
	{
		switch (Long.toString(amountStored).length())
		{
		case 7:
			formattedAmount += Long.toString(amountStored).substring(0, 1) + "." + Long.toString(amountStored).substring(1, 4) + "M";
			return;
		case 8:
			formattedAmount += Long.toString(amountStored).substring(0, 2) + "." + Long.toString(amountStored).substring(2, 5) + "M";
			return;
		case 9:
			formattedAmount += Long.toString(amountStored).substring(0, 3) + "." + Long.toString(amountStored).substring(3, 6) + "M";
			return;
		case 10:
			formattedAmount += Long.toString(amountStored).substring(0, 1) + "." + Long.toString(amountStored).substring(1, 4) + "B";
			return;
		case 11:
			formattedAmount += Long.toString(amountStored).substring(0, 2) + "." + Long.toString(amountStored).substring(2, 5) + "B";
			return;
		case 12:
			formattedAmount += Long.toString(amountStored).substring(0, 3) + "." + Long.toString(amountStored).substring(3, 6) + "B";
			return;
		case 13:
			formattedAmount += Long.toString(amountStored).substring(0, 1) + "." + Long.toString(amountStored).substring(1, 4) + "T";
			return;
		case 14:
			formattedAmount += Long.toString(amountStored).substring(0, 2) + "." + Long.toString(amountStored).substring(2, 5) + "T";
			return;
		case 15:
			formattedAmount += Long.toString(amountStored).substring(0, 3) + "." + Long.toString(amountStored).substring(3, 6) + "T";
		default:
			return;
		}
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
	{
		this.fontRendererObj.drawString(formattedAmount, (int) (this.xSize / 1.7), 50, 0x000000);
	}
}
