package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.content.item.FDFoodItem;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.data.TagGen;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import dev.xkmc.l2library.repack.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.ArrayList;
import java.util.Arrays;
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
	LEMON_JUICE(FruitType.LEMON, FoodType.JUICE),
	PEAR_JUICE(FruitType.PEAR, FoodType.JUICE),
	MANGO_TEA(FruitType.MANGO, FoodType.JUICE),
	PEACH_TEA(FruitType.PEACH, FoodType.JUICE),
	LYCHEE_CHERRY_TEA(FruitType.LYCHEE, FoodType.JUICE),
	BELLINI_COCKTAIL(FruitType.SWEETBERRY, FoodType.JUICE,
			new EffectEntry(FDEffects.HEAL_AURA::get, 200),
			new EffectEntry(() -> MobEffects.CONFUSION, 100)),

	BLUEBERRY_CUSTARD(FruitType.BLUEBERRY, FoodType.CREAM),
	BLUEBERRY_MUFFIN(FruitType.BLUEBERRY, FoodType.DESSERT),
	HAMIMELON_POPSICLE(FruitType.HAMIMELON, FoodType.STICK),
	HAMIMELON_SHAVED_ICE(FruitType.HAMIMELON, FoodType.JUICE),
	HAWBERRY_ROLL(FruitType.HAWBERRY, FoodType.ROLL, 0),
	HAWBERRY_SHEET(FruitType.HAWBERRY, FoodType.SHEET),
	HAWBERRY_STICK(FruitType.HAWBERRY, FoodType.STICK),
	ORANGE_SLICE(FruitType.ORANGE, FoodType.SLICE),
	LEMON_SLICE(FruitType.LEMON, FoodType.SLICE),
	BAKED_PEAR(FruitType.PEAR, FoodType.FRUIT),
	PINEAPPLE_PIE(FruitType.PINEAPPLE, FoodType.DESSERT),
	LEMON_TART(FruitType.LEMON, FoodType.DESSERT),
	MANGO_MILKSHAKE(FruitType.MANGO, FoodType.JUICE),
	MANGO_SALAD(FruitType.MANGO, FoodType.BOWL, 0),
	DRIED_PERSIMMON(FruitType.PERSIMMON, FoodType.FRUIT),
	PERSIMMON_COOKIE(FruitType.PERSIMMON, FoodType.COOKIE),
	LEMON_COOKIE(FruitType.LEMON, FoodType.COOKIE),
	PEAR_WITH_ROCK_SUGAR(FruitType.PEAR, FoodType.BOWL, new EffectEntry(ModEffects.COMFORT, 1200)),
	ORANGE_CHICKEN(FruitType.ORANGE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	ORANGE_MARINATED_PORK(FruitType.ORANGE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	BOWL_OF_PINEAPPLE_FRIED_RICE(FruitType.PINEAPPLE, FoodType.STAPLE, new EffectEntry(ModEffects.COMFORT, 6000)),
	PINEAPPLE_MARINATED_PORK(FruitType.PINEAPPLE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	LYCHEE_CHICKEN(FruitType.LYCHEE, FoodType.MEAL, new EffectEntry(ModEffects.NOURISHMENT, 3600)),
	JELLY_BREAD(FruitType.SWEETBERRY, FoodType.SWEET, 1),
	;

	private final String name;

	public final FruitType fruit;
	public final FoodType type;
	public final ItemEntry<FDFoodItem> item;
	public final EffectEntry[] effs;
	public final int overlay;

	FDFood(boolean allowJelly, int overlay, FruitType fruit, FoodType food, EffectEntry... effs) {
		this.fruit = fruit;
		this.type = food;
		this.overlay = overlay;
		this.name = name().toLowerCase(Locale.ROOT);
		this.item = FruitsDelight.REGISTRATE.item(name, p -> food.build(p, fruit, effs, this))
				.transform(b -> food.model(b, overlay, fruit)).lang(FDItems.toEnglishName(name)).tag(getTags(allowJelly, food.tags))
				.register();
		this.effs = effs;
	}

	FDFood(FruitType fruit, FoodType food, EffectEntry... effs) {
		this(false, 0, fruit, food, effs);
	}

	FDFood(FruitType fruit, FoodType food, int overlay) {
		this(true, overlay, fruit, food);
	}

	@SuppressWarnings({"unsafe", "unchecked"})
	private TagKey<Item>[] getTags(boolean allowJelly, TagKey<Item>[] tags) {
		var ans = new ArrayList<>(Arrays.stream(tags).toList());
		if (allowJelly) ans.add(TagGen.ALLOW_JELLY);
		return ans.toArray(TagKey[]::new);
	}

	public static void register() {

	}

	public Item getFruit() {
		return fruit.fruit.get();
	}
}
