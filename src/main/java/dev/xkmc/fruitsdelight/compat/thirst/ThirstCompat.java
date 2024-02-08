package dev.xkmc.fruitsdelight.compat.thirst;

import dev.ghen.thirst.api.ThirstHelper;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FDJuice;
import dev.xkmc.fruitsdelight.init.food.FruitType;

public class ThirstCompat {

	public static void init() {
		for (var e : FDJuice.values()) {
			ThirstHelper.addDrink(e.get(), 8, 13);
		}

		ThirstHelper.addFood(FruitType.ORANGE.fruit.get(), 4, 6);
		ThirstHelper.addFood(FruitType.LYCHEE.fruit.get(), 4, 6);
		ThirstHelper.addFood(FruitType.PINEAPPLE.fruit.get(), 4, 6);

		ThirstHelper.addFood(FruitType.KIWI.fruit.get(), 6, 10);
		ThirstHelper.addFood(FruitType.PEACH.fruit.get(), 6, 10);
		ThirstHelper.addFood(FruitType.HAMIMELON.fruit.get(), 6, 10);
		ThirstHelper.addFood(FruitType.MANGO.fruit.get(), 6, 10);

		ThirstHelper.addFood(FruitType.PEAR.fruit.get(), 10, 16);
		ThirstHelper.addFood(FDFood.BAKED_PEAR.get(), 10, 16);
		ThirstHelper.addFood(FDFood.HAMIMELON_POPSICLE.get(), 10, 16);
		ThirstHelper.addFood(FDFood.HAMIMELON_SHAVED_ICE.get(), 10, 16);
		ThirstHelper.addFood(FDFood.KIWI_POPSICLE.get(), 10, 16);
		ThirstHelper.addFood(FDFood.FIG_CHICKEN_STEW.get(), 10, 16);
		ThirstHelper.addFood(FDFood.PEAR_WITH_ROCK_SUGAR.get(), 14, 20);
	}

}
