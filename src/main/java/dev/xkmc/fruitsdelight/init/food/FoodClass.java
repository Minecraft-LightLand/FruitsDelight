package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;

import java.util.function.BiFunction;

public enum FoodClass implements IFoodClass{
	NONE(FDFoodItem::new),
	STICK((p, e) -> new FDFoodItem(p.craftRemainder(Items.STICK), e)),
	GLASS((p, e) -> new FDFoodItem(p.craftRemainder(Items.GLASS_BOTTLE).stacksTo(16), e, UseAnim.DRINK)),
	BOWL((p, e) -> new FDFoodItem(p.craftRemainder(Items.BOWL).stacksTo(16), e)),
	;

	public final BiFunction<Item.Properties, IFDFood, FDFoodItem> factory;

	FoodClass(BiFunction<Item.Properties, IFDFood, FDFoodItem> factory) {
		this.factory = factory;
	}

	public Item build(Item.Properties food, IFDFood type) {
		return factory.apply(food, type);
	}

}
