package tterrag.supermassivetech.api.common.registry;

import net.minecraft.util.EnumChatFormatting;
import tterrag.supermassivetech.common.registry.Stars.StarTier;
import cofh.api.energy.IEnergyContainerItem;

public interface IStar extends IEnergyContainerItem
{
    /**
     * Returns the unique ID of this star, use the static method in Stars.java!
     */
    public int getID();

    /**
     * The color of the star icon, a hex value (0xRRGGBB)
     */
    public int getColor();

    /**
     * Color of the name in tooltips
     */
    public EnumChatFormatting getTextColor();

    /**
     * The max power this star can provide in the harvester
     */
    public int getPowerPerTick();

    /**
     * The amount of time the fuse lasts on this star
     */
    public int getFuse();

    /**
     * Returns the basic (NBT identifier) name of the star
     */
    public String getName();

    /**
     * <code>toString()</code> here returns the proper name of the star, to be
     * used in tooltips
     */
    @Override
    public String toString();

    /**
     * The tier of this star, a {@link StarTier} type
     */
    public StarTier getTier();

    /**
     * The level (> 0 and usually < 5) of gravity potion effects to apply to the
     * player when this tier of star is held
     * 
     * @return
     */
    public int getMassLevel();

}
