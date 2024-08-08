package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.DurianFleshItem;
import dev.xkmc.fruitsdelight.content.item.FDBlockItem;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public enum FoodClass implements IFoodBuilder {
	NONE((b, p, e) -> new FDFoodItem(p, e), null),
	STICK((b, p, e) -> new FDFoodItem(p, e), () -> Items.STICK),
	GLASS((b, p, e) -> new FDFoodItem(p.stacksTo(16), e, UseAnim.DRINK), () -> Items.GLASS_BOTTLE),
	JELLY((b, p, e) -> new FDBlockItem(b, p.stacksTo(16), e, UseAnim.DRINK), () -> Items.GLASS_BOTTLE),
	BOWL((b, p, e) -> new FDFoodItem(p.stacksTo(16), e), () -> Items.BOWL),
	DURIAN_FLESH((b, p, e) -> new DurianFleshItem(p, e), null),
	PLATE((b, p, e) -> new FDBlockItem(b, p.stacksTo(16), e), () -> Items.BOWL),
	;

	public final IFoodFactory factory;
	private final Supplier<Item> container;

	FoodClass(IFoodFactory factory, @Nullable Supplier<Item> container) {
		this.factory = factory;
		this.container = container;
	}

	public Item build(@Nullable Block block, Item.Properties food, IFDFood type, FoodProperties.Builder builder) {
		if (container != null) {
			var item = container.get();
			builder = builder.usingConvertsTo(item);
			food.craftRemainder(item);
		}
		food.food(builder.build());
		return factory.build(block, food, type);

	}

}
