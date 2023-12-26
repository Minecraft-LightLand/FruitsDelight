package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FoodType;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.fruitsdelight.init.food.RecordFood;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class FDItems {

	private static final Set<String> SMALL_WORDS = Set.of("of", "the", "with");

	public static ItemEntry<Item>[] JELLY, JELLO;

	static {
		int fruits = FruitType.values().length;
		JELLY = new ItemEntry[fruits];
		JELLO = new ItemEntry[fruits];
		for (int j = 0; j < fruits; j++) {
			FruitType fruit = FruitType.values()[j];
			String name = fruit.name().toLowerCase(Locale.ROOT);
			FoodType food = FoodType.JELLY;
			JELLY[j] = FruitsDelight.REGISTRATE.item(name + "_jelly", p -> food.build(p, new RecordFood(fruit, food)))
					.transform(b -> food.model(b, 0, fruit)).lang(FDItems.toEnglishName(name) + " Jam").tag(food.tags)
					.register();
		}
		for (int j = 0; j < fruits; j++) {
			FruitType fruit = FruitType.values()[j];
			String name = fruit.name().toLowerCase(Locale.ROOT);
			FoodType food = FoodType.JELLO;
			JELLO[j] = FruitsDelight.REGISTRATE.item(name + "_jello", p -> food.build(p, new RecordFood(fruit, food)))
					.transform(b -> food.model(b, 0, fruit)).lang(FDItems.toEnglishName(name) + " Jello").tag(food.tags)
					.register();
		}
	}

	public static String toEnglishName(String internalName) {
		return Arrays.stream(internalName.split("_"))
				.map(e -> SMALL_WORDS.contains(e) ? e : StringUtils.capitalize(e))
				.collect(Collectors.joining(" "));
	}

	public static void register() {
	}

}
