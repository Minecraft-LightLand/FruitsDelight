package dev.xkmc.fruitsdelight.init.plants;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface FruitPlant<T extends Enum<T> & FruitPlant<T>> extends PlantDataEntry<T> {

	Item getFruit();

	default TagKey<Item> getFruitTag() {
		return ItemTags.create(new ResourceLocation("forge", "fruits/" + getName()));
	}

}
