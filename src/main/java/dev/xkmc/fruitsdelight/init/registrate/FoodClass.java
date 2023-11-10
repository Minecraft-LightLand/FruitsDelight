package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Function;

public enum FoodClass {
	NONE(FDFoodItem::new),
	STICK(p -> new FDFoodItem(p.craftRemainder(Items.STICK))),
	GLASS(p -> new FDFoodItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16))),
	BOWL(p -> new FDFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16))),
	;

	public final Function<Item.Properties, Item> factory;

	FoodClass(Function<Item.Properties, Item> factory) {
		this.factory = factory;
	}
}
