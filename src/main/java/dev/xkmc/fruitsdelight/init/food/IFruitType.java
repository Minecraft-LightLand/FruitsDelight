package dev.xkmc.fruitsdelight.init.food;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public interface IFruitType {

	int color();

	Ingredient getFruitTag();

	Item getFruit();

	Item getJam();

	Item getJello();

	List<EffectFunc> getFruitEffects();
}
