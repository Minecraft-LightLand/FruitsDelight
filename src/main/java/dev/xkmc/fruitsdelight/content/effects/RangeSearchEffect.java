package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RangeSearchEffect extends MobEffect {

	public <T extends Entity> List<T> getEntitiesInRange(LivingEntity center, Class<T> cls) {
		Vec3 pos = center.position();
		return center.level().getEntitiesOfClass(cls, center.getBoundingBox().inflate(getRange()), e -> true)
				.stream().sorted(Comparator.comparingDouble(entcnd -> entcnd.distanceToSqr(pos)))
				.collect(Collectors.toList());
	}

	protected RangeSearchEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	protected abstract int getRange();

	protected int getPeriod() {
		return 10;
	}

	protected boolean applicable(LivingEntity entity) {
		return true;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity.level().isClientSide()) return;
		if (entity.tickCount % getPeriod() == 0) {
			searchEntities(entity, amplifier);
		}
	}

	protected void searchEntities(LivingEntity entity, int amplifier) {
		for (LivingEntity e : getEntitiesInRange(entity, LivingEntity.class)) {
			if (e == entity || !applicable(e)) {
				continue;
			}
			applyEffect(entity, e, amplifier);
		}
	}

	@Override
	public boolean isDurationEffectTick(int tick, int amplifier) {
		return true;
	}

	protected abstract void applyEffect(LivingEntity le, LivingEntity target, int amplifier);

}
