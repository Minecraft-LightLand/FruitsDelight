package dev.xkmc.fruitsdelight.init.food;

public record RecordFood(FruitType fruit, FoodType type) implements IFDFood {

	@Override
	public FoodType getType() {
		return type;
	}

	@Override
	public EffectEntry[] getEffects() {
		return new EffectEntry[0];
	}
}
