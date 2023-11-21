package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class HealAuraEffect extends RangeRenderEffect {

	public HealAuraEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	protected ParticleOptions getParticle() {
		return ParticleTypes.HAPPY_VILLAGER;
	}

	@Override
	protected int getPeriod() {
		return 60;
	}

	@Override
	protected void searchEntities(LivingEntity entity, int amplifier) {
		super.searchEntities(entity, amplifier);
		applyEffect(entity, entity, amplifier);
	}

	@Override
	protected void applyEffect(LivingEntity le, LivingEntity target, int amplifier) {
		MobEffects.HEAL.applyEffectTick(target, amplifier);
	}

}
