package tterrag.supermassivetech.api.common.tile;

import net.minecraft.entity.Entity;

public interface IBlackHole
{
    public int getEnergy();
    
    public void setEnergy(int newEnergy);
    
    public void incrementEnergy(Entity collided);
    
    public float getSize();
}
