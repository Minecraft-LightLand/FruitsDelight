package dev.xkmc.fruitsdelight.init.registrate;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public enum FoodType {
	JUICE(FoodClass.GLASS, 1, 0.2f, false),
	JELLY(FoodClass.GLASS, 2, 0.2f, false),
	FRUIT(FoodClass.NONE, 4, 0.3f, false),
	SLICE(FoodClass.NONE, 2, 0.3f, true),
	STICK(FoodClass.STICK, 4, 0.4f, true),
	SWEET(FoodClass.NONE, 6, 0.5f, false),
	CREAM(FoodClass.GLASS, 6, 0.5f, false),
	MEAL(FoodClass.BOWL, 10, 0.8f, false),
	;

	private final FoodClass cls;
	private final int food;
	private final float sat;
	private final boolean fast;

	FoodType(FoodClass cls, int food, float sat, boolean fast) {
		this.cls = cls;
		this.food = food;
		this.sat = sat;
		this.fast = fast;
	}

	public Item build(Item.Properties p, FruitType fruit) {
		var val = new FoodProperties.Builder();
		val.nutrition(food).saturationMod(sat);
		if (fast) val.fast();
		return cls.factory.apply(p.food(val.build()));
	}
}
