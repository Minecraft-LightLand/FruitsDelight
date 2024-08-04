package dev.xkmc.fruitsdelight.init.food;

public interface IFDFood {
	
	FruitType fruit();

	FoodType getType();

	EffectEntry[] getEffects();
}
