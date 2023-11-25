package dev.xkmc.fruitsdelight.init.food;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Locale;

public enum FDFood {
	APPLE_JELLY(FruitType.APPLE, FoodType.JELLY),
	BLUEBERRY_JELLY(FruitType.BLUEBERRY, FoodType.JELLY),
	GLOWBERRY_JELLY(FruitType.GLOWBERRY, FoodType.JELLY),
	HAMIMELON_JELLY(FruitType.HAMIMELON, FoodType.JELLY),
	MELON_JELLY(FruitType.MELON, FoodType.JELLY),
	HAWBERRY_JELLY(FruitType.HAWBERRY, FoodType.JELLY),
	LYCHEE_JELLY(FruitType.LYCHEE, FoodType.JELLY),
	MANGO_JELLY(FruitType.MANGO, FoodType.JELLY),
	ORANGE_JELLY(FruitType.ORANGE, FoodType.JELLY),
	PEACH_JELLY(FruitType.PEACH, FoodType.JELLY),
	PEAR_JELLY(FruitType.PEAR, FoodType.JELLY),
	PERSIMMON_JELLY(FruitType.PERSIMMON, FoodType.JELLY),
	PINEAPPLE_JELLY(FruitType.PINEAPPLE, FoodType.JELLY),
	SWEETBERRY_JELLY(FruitType.SWEETBERRY, FoodType.JELLY),
	LEMON_JELLY(FruitType.LEMON, FoodType.JELLY),

	HAMIMELON_JUICE(FruitType.HAMIMELON, FoodType.JUICE),
	HAWBERRY_TEA(FruitType.HAWBERRY, FoodType.JUICE),
	ORANGE_JUICE(FruitType.ORANGE, FoodType.JUICE),
	PEAR_JUICE(FruitType.PEAR, FoodType.JUICE),
	MANGO_TEA(FruitType.MANGO, FoodType.JUICE),
	PEACH_TEA(FruitType.PEACH, FoodType.JUICE),
	LYCHEE_CHERRY_TEA(FruitType.LYCHEE, FoodType.JUICE),
	BELLINI_COCKTAIL(FruitType.PEACH, FoodType.JUICE),

	BLUEBERRY_CUSTARD(FruitType.BLUEBERRY, FoodType.CREAM),
	BLUEBERRY_MUFFIN(FruitType.BLUEBERRY, FoodType.SWEET),
	HAMIMELON_POPSICLE(FruitType.HAMIMELON, FoodType.STICK),
	HAMIMELON_SHAVED_ICE(FruitType.HAMIMELON, FoodType.JUICE),
	HAWBERRY_ROLL(FruitType.HAWBERRY, FoodType.ROLL),
	HAWBERRY_SHEET(FruitType.HAWBERRY, FoodType.ROLL),
	HAWBERRY_STICK(FruitType.HAWBERRY, FoodType.STICK),
	ORANGE_SLICE(FruitType.ORANGE, FoodType.SLICE),
	LEMON_SLICE(FruitType.LEMON, FoodType.SLICE),
	BAKED_PEAR(FruitType.PEAR, FoodType.FRUIT),
	PINEAPPLE_PIE(FruitType.PINEAPPLE, FoodType.SWEET),
	MANGO_MILKSHAKE(FruitType.MANGO, FoodType.JUICE),
	MANGO_SALAD(FruitType.MANGO, FoodType.BOWL),
	DRIED_PERSIMMON(FruitType.PERSIMMON, FoodType.FRUIT),
	PERSIMMON_COOKIE(FruitType.PERSIMMON, FoodType.ROLL),
	PEAR_WITH_ROCK_SUGAR(FruitType.PEAR, FoodType.BOWL, new EffectEntry(ModEffects.COMFORT, 1200)),
	ORANGE_CHICKEN(FruitType.ORANGE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	ORANGE_MARINATED_PORK(FruitType.ORANGE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	BOWL_OF_PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, FoodType.MEAL, new EffectEntry(ModEffects.COMFORT, 6000)),
	PINEAPPLE_MARINATED_PORK(FruitType.PINEAPPLE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	LYCHEE_CHICKEN(FruitType.LYCHEE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	;

	private final String name;
	public final FruitType fruit;
	public final FoodType type;
	public final ItemEntry<Item> item;
	public final EffectEntry[] effs;

	FDFood(FruitType fruit, FoodType food, EffectEntry... effs) {
		this.fruit = fruit;
		this.type = food;
		this.name = name().toLowerCase(Locale.ROOT);
		this.item = FruitsDelight.REGISTRATE.item(name, p -> food.build(p, fruit, effs, this))
				.transform(b -> food.model(b, fruit)).lang(FDItems.toEnglishName(name)).tag(food.tags)
				.register();
		this.effs = effs;
	}

	public static void register() {

	}

	public Item getFruit() {
		return fruit.fruit.get();
	}
}
