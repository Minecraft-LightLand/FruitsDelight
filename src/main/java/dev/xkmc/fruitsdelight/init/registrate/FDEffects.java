package dev.xkmc.fruitsdelight.init.registrate;

import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import dev.xkmc.fruitsdelight.content.effects.*;
import dev.xkmc.fruitsdelight.init.FruitsDelight;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public class FDEffects {

	public static final RegistryEntry<AstringentEffect> ASTRINGENT = genEffect("astringent", () ->
					new AstringentEffect(MobEffectCategory.NEUTRAL, 0xffffff),
			"Grant more friction when walking on low-friction blocks, such as ice.");

	public static final RegistryEntry<LozengeEffect> LOZENGE = genEffect("lozenge", () ->
					new LozengeEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Eat and drink faster.");

	public static final RegistryEntry<AppetizingEffect> APPETIZING = genEffect("appetizing", () ->
					new AppetizingEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Eat and drink even when you are full.");

	public static final RegistryEntry<RageAuraEffect> RAGE_AURA = genEffect("rage_aura", () ->
					new RageAuraEffect(MobEffectCategory.HARMFUL, 0xffffff),
			"Mobs around you will be hostile to you");

	public static final RegistryEntry<HealAuraEffect> HEAL_AURA = genEffect("heal_aura", () ->
					new HealAuraEffect(MobEffectCategory.BENEFICIAL, 0xffffff),
			"Apply instant health to mobs around you");

	public static final RegistryEntry<EffectRemovalEffect> REFRESHING = genEffect("refreshing", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, 0xffffff, List.of(
					() -> MobEffects.DIG_SLOWDOWN
			)), "Make player immune to mining fatigue");

	public static final RegistryEntry<EffectRemovalEffect> SWEETENING = genEffect("sweetening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, 0xffffff, List.of(
					() -> MobEffects.MOVEMENT_SLOWDOWN
			)), "Make player immune to slowness");

	public static final RegistryEntry<EffectRemovalEffect> BRIGHTENING = genEffect("brightening", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, 0xffffff, List.of(
					() -> MobEffects.BLINDNESS,
					() -> MobEffects.DARKNESS
			)), "Make player immune to blindness and darkness");

	public static final RegistryEntry<EffectRemovalEffect> RECOVERING = genEffect("recovering", () ->
			new EffectRemovalEffect(MobEffectCategory.BENEFICIAL, 0xffffff, List.of(
					() -> MobEffects.POISON,
					() -> MobEffects.WITHER
			)), "Make player immune to poison and wither");


	private static <T extends MobEffect> RegistryEntry<T> genEffect(String name, NonNullSupplier<T> sup, String desc) {
		return FruitsDelight.REGISTRATE.effect(name, sup, desc).lang(MobEffect::getDescriptionId).register();
	}

	public static void register() {

	}

}
