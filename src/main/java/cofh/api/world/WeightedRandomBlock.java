package cofh.api.world;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

/**
 * This class essentially allows for ores to be generated in clusters, with Features randomly choosing one or more blocks from a weighted list.
 * 
 * @author King Lemming
 * 
 */
public final class WeightedRandomBlock extends WeightedRandom {

	public final net.minecraft.item.Item block;
	public final int metadata;

	public WeightedRandomBlock(ItemStack ore) {

		super();
		this.block = ore.getItem();
		this.metadata = ore.getItemDamage();
	}

	public WeightedRandomBlock(ItemStack ore, int weight) {

		super();
		this.block = ore.getItem();
		this.metadata = ore.getItemDamage();
	}

}
