package dev.xkmc.fruitsdelight.content.effects;

import dev.xkmc.fruitsdelight.init.data.FDModConfig;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class RageAuraEffect extends RangeRenderEffect {

	public RageAuraEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	protected ParticleOptions getParticle() {
		return ParticleTypes.ANGRY_VILLAGER;
	}

	protected int getRange() {
		return FDModConfig.SERVER.rageEffectRange.get();
	}

	@Override
	protected int getParticleCount(int lv) {
		return 1;
	}

	@Override
	protected void applyEffect(LivingEntity le, LivingEntity target, int amplifier) {
		if (target instanceof Mob mob && mob.canAttack(le) && mob.getTarget() == null) {
			mob.setTarget(le);
		}
	}

}
