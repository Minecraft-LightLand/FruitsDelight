package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Supplier;

public enum FruitType {
	APPLE(0xC3833D, () -> Items.APPLE, List.of(new EffectFunc(() -> MobEffects.ABSORPTION, lv -> lv * 20))),
	BLUEBERRY(0x5A32BF, FDBushes.BLUEBERRY::getFruit, List.of(
			new EffectFunc(() -> MobEffects.NIGHT_VISION, lv -> lv * 20),
			new EffectFunc(FDEffects.BRIGHTENING::get, lv -> lv * 20)
	)),
	GLOWBERRY(0xFAAB16, () -> Items.GLOW_BERRIES, List.of(new EffectFunc(() -> MobEffects.GLOWING, lv -> lv * 80))),
	HAMIMELON(0xFFC14E, FDMelons.HAMIMELON::getSlice, List.of(new EffectFunc(() -> MobEffects.HEALTH_BOOST, lv -> lv * 20))),
	MELON(0xC83627, () -> Items.MELON_SLICE, List.of(new EffectFunc(() -> MobEffects.REGENERATION, lv -> lv * 40))),
	HAWBERRY(0xBF1D1D, FDTrees.HAWBERRY::getFruit, List.of(new EffectFunc(FDEffects.APPETIZING::get, lv -> lv * 20))),
	LYCHEE(0xFFF2FC, FDTrees.LYCHEE::getFruit, List.of(new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 10, lv -> 2))),
	MANGO(0xFFB229, FDTrees.MANGO::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 20, lv -> 2),
			new EffectFunc(FDEffects.RAGE_AURA::get, lv -> lv * 20)
	)),
	ORANGE(0xF7970F, FDTrees.ORANGE::getFruit, List.of(
			new EffectFunc(() -> MobEffects.REGENERATION, lv -> lv * 20),
			new EffectFunc(FDEffects.RECOVERING::get, lv -> lv * 20)
	)),
	PEACH(0xE78264, FDTrees.PEACH::getFruit, List.of(new EffectFunc(FDEffects.HEAL_AURA::get, lv -> lv * 10))),
	PEAR(0xBECB32, FDTrees.PEAR::getFruit, List.of(new EffectFunc(FDEffects.LOZENGE::get, lv -> lv * 20))),
	PERSIMMON(0xE15F1E, FDTrees.PERSIMMON::getFruit, List.of(new EffectFunc(FDEffects.ASTRINGENT::get, lv -> lv * 80))),
	PINEAPPLE(0xFDDC28, FDPineapple.PINEAPPLE::getSlice, List.of(
			new EffectFunc(() -> MobEffects.MOVEMENT_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.SWEETENING::get, lv -> lv * 20)
	)),
	LEMON(0xFFE47A, FDBushes.LEMON::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DIG_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.REFRESHING::get, lv -> lv * 20)
	)),
	SWEETBERRY(0xC41A4F, () -> Items.SWEET_BERRIES, List.of());

	public final int color;
	public final Supplier<Item> fruit;
	public final List<EffectFunc> eff;

	FruitType(int color, Supplier<Item> fruit, List<EffectFunc> eff) {
		this.color = color;
		this.fruit = fruit;
		this.eff = eff;
	}

}
