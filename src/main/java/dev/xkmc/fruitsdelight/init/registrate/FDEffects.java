package dev.xkmc.fruitsdelight.init.registrate;

import dev.xkmc.fruitsdelight.content.effects.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import dev.xkmc.fruitsdelight.init.food.FruitType;
import dev.xkmc.l2library.repack.registrate.builders.NoConfigBuilder;
import dev.xkmc.l2library.repack.registrate.util.entry.RegistryEntry;
import dev.xkmc.l2library.repack.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class FDEffects {

	public static final RegistryEntry<EmptyEffect> ASTRINGENT = genEffect("astringent", () ->
					new EmptyEffect(MobEffectCategory.NEUTRAL, FruitType.PERSIMMON.color),
			"Grant more friction when walking on low-friction blocks, such as ice.");

	public static final RegistryEntry<EmptyEffect> SLIDING = genEffect("sliding", () ->
					new EmptyEffect(MobEffectCategory.NEUTRAL, FruitType.MANGOSTEEN.color),
			"Reduce friction between your boat and blocks.");

	public static final RegistryEntry<SizeEffect> SHRINKING = genEffect("shrinking", () ->
					new SizeEffect(MobEffectCategory.BENEFICIAL, FruitType.CRANBERRY.color),
			"You can crawl into 1-block space when sneaking.");

	public static final RegistryEntry<EmptyEffect> LOZENGE = genEffect("lozenge", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.PEAR.color),
			"Eat and drink faster.");

	public static final RegistryEntry<EmptyEffect> APPETIZING = genEffect("appetizing", () ->
					new EmptyEffect(MobEffectCategory.BENEFICIAL, FruitType.HAWBERRY.color),
			"Eat and drink even when you are full.");

	public static final RegistryEntry<RageAuraEffect> RAGE_AURA = genEffect("rage_aura", () ->
					new RageAuraEffect(MobEffectCategory.NEUTRAL, FruitType.MANGO.color),
			"Mobs around you will be hostile to you");

	public static final RegistryEntry<HealAuraEffect> HEAL_AURA = genEffect("heal_aura", () ->
					new HealAuraEffect(MobEffectCategory.BENEFICIAL, FruitType.PEACH.color),
			"Apply instant health to mobs around you");

	public static final RegistryEntry<EffectRemovalEffect> REFRESHING = genEffect("refreshing", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.LEMON.color, List.of(
					() -> MobEffects.DIG_SLOWDOWN
			)), "Make player immune to mining fatigue");

	public static final RegistryEntry<EffectRemovalEffect> SWEETENING = genEffect("sweetening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.PINEAPPLE.color, List.of(
					() -> MobEffects.MOVEMENT_SLOWDOWN
			)), "Make player immune to slowness");

	public static final RegistryEntry<EffectRemovalEffect> BRIGHTENING = genEffect("brightening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.BLUEBERRY.color, List.of(
					() -> MobEffects.BLINDNESS,
					() -> MobEffects.DARKNESS
			)), "Make player immune to blindness and darkness");

	public static final RegistryEntry<EffectRemovalEffect> RECOVERING = genEffect("recovering", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, FruitType.ORANGE.color, List.of(
					() -> MobEffects.POISON,
					() -> MobEffects.WITHER
			)), "Make player immune to poison and wither");

	public static final RegistryEntry<ChorusEffect> CHORUS = genEffect("chorus", () ->
					new ChorusEffect(MobEffectCategory.NEUTRAL, FruitType.CHORUS.color),
			"Teleport player randomly");

	public static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		FruitsDelight.REGISTRATE.addRawLang("effect." + FruitsDelight.MODID + "." + name + ".description", desc);
		return FruitsDelight.REGISTRATE.entry(name, cb -> new NoConfigBuilder<>(FruitsDelight.REGISTRATE,
						FruitsDelight.REGISTRATE, name, cb, ForgeRegistries.Keys.MOB_EFFECTS, sup))
				.lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
