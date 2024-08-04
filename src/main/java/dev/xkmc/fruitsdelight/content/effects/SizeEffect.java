package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SizeEffect extends MobEffect {

	public SizeEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		entity.refreshDimensions();
	}

}
