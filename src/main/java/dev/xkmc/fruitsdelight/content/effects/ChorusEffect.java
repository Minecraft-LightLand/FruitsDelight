package dev.xkmc.fruitsdelight.content.effects;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.Nullable;

public class ChorusEffect extends MobEffect {

	public ChorusEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean isDurationEffectTick(int tick, int lv) {
		return tick == 1;
	}

	@Override
	public void applyEffectTick(LivingEntity user, int lv) {
		teleport(user);
	}

	public void teleport(LivingEntity target) {
		Level level = target.level();
		if (level.isClientSide) return;
		double d0 = target.getX();
		double d1 = target.getY();
		double d2 = target.getZ();

		for (int i = 0; i < 16; ++i) {
			double d3 = target.getX() + (target.getRandom().nextDouble() - 0.5D) * 16.0D;
			double d4 = Mth.clamp(target.getY() + (double) (target.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1);
			double d5 = target.getZ() + (target.getRandom().nextDouble() - 0.5D) * 16.0D;
			if (target.isPassenger()) {
				target.stopRiding();
			}

			Vec3 vec3 = target.position();
			level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(target));
			EntityTeleportEvent.ChorusFruit event = ForgeEventFactory.onChorusFruitTeleport(target, d3, d4, d5);
			if (event.isCanceled()) return;
			if (target.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true)) {
				SoundEvent soundevent = target instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
				level.playSound(null, d0, d1, d2, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
				target.playSound(soundevent, 1.0F, 1.0F);
				break;
			}
		}
	}

}
