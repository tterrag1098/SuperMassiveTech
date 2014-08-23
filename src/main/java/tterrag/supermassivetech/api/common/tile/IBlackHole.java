package tterrag.supermassivetech.api.common.tile;

import net.minecraft.entity.Entity;

/**
 * Interface to interact with black hole (tile)entities
 * 
 * @author Garrett Spicer-Davis
 * 
 */
public interface IBlackHole
{
    /**
     * @return the internal "energy" value of this black hole
     */
    public long getEnergy();

    /**
     * @param newEnergy - The energy level at which to set the black hole
     */
    public void setEnergy(long newEnergy);

    /**
     * Increment the energy inside this black hole dependant on the entity that has collided with it.
     * 
     * @param collided - The entity that has collided with this black hole and needs to be ingested
     */
    public void incrementEnergy(Entity collided);

    /**
     * @return The size at which to render this black hole
     */
    public float getSize();
}
