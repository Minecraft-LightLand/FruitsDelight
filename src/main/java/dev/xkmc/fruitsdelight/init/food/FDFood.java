package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.world.item.Item;

import java.util.Locale;

public enum FDFood {
	APPLE_JELLY(FruitType.APPLE, FoodType.JELLY),
	BLUEBERRY_JELLY(FruitType.BLUEBERRY, FoodType.JELLY),
	GLOWBERRY_JELLY(FruitType.GLOWBERRY, FoodType.JELLY),
	HAMIMELON_JELLY(FruitType.HAMIMELON, FoodType.JELLY),
	HAWBERRY_JELLY(FruitType.HAWBERRY, FoodType.JELLY),
	LYCHEE_JELLY(FruitType.LYCHEE, FoodType.JELLY),
	MANGO_JELLY(FruitType.MANGO, FoodType.JELLY),
	ORANGE_JELLY(FruitType.ORANGE, FoodType.JELLY),
	PEACH_JELLY(FruitType.PEACH, FoodType.JELLY),
	PEAR_JELLY(FruitType.PEAR, FoodType.JELLY),
	PERSIMMON_JELLY(FruitType.PERSIMMON, FoodType.JELLY),
	PINEAPPLE_JELLY(FruitType.PINEAPPLE, FoodType.JELLY),
	SWEETBERRY_JELLY(FruitType.SWEETBERRY, FoodType.JELLY),

	HAMIMELON_JUICE(FruitType.HAMIMELON, FoodType.JUICE),
	HAWBERRY_TEA(FruitType.HAWBERRY, FoodType.JUICE),
	ORANGE_JUICE(FruitType.ORANGE, FoodType.JUICE),
	PEAR_JUICE(FruitType.PEAR, FoodType.JUICE),

	BLUEBERRY_CUSTARD(FruitType.BLUEBERRY, FoodType.CREAM),
	BLUEBERRY_MUFFIN(FruitType.BLUEBERRY, FoodType.SWEET),
	HAMIMELON_POPSICLE(FruitType.HAMIMELON, FoodType.STICK),
	HAWBERRY_ROLL(FruitType.HAWBERRY, FoodType.ROLL),
	HAWBERRY_SHEET(FruitType.HAWBERRY, FoodType.ROLL),
	HAWBERRY_STICK(FruitType.HAWBERRY, FoodType.STICK),
	ORANGE_CHICKEN(FruitType.ORANGE, FoodType.MEAL),
	ORANGE_MARINATED_PORK(FruitType.ORANGE, FoodType.MEAL),
	ORANGE_SLICE(FruitType.ORANGE, FoodType.SLICE),
	BAKED_PEAR(FruitType.PEAR, FoodType.FRUIT),
	//PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, food),
	BOWL_OF_PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, FoodType.MEAL),
	PINEAPPLE_MARINATED_PORK(FruitType.PINEAPPLE, FoodType.MEAL),
	PINEAPPLE_PIE(FruitType.PINEAPPLE, FoodType.SWEET),
	;

	private final String name;
	public final FruitType fruit;
	public final ItemEntry<Item> item;

	FDFood(FruitType fruit, FoodType food) {
		this.fruit = fruit;
		this.name = name().toLowerCase(Locale.ROOT);
		this.item = FruitsDelight.REGISTRATE.item(name, p -> food.build(p, fruit))
				.defaultModel().lang(FDItems.toEnglishName(name))
				.register();
	}

	public static void register() {

	}

	public Item getFruit() {
		return fruit.fruit.get();
	}
}
