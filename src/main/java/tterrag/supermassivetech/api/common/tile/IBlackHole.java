package tterrag.supermassivetech.api.common.tile;

import net.minecraft.entity.Entity;

public interface IBlackHole
{
    public long getEnergy();
    
    public void setEnergy(long newEnergy);
    
    public void incrementEnergy(Entity collided);
    
    public float getSize();
}
