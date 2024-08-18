package dev.xkmc.fruitsdelight.init.plants;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface FruitPlant<T extends Enum<T> & FruitPlant<T>> extends PlantDataEntry<T> {

	Item getFruit();

	default TagKey<Item> getFruitTag() {
		return PlantDataEntry.getFruitTag(getName());
	}

}
