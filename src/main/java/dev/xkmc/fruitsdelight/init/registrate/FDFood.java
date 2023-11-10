package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum FDFood {
	APPLE_JELLY(FruitType.APPLE, FoodType.JELLY),
	BLUEBERRY_CUSTARD(FruitType.BLUEBERRY, FoodType.CREAM),
	BLUEBERRY_JELLY(FruitType.BLUEBERRY, FoodType.JELLY),
	BLUEBERRY_MUFFIN(FruitType.BLUEBERRY, FoodType.SWEET),
	GLOWBERRY_JELLY(FruitType.GLOWBERRY, FoodType.JELLY),
	HAMIMELON_JELLY(FruitType.HAMIMELON, FoodType.JELLY),
	HAMIMELON_JUICE(FruitType.HAMIMELON, FoodType.JUICE),
	HAMIMELON_POPSICLE(FruitType.HAMIMELON, FoodType.STICK),
	HAWBERRY_JELLY(FruitType.HAWBERRY, FoodType.JELLY),
	HAWBERRY_ROLL(FruitType.HAWBERRY, FoodType.SLICE),
	HAWBERRY_SHEET(FruitType.HAWBERRY, FoodType.SLICE),
	HAWBERRY_STICK(FruitType.HAWBERRY, FoodType.STICK),
	HAWBERRY_TEA(FruitType.HAWBERRY, FoodType.JUICE),
	LYCHEE_JELLY(FruitType.LYCHEE, FoodType.JELLY),
	MANGO_JELLY(FruitType.MANGO, FoodType.JELLY),
	ORANGE_CHICKEN(FruitType.ORANGE, FoodType.MEAL),
	ORANGE_JELLY(FruitType.ORANGE, FoodType.JELLY),
	ORANGE_JUICE(FruitType.ORANGE, FoodType.JUICE),
	ORANGE_MARINATED_PORK(FruitType.ORANGE, FoodType.MEAL),
	ORANGE_SLICE(FruitType.ORANGE, FoodType.SLICE),
	PEACH_JELLY(FruitType.PEACH, FoodType.JELLY),
	PEAR_JELLY(FruitType.PEAR, FoodType.JELLY),
	PEAR_JUICE(FruitType.PEAR, FoodType.JUICE),
	BAKED_PEAR(FruitType.PEAR, FoodType.FRUIT),
	PERSIMMON_JELLY(FruitType.PERSIMMON, FoodType.JELLY),
	//PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, food),
	BOWL_OF_PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, FoodType.MEAL),
	PINEAPPLE_MARINATED_PORK(FruitType.PINEAPPLE, FoodType.MEAL),
	PINEAPPLE_PIE(FruitType.PINEAPPLE, FoodType.SWEET),
	SWEETBERRY_JELLY(FruitType.SWEETBERRY, FoodType.JELLY);

	private final String name;

	public final ItemEntry<Item> item;

	FDFood(FruitType fruit, FoodType food) {
		this.name = name().toLowerCase(Locale.ROOT);
		this.item = FruitsDelight.REGISTRATE.item(name, p -> food.build(p, fruit))
				.defaultModel().lang(FDItems.toEnglishName(name))
				.register();
	}

	public static void register() {

	}

}
