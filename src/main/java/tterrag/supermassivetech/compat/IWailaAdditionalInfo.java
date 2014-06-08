package tterrag.supermassivetech.compat;

import java.util.List;

import net.minecraft.world.World;

public interface IWailaAdditionalInfo
{
    public void getWailaInfo(List<String> tooltip, int x, int y, int z, World world);
}
