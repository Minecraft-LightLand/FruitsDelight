package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;

public class CyclingEffect extends MobEffect {

	public CyclingEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int lv) {
		if (!(entity instanceof ServerPlayer player)) return true;
		if (player.experienceLevel <= 1) return true;
		if (player.takeXpDelay > 0) return true;
		int exp = Math.round(player.experienceProgress * player.getXpNeededForNextLevel());
		player.experienceLevel--;
		exp += player.getXpNeededForNextLevel();
		player.experienceLevel++;
		player.giveExperiencePoints(-exp);
		ExperienceOrb.award(player.serverLevel(), player.position(), exp);
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int tick, int lv) {
		return tick % (20 << lv) == 0;
	}

}
