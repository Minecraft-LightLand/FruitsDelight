package dev.xkmc.fruitsdelight.compat.thirst;

import dev.ghen.thirst.foundation.common.event.RegisterThirstValueEvent;
import dev.xkmc.fruitsdelight.init.food.FDFood;
import dev.xkmc.fruitsdelight.init.food.FDJuice;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ThirstCompat {

	public static void init() {
		NeoForge.EVENT_BUS.register(ThirstCompat.class);
	}

	@SubscribeEvent
	public static void compat(RegisterThirstValueEvent event) {
		for (var e : FDJuice.values()) {
			event.addDrink(e.get(), 8, 13);
		}

		event.addFood(FruitType.ORANGE.getFruit(), 4, 6);
		event.addFood(FruitType.LYCHEE.getFruit(), 4, 6);
		event.addFood(FruitType.PINEAPPLE.getFruit(), 4, 6);
		event.addFood(FruitType.KIWI.getFruit(), 4, 6);
		event.addFood(FruitType.PEACH.getFruit(), 4, 6);
		event.addFood(FruitType.HAMIMELON.getFruit(), 4, 6);
		event.addFood(FruitType.MANGO.getFruit(), 4, 6);
		event.addFood(FruitType.PEAR.getFruit(), 6, 10);
		event.addFood(FDFood.BAKED_PEAR.get(), 8, 13);
		event.addFood(FDFood.HAMIMELON_POPSICLE.get(), 8, 13);
		event.addFood(FDFood.KIWI_POPSICLE.get(), 8, 13);
		event.addFood(FDFood.HAMIMELON_SHAVED_ICE.get(), 10, 16);
		event.addFood(FDFood.FIG_CHICKEN_STEW.get(), 10, 16);
		event.addFood(FDFood.PEAR_WITH_ROCK_SUGAR.get(), 14, 20);
	}

}
