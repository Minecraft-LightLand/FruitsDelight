package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Supplier;

public enum FruitType {
	APPLE(() -> Items.APPLE, List.of(new EffectFunc(() -> MobEffects.ABSORPTION, lv -> lv * 20))),
	BLUEBERRY(FDBushes.BLUEBERRY::getFruit, List.of(
			new EffectFunc(() -> MobEffects.NIGHT_VISION, lv -> lv * 20),
			new EffectFunc(FDEffects.BRIGHTENING::get, lv -> lv * 20)
	)),
	GLOWBERRY(() -> Items.GLOW_BERRIES, List.of(new EffectFunc(() -> MobEffects.GLOWING, lv -> lv * 80))),
	HAMIMELON(FDMelons.HAMIMELON::getSlice, List.of(new EffectFunc(() -> MobEffects.HEALTH_BOOST, lv -> lv * 20))),
	HAWBERRY(FDTrees.HAWBERRY::getFruit, List.of(new EffectFunc(FDEffects.APPETIZING::get, lv -> lv * 20))),
	LYCHEE(FDTrees.LYCHEE::getFruit, List.of(new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 10, lv -> 2))),
	MANGO(FDTrees.MANGO::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 20, lv -> 2),
			new EffectFunc(FDEffects.RAGE_AURA::get, lv -> lv * 20)
	)),
	ORANGE(FDTrees.ORANGE::getFruit, List.of(
			new EffectFunc(() -> MobEffects.REGENERATION, lv -> lv * 20),
			new EffectFunc(FDEffects.RECOVERING::get, lv -> lv * 20)
	)),
	PEACH(FDTrees.PEACH::getFruit, List.of(new EffectFunc(FDEffects.HEAL_AURA::get, lv -> lv * 10))),
	PEAR(FDTrees.PEAR::getFruit, List.of(new EffectFunc(FDEffects.LOZENGE::get, lv -> lv * 20))),
	PERSIMMON(FDTrees.PERSIMMON::getFruit, List.of(new EffectFunc(FDEffects.ASTRINGENT::get, lv -> lv * 80))),
	PINEAPPLE(FDPineapple.PINEAPPLE::getSlice, List.of(
			new EffectFunc(() -> MobEffects.MOVEMENT_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.SWEETENING::get, lv -> lv * 20)
	)),
	LEMON(FDBushes.LEMON::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DIG_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.REFRESHING::get, lv -> lv * 20)
	)),
	SWEETBERRY(() -> Items.SWEET_BERRIES, List.of());

	public final Supplier<Item> fruit;
	public final List<EffectFunc> eff;

	FruitType(Supplier<Item> fruit, List<EffectFunc> eff) {
		this.fruit = fruit;
		this.eff = eff;
	}

}
