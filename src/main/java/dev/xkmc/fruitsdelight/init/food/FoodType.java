package dev.xkmc.fruitsdelight.init.food;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public enum FoodType {
	JUICE(FoodClass.GLASS, 1, 0.2f, 10, false),
	JELLY(FoodClass.GLASS, 2, 0.2f, 20, false),
	FRUIT(FoodClass.NONE, 4, 0.3f, 0, false),
	SLICE(FoodClass.NONE, 2, 0.3f, 0, true),
	ROLL(FoodClass.NONE, 2, 0.3f, 10, true),
	STICK(FoodClass.STICK, 4, 0.4f, 10, true),
	SWEET(FoodClass.NONE, 6, 0.5f, 20, false),
	CREAM(FoodClass.GLASS, 6, 0.5f, 20, false),
	BOWL(FoodClass.BOWL, 6, 0.5f, 30, false),
	MEAL(FoodClass.BOWL, 10, 0.8f, 40, false),
	;

	private final FoodClass cls;
	private final int food;
	private final float sat;
	private final int effectLevel;
	private final boolean fast;

	FoodType(FoodClass cls, int food, float sat, int effectLevel, boolean fast) {
		this.cls = cls;
		this.food = food;
		this.sat = sat;
		this.effectLevel = effectLevel;
		this.fast = fast;
	}

	public Item build(Item.Properties p, FruitType fruit) {
		var val = new FoodProperties.Builder();
		val.nutrition(food).saturationMod(sat);
		if (fast) val.fast();
		if (effectLevel > 0)
			for (var e : fruit.eff) {
				val.effect(() -> e.getEffect(effectLevel), e.getChance(effectLevel));
			}
		return cls.factory.apply(p.food(val.build()));
	}
}
