package dev.xkmc.fruitsdelight.init.food;

import it.unimi.dsi.fastutil.ints.Int2FloatFunction;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public record EffectFunc(Holder<MobEffect> eff,
						 Int2IntFunction duration,
						 Int2IntFunction amplifier,
						 Int2FloatFunction chance) {

	public EffectFunc(Holder<MobEffect> eff,
					  Int2IntFunction duration,
					  Int2IntFunction amplifier) {
		this(eff, duration, amplifier, x -> 1);
	}

	public EffectFunc(Holder<MobEffect> eff,
					  Int2IntFunction duration) {
		this(eff, duration, x -> 0, x -> 1);
	}

	public float getChance(int effectLevel) {
		return chance.get(effectLevel);
	}

	public MobEffectInstance getEffect(int effectLevel) {
		return new MobEffectInstance(eff, duration.get(effectLevel), amplifier.get(effectLevel));
	}
}
