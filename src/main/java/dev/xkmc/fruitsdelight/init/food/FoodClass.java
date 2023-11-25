package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.BiFunction;

public enum FoodClass {
	NONE(FDFoodItem::new),
	STICK((p, e) -> new FDFoodItem(p.craftRemainder(Items.STICK), e)),
	GLASS((p, e) -> new FDFoodItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), e)),
	BOWL((p, e) -> new FDFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16), e)),
	;

	public final BiFunction<Item.Properties, FDFood, FDFoodItem> factory;

	FoodClass(BiFunction<Item.Properties, FDFood, FDFoodItem> factory) {
		this.factory = factory;
	}
}
