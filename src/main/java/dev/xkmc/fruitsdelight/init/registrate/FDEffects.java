package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.fruitsdelight.content.effects.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class FDEffects {

	public static final SimpleEntry<MobEffect> ASTRINGENT = genEffect("astringent", () ->
					new EmptyEffect(MobEffectCategory.NEUTRAL, FruitType.PERSIMMON.color),
			"Grant more friction when walking on low-friction blocks, such as ice.");

	public static final SimpleEntry<MobEffect> SLIDING = genEffect("sliding", () ->
					new EmptyEffect(MobEffectCategory.NEUTRAL, FruitType.MANGOSTEEN.color),
			"Reduce friction between your boat and blocks.");

	public static final SimpleEntry<MobEffect> SHRINKING = genEffect("shrinking", () ->
					new SizeEffect(MobEffectCategory.BENEFICIAL, FruitType.CRANBERRY.color),
			"You can crawl into 1-block space when sneaking.");

	public static final SimpleEntry<MobEffect> LOZENGE = genEffect("lozenge", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.PEAR.color),
			"Eat and drink faster.");

	public static final SimpleEntry<MobEffect> APPETIZING = genEffect("appetizing", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.HAWBERRY.color),
			"Eat and drink even when you are full.");

	public static final SimpleEntry<MobEffect> RAGE_AURA = genEffect("rage_aura", () ->
					new RageAuraEffect(MobEffectCategory.NEUTRAL, FruitType.MANGO.color),
			"Mobs around you will be hostile to you");

	public static final SimpleEntry<MobEffect> HEAL_AURA = genEffect("heal_aura", () ->
					new HealAuraEffect(MobEffectCategory.BENEFICIAL, FruitType.PEACH.color),
			"Apply instant health to mobs around you");

	public static final SimpleEntry<MobEffect> REFRESHING = genEffect("refreshing", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.LEMON.color, List.of(
					() -> MobEffects.DIG_SLOWDOWN
			)), "Make player immune to mining fatigue");

	public static final SimpleEntry<MobEffect> SWEETENING = genEffect("sweetening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.PINEAPPLE.color, List.of(
					() -> MobEffects.MOVEMENT_SLOWDOWN
			)), "Make player immune to slowness");

	public static final SimpleEntry<MobEffect> BRIGHTENING = genEffect("brightening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.BLUEBERRY.color, List.of(
					() -> MobEffects.BLINDNESS,
					() -> MobEffects.DARKNESS
			)), "Make player immune to blindness and darkness");

	public static final SimpleEntry<MobEffect> RECOVERING = genEffect("recovering", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.ORANGE.color, List.of(
					() -> MobEffects.POISON,
					() -> MobEffects.WITHER
			)), "Make player immune to poison and wither");

	public static final SimpleEntry<MobEffect> CHORUS = genEffect("chorus", () ->
					new ChorusEffect(MobEffectCategory.NEUTRAL, FruitType.CHORUS.color),
			"Teleport player randomly");

	public static final SimpleEntry<MobEffect> DIGESTING = genEffect("digesting", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.FIG.color),
			"Overflowing nutrition becomes saturation.");

	public static final SimpleEntry<MobEffect> LEAF_PIERCING = genEffect("leaf_piercing", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.BAYBERRY.color),
			"Projectiles player shot will go through leaves");

	public static final SimpleEntry<MobEffect> CYCLING = genEffect("cycling", () ->
					new CyclingEffect(MobEffectCategory.BENEFICIAL, FruitType.KIWI.color),
			"Player will drop 1 level of experience as orbs every second");

	public static final SimpleEntry<MobEffect> DISGUSTED = genEffect("disgusted", () ->
					new EmptyEffect(MobEffectCategory.HARMFUL, FruitType.DURIAN.color),
			"Entity with this effect will not breed");

	public static final SimpleEntry<MobEffect> ALIENATING = genEffect("alienating", () ->
					new AlienatingAuraEffect(MobEffectCategory.BENEFICIAL, FruitType.DURIAN.color),
			"Apply disgusted and weakness effect to surrounding entities without this effect");

	public static final SimpleEntry<MobEffect> SUSPICIOUS_SMELL = genEffect("suspicious_smell", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.DURIAN.color),
			"You can see effects in suspicious stew and find suspicious sand easily");


	private static <T extends MobEffect> SimpleEntry<MobEffect> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return new SimpleEntry<>(FruitsDelight.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register());
	}

	public static void register() {

	}

}
