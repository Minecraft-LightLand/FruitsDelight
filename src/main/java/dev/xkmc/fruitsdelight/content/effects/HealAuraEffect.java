package dev.xkmc.fruitsdelight.content.effects;

import dev.xkmc.fruitsdelight.init.data.FDModConfig;
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
		return ParticleTypes.HEART;
	}

	protected int getRange() {
		return FDModConfig.SERVER.healEffectRange.get();
	}

	@Override
	protected int getParticleCount(int lv) {
		return 1;
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
		MobEffects.HEAL.value().applyEffectTick(target, amplifier);
	}

}
