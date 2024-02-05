package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.init.plants.FDBushes;
import dev.xkmc.fruitsdelight.init.plants.FDMelons;
import dev.xkmc.fruitsdelight.init.plants.FDPineapple;
import dev.xkmc.fruitsdelight.init.plants.FDTrees;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Supplier;

public enum FruitType {
	APPLE(2, 0xC3833D, () -> Items.APPLE, List.of(new EffectFunc(() -> MobEffects.ABSORPTION, lv -> lv * 20))),
	BLUEBERRY(4, 0x5A32BF, FDBushes.BLUEBERRY::getFruit, List.of(
			new EffectFunc(() -> MobEffects.NIGHT_VISION, lv -> lv * 20),
			new EffectFunc(FDEffects.BRIGHTENING::get, lv -> lv * 20)
	)),
	GLOWBERRY(4, 0xFAAB16, () -> Items.GLOW_BERRIES, List.of(new EffectFunc(() -> MobEffects.GLOWING, lv -> lv * 80))),
	HAMIMELON(4, 0xFFC14E, FDMelons.HAMIMELON::getSlice, List.of(new EffectFunc(() -> MobEffects.HEALTH_BOOST, lv -> lv * 20))),
	MELON(4, 0xC83627, () -> Items.MELON_SLICE, List.of(new EffectFunc(() -> MobEffects.REGENERATION, lv -> lv * 40))),
	HAWBERRY(4, 0xBF1D1D, FDTrees.HAWBERRY::getFruit, List.of(new EffectFunc(FDEffects.APPETIZING::get, lv -> lv * 20))),
	LYCHEE(4, 0xFFF2FC, FDTrees.LYCHEE::getFruit, List.of(new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 10, lv -> 2))),
	MANGO(2, 0xFFB229, FDTrees.MANGO::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DAMAGE_BOOST, lv -> lv * 20, lv -> 2),
			new EffectFunc(FDEffects.RAGE_AURA::get, lv -> lv * 20)
	)),
	ORANGE(2, 0xF7970F, FDTrees.ORANGE::getFruit, List.of(
			new EffectFunc(() -> MobEffects.REGENERATION, lv -> lv * 20),
			new EffectFunc(FDEffects.RECOVERING::get, lv -> lv * 20)
	)),
	PEACH(2, 0xE78264, FDTrees.PEACH::getFruit, List.of(new EffectFunc(FDEffects.HEAL_AURA::get, lv -> lv * 10))),
	PEAR(2, 0xBECB32, FDTrees.PEAR::getFruit, List.of(new EffectFunc(FDEffects.LOZENGE::get, lv -> lv * 20))),
	PERSIMMON(2, 0xE15F1E, FDTrees.PERSIMMON::getFruit, List.of(new EffectFunc(FDEffects.ASTRINGENT::get, lv -> lv * 80))),
	PINEAPPLE(4, 0xFDDC28, FDPineapple.PINEAPPLE::getSlice, List.of(
			new EffectFunc(() -> MobEffects.MOVEMENT_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.SWEETENING::get, lv -> lv * 20)
	)),
	LEMON(2, 0xFFE47A, FDBushes.LEMON::getFruit, List.of(
			new EffectFunc(() -> MobEffects.DIG_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.REFRESHING::get, lv -> lv * 20)
	)),
	CRANBERRY(4, 0xCF2626, FDBushes.CRANBERRY::getFruit, List.of(new EffectFunc(FDEffects.SHRINKING::get, lv -> lv * 80))),
	MANGOSTEEN(2, 0xFBECD6, FDTrees.MANGOSTEEN::getFruit, List.of(new EffectFunc(FDEffects.SLIDING::get, lv -> lv * 80))),
	SWEETBERRY(4, 0xC41A4F, () -> Items.SWEET_BERRIES, List.of()),
	CHORUS(2, 0xD0A9D7, () -> Items.CHORUS_FRUIT, List.of(new EffectFunc(FDEffects.CHORUS::get, lv -> 1, lv -> 0, lv -> Math.min(1, lv * 0.05f)))),
	BAYBERRY(4, 0x541135, FDTrees.BAYBERRY::getFruit, List.of(new EffectFunc(FDEffects.LEAF_PIERCING::get, lv -> lv * 80))),
	KIWI(2, 0x89D90D, FDTrees.KIWI::getFruit, List.of(new EffectFunc(FDEffects.CYCLING::get, lv -> lv * 4))),
	FIG(2, 0xE08517, FDTrees.FIG::getFruit, List.of(new EffectFunc(FDEffects.DIGESTING::get, lv -> lv * 80))),
	;

	public static FruitType empty() {
		return SWEETBERRY;
	}

	public final int color, jellyCost;
	public final Supplier<Item> fruit;
	public final List<EffectFunc> eff;

	FruitType(int jellyCost, int color, Supplier<Item> fruit, List<EffectFunc> eff) {
		this.jellyCost = jellyCost;
		this.color = 0xFF000000 | color;
		this.fruit = fruit;
		this.eff = eff;
	}

	public Item getJelly() {
		return FDItems.JELLY[ordinal()].get().asItem();
	}

	public Item getJello() {
		return FDItems.JELLO[ordinal()].get();
	}
}
