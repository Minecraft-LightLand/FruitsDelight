package dev.xkmc.fruitsdelight.init.food;

import dev.xkmc.fruitsdelight.init.plants.*;
import dev.xkmc.fruitsdelight.init.registrate.FDEffects;
import dev.xkmc.fruitsdelight.init.registrate.FDItems;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public enum FruitType {
	APPLE(2, 0xC3833D, () -> Items.APPLE, List.of(new EffectFunc(MobEffects.ABSORPTION, lv -> lv * 20))),
	BLUEBERRY(4, 0x5A32BF, FDBushes.BLUEBERRY, List.of(
			new EffectFunc(MobEffects.NIGHT_VISION, lv -> lv * 20),
			new EffectFunc(FDEffects.BRIGHTENING, lv -> lv * 20)
	)),
	GLOWBERRY(4, 0xFAAB16, () -> Items.GLOW_BERRIES, List.of(new EffectFunc(MobEffects.GLOWING, lv -> lv * 80))),
	HAMIMELON(4, 0xFFC14E, FDMelons.HAMIMELON::getSlice, List.of(new EffectFunc(MobEffects.HEALTH_BOOST, lv -> lv * 20))),
	MELON(4, 0xC83627, () -> Items.MELON_SLICE, List.of(new EffectFunc(MobEffects.REGENERATION, lv -> lv * 40))),
	HAWBERRY(4, 0xBF1D1D, FDTrees.HAWBERRY, List.of(new EffectFunc(FDEffects.APPETIZING, lv -> lv * 20))),
	LYCHEE(4, 0xFFF2FC, FDTrees.LYCHEE, List.of(new EffectFunc(MobEffects.DAMAGE_BOOST, lv -> lv * 10, lv -> 2))),
	MANGO(2, 0xFFB229, FDTrees.MANGO, List.of(
			new EffectFunc(MobEffects.DAMAGE_BOOST, lv -> lv * 20, lv -> 2),
			new EffectFunc(FDEffects.RAGE_AURA, lv -> lv * 20)
	)),
	ORANGE(2, 0xF7970F, FDTrees.ORANGE, List.of(
			new EffectFunc(MobEffects.REGENERATION, lv -> lv * 20),
			new EffectFunc(FDEffects.RECOVERING, lv -> lv * 20)
	)),
	PEACH(2, 0xE78264, FDTrees.PEACH, List.of(new EffectFunc(FDEffects.HEAL_AURA, lv -> lv * 10))),
	PEAR(2, 0xBECB32, FDTrees.PEAR, List.of(new EffectFunc(FDEffects.LOZENGE, lv -> lv * 20))),
	PERSIMMON(2, 0xE15F1E, FDTrees.PERSIMMON, List.of(new EffectFunc(FDEffects.ASTRINGENT, lv -> lv * 80))),
	PINEAPPLE(4, 0xFDDC28, FDPineapple.PINEAPPLE::getSlice, List.of(
			new EffectFunc(MobEffects.MOVEMENT_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.SWEETENING, lv -> lv * 20)
	)),
	LEMON(2, 0xFFE47A, FDBushes.LEMON, List.of(
			new EffectFunc(MobEffects.DIG_SPEED, lv -> lv * 20),
			new EffectFunc(FDEffects.REFRESHING, lv -> lv * 20)
	)),
	CRANBERRY(4, 0xCF2626, FDBushes.CRANBERRY, List.of(new EffectFunc(FDEffects.SHRINKING, lv -> lv * 80))),
	MANGOSTEEN(2, 0xFBECD6, FDTrees.MANGOSTEEN, List.of(new EffectFunc(FDEffects.SLIDING, lv -> lv * 80))),
	SWEETBERRY(4, 0xC41A4F, () -> Items.SWEET_BERRIES, List.of()),
	CHORUS(2, 0xD0A9D7, () -> Items.CHORUS_FRUIT, List.of(new EffectFunc(FDEffects.CHORUS, lv -> 1, lv -> 0, lv -> Math.min(1, lv * 0.05f)))),
	BAYBERRY(4, 0x541135, FDTrees.BAYBERRY, List.of(new EffectFunc(FDEffects.LEAF_PIERCING, lv -> lv * 20))),
	KIWI(2, 0x89D90D, FDTrees.KIWI, List.of(new EffectFunc(FDEffects.CYCLING, lv -> lv * 4))),
	FIG(2, 0xE08517, FDTrees.FIG, List.of(new EffectFunc(FDEffects.DIGESTING, lv -> lv * 80))),
	DURIAN(2, 0xEDD955, Durian::getSlice, List.of(
			new EffectFunc(FDEffects.ALIENATING, lv -> lv * 40),
			new EffectFunc(FDEffects.SUSPICIOUS_SMELL, lv -> lv * 80)
	)),
	;

	public static FruitType empty() {
		return SWEETBERRY;
	}

	public final int color, jamCost;
	private final Supplier<Item> fruit;
	private final TagKey<Item> tag;
	public final List<EffectFunc> eff;

	FruitType(int jamCost, int color, Supplier<Item> fruit, @Nullable TagKey<Item> tag, List<EffectFunc> eff) {
		this.jamCost = jamCost;
		this.color = 0xFF000000 | color;
		this.fruit = fruit;
		this.tag = tag;
		this.eff = eff;
	}

	FruitType(int jamCost, int color, FruitPlant<?> plant, List<EffectFunc> eff) {
		this(jamCost, color, plant::getFruit, plant.getFruitTag(), eff);
	}

	FruitType(int jamCost, int color, Supplier<Item> fruit, List<EffectFunc> eff) {
		this(jamCost, color, fruit, null, eff);
	}

	public Ingredient getFruitTag() {
		return tag == null ? Ingredient.of(fruit.get()) : Ingredient.of(tag);
	}

	public Item getFruit() {
		return fruit.get();
	}

	public Item getJam() {
		return FDItems.JAM[ordinal()].asItem();
	}

	public Item getJello() {
		return FDItems.JELLO[ordinal()].get();
	}
}
