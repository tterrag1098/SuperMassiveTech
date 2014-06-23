package tterrag.supermassivetech.tile;

import net.minecraft.tileentity.TileEntity;

public class TileBlackHole extends TileEntity
{
    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return pass > 0;
    }
}
